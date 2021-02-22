package com.android.technicalassignment.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface APIService {

    @GET("list")
    fun getData(): Call<ResponseBody>


    companion object {
        operator fun invoke(): APIService {
            return Retrofit.Builder()
                .baseUrl("https://picsum.photos/")
                .build()
                .create(APIService::class.java)
        }
    }

}