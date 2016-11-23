package com.peterombodi.newconverterlab.data.api;

import com.peterombodi.newconverterlab.data.model.DataResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Admin on 17.11.2016.
 */

public interface ApiRest {
    @GET("ru/public/currency-cash.json")
    Call<DataResponse> connect();
}
