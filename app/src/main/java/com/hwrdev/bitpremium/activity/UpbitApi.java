package com.hwrdev.bitpremium.activity;


import java.io.IOException;
import java.util.UUID;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.auth0.android.request.internal.Jwt;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.hwrdev.bitpremium.activity.bithumbApi.Api_Client;
import com.hwrdev.bitpremium.activity.sidesheet.AssetViewModel;
import com.hwrdev.bitpremium.activity.sidesheet.BinanceApiService;
import com.hwrdev.bitpremium.model.Asset;
import com.hwrdev.bitpremium.model.BithumbAsset;
import com.hwrdev.bitpremium.model.UpbitAsset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import tech.gusavila92.apache.commons.codec.binary.Hex;
import tech.gusavila92.apache.http.HttpEntity;
import tech.gusavila92.apache.http.HttpResponse;
import tech.gusavila92.apache.http.util.EntityUtils;
import tech.gusavila92.websocketclient.WebSocketClient;

public class UpbitApi {
    final String apiKey;
    final String apiSecret;
    private WebSocketClient mWebSocketClient;
    private Retrofit retrofit;
    UpbitApiService service;
    String listenKey;
    Context ctx;
    AssetViewModel model;
    Api_Client api;
    interface RevealDetailsCallbacks {
        public String getDataFromResult(List<String> details);
    }
    RevealDetailsCallbacks callback;
    List<UpbitAsset> assets = new ArrayList<>();
    public UpbitApi(String apiKey, String apiSecret, Context ctx){
        this.ctx = ctx;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        api = new Api_Client(apiKey,
                apiSecret);
        model = new ViewModelProvider((ViewModelStoreOwner) ctx).get(AssetViewModel.class);

    }



    public void walletSnapShot()  {
        String accessKey = System.getenv("UPBIT_OPEN_API_ACCESS_KEY");
        String secretKey = System.getenv("UPBIT_OPEN_API_SECRET_KEY");
        String serverUrl = System.getenv("https://api.upbit.com");
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.upbit.com")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        String jwtToken = Jwts.builder().claim("access_key",accessKey).claim("nonce", UUID.randomUUID().toString())
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
        Log.v("JWT : - ",jwtToken);

        service = retrofit.create(UpbitApiService.class);
        String authenticationToken = "Bearer " + jwtToken;
        service.getBalanceInfo(authenticationToken).enqueue(new retrofit2.Callback<JsonObject>(){
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                JsonArray jsonArray = response.body().getAsJsonArray();
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
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });


        new Thread() {
            public void run() {
                try {
                    String result = api.callApi("/info/balance", rgParams);
                    Log.d("자산정보", result);
                    JSONObject jsonObject = new JSONObject(result).getJSONObject("data");

                    Map<String, Object> map = getMapFromJsonObject(jsonObject);


                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        if((Double) Double.parseDouble(entry.getValue().toString()) != 0 && entry.getKey().contains("available")){
                            BithumbAsset asset = new BithumbAsset();
                            asset.ticker = entry.getKey().substring(10).toUpperCase();
                            asset.amount = (String) entry.getValue();
                            assets.add(asset);
                            Log.d("빗썸", asset.ticker + "+" + asset.amount);
                        }
                    }
                    model.updateBithumb(assets);



                } catch (Exception e) {
                    Log.d("자산정보", "가져오기 실패");
                    e.printStackTrace();
                }
            }
        }.start();
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
