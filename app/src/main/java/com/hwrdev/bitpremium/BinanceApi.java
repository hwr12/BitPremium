package com.planet.premium;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.planet.premium.activity.sidesheet.AssetViewModel;
import com.planet.premium.activity.sidesheet.BinanceApiService;
import com.planet.premium.model.Asset;
import com.planet.premium.utils.ModelError;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import tech.gusavila92.apache.commons.codec.binary.Hex;
import tech.gusavila92.websocketclient.WebSocketClient;

public class BinanceApi {
    final String apiKey;
    final String apiSecret;
    private WebSocketClient mWebSocketClient;
    private Retrofit retrofit;
    BinanceApiService service;
    String listenKey;
    Context ctx;
    AssetViewModel model;


    interface RevealDetailsCallbacks {
        public String getDataFromResult(List<String> details);
    }
    RevealDetailsCallbacks callback;

    public BinanceApi(String apiKey, String apiSecret, Context ctx){
        this.ctx = ctx;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.binance.com")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(BinanceApiService.class);

        this.callback = new RevealDetailsCallbacks() {
            @Override
            public String getDataFromResult(List<String> details) {
                //Do stuff here with the returned list of Strings

                return details.toString();
            }
        };

        model = new ViewModelProvider((ViewModelStoreOwner) ctx).get(AssetViewModel.class);
    }

    public void walletSnapShot()  {
        long timestamp = getTimestamp();
        String data = service.getAccountSnapshot(apiKey, null, timestamp, null).request().url().query() ;

        Log.d("data", data);
        String signature = "";
        try {
            signature = encode(apiSecret, data);
            Log.d("sinature", signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        service.getAccountSnapshot(apiKey, null, timestamp, signature).enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                JsonObject jsonObject = response.body();

                JsonArray jsonArray = jsonObject.getAsJsonArray("balances");
                List<Asset> assets = new ArrayList<>();
                for(int i = 0; i < jsonArray.size() ; i++) {
                    Asset asset = new Asset();
                    JsonObject jsObject = jsonArray.get(i).getAsJsonObject();

                    if (jsObject.get("free").getAsDouble() == 0) {

                    } else {
                        asset.ticker = jsObject.get("asset").getAsString();
                        asset.amount = jsObject.get("free").getAsString();
                        assets.add(asset);
                    }


                }
                Log.d("asset", assets.get(0).ticker);

                model.update(assets);


                Log.d("accountSnapshot", jsonArray.toString());

                if (response.errorBody() != null ) {
                    Converter<ResponseBody, ModelError> errorConverter =
                            retrofit.responseBodyConverter(ModelError.class, new Annotation[0]);
                    try {
                        ModelError error = errorConverter.convert(response.errorBody());
                        Log.d("accountSnapshot", error.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Now use error.getMessage()
                }



            }
            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                Log.d("accountSnapshot", t.getMessage());
            }
        });
    }



    public static String encode(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    }

    public long getTimestamp(){
        Date date= new Date();
        long time = date.getTime();
        return time;
    }



    public void requestListenKey(){
        service.startUserDataStream(apiKey).enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                JsonObject jsonObject = response.body();
                listenKey = jsonObject.get("listenKey").getAsString();
                userDataStreamWS(listenKey);
                Log.d("requestListenKey", listenKey);
            }
            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                Log.d("requestListenKey", t.getMessage());
            }
        });
    }

    public void userDataStreamWS(String listenKey){
        URI uri = URI.create("wss://stream.binance.com:9443/ws/" + listenKey);


        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.d("websocketRecieved","open");
            }

            @Override
            public void onTextReceived(String message) {
                walletSnapShot();
                try {
                    JSONObject jsonObject = new JSONObject(message);
                    if(jsonObject.get("e").equals("outboundAccountPosition")) {

                        JSONArray update_list = new JSONArray();
                        update_list = jsonObject.getJSONArray("B");
                        for (int i = 0; i < update_list.length(); i++) {

                            JSONObject asset_update = new JSONObject(update_list.get(i).toString());
                            Asset asset = new  Asset();
                            asset.ticker = asset_update.getString("a");
                            asset.amount = asset_update.getString("f");
                            //model.update_balance(asset);

                            Log.d("asset",  asset_update.getString("a"));
                        }
                    } else if(jsonObject.get("e").equals("balanceUpdate")){




                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("websocketRecieved", message);



            }

            @Override
            public void onBinaryReceived(byte[] data) {

            }

            @Override
            public void onPingReceived(byte[] data) {

            }

            @Override
            public void onPongReceived(byte[] data) {

            }

            @Override
            public void onException(Exception e) {

            }

            @Override
            public void onCloseReceived() {

            }


        };
        mWebSocketClient.connect();
    }

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("wss://stream.binance.com:9443");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {

            }

            @Override
            public void onTextReceived(String message) {

            }

            @Override
            public void onBinaryReceived(byte[] data) {

            }

            @Override
            public void onPingReceived(byte[] data) {

            }

            @Override
            public void onPongReceived(byte[] data) {

            }

            @Override
            public void onException(Exception e) {

            }

            @Override
            public void onCloseReceived() {

            }


        };
        mWebSocketClient.connect();
    }



}
