package com.hwrdev.bitpremium.activity;


import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Binance's REST API URL mappings and endpoint security configuration.
 */
public interface UpbitApiService {

    // General endpoints

    @GET("/api/v1/ping")
    Call<Void> ping();


//daily 스냅샷으로 실시간 업데이트 정보를 가져오지 아니함
//    @GET("/sapi/v1/accountSnapshot")
//    Call<JsonObject> getAccountSnapshot(@Header("X-MBX-APIKEY") String ApiKey,   @Query("type") String type, @Query("startTime") Long startTime,@Query("endTime") Long endTime,@Query("limit") Integer limit, @Query("recvWindow") Long recvWindow, @Query("timestamp") Long timestamp, @Query("signature") String signature);


    @GET("/info/balance")
    Call<JsonObject> getBalanceInfo(@Header("Authorization") String jwt);





}