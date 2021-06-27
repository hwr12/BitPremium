package com.planet.premium.activity.sidesheet;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.*;

/**
 * Binance's REST API URL mappings and endpoint security configuration.
 */
public interface BinanceApiService {

    // General endpoints

    @GET("/api/v1/ping")
    Call<Void> ping();

    @GET("/sapi/v1/capital/config/getall")
    Call<JsonObject> getWalletInfo(@Header("X-MBX-APIKEY") String ApiKey);

//daily 스냅샷으로 실시간 업데이트 정보를 가져오지 아니함
//    @GET("/sapi/v1/accountSnapshot")
//    Call<JsonObject> getAccountSnapshot(@Header("X-MBX-APIKEY") String ApiKey,   @Query("type") String type, @Query("startTime") Long startTime,@Query("endTime") Long endTime,@Query("limit") Integer limit, @Query("recvWindow") Long recvWindow, @Query("timestamp") Long timestamp, @Query("signature") String signature);


    @GET("/api/v3/account")
    Call<JsonObject> getAccountSnapshot(@Header("X-MBX-APIKEY") String ApiKey,   @Query("recvWindow") Long recvWindow, @Query("timestamp") Long timestamp, @Query("signature") String signature);


    @POST("/api/v3/userDataStream")
    Call<JsonObject> startUserDataStream(@Header("X-MBX-APIKEY") String ApiKey);


    @PUT("/api/v1/userDataStream")
    Call<Void> keepAliveUserDataStream(@Query("listenKey") String listenKey);


    @DELETE("/api/v1/userDataStream")
    Call<Void> closeAliveUserDataStream(@Query("listenKey") String listenKey);



}