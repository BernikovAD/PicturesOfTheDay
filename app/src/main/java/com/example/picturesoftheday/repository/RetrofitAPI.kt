package com.example.picturesoftheday.repository

import com.example.picturesoftheday.repository.dto.MarsPhotosServerResponseData
import com.example.picturesoftheday.repository.dto.MarsServerResponseData
import com.example.picturesoftheday.repository.dto.PODServerResponseData
import com.example.picturesoftheday.repository.dto.SolarFlareResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAPI {
    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String): Call<PODServerResponseData>

    @GET("planetary/apod")
    fun getPictureOfTheDayDate(
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
        @Query("api_key") apiKey: String
    ): Call<List<PODServerResponseData>>

    @GET("DONKI/FLR")
    fun getSolarFlare(
        @Query("startDate") startDate: String = "2021-09-07",
        @Query("endDate") endDate: String = "2021-09-19",
        @Query("api_key") apiKey: String
    ): Call<List<SolarFlareResponseData>>

    @GET("DONKI/FLR")
    fun getSolarFlareToday(
        @Query("startDate") startDate: String = "2021-09-07",
        @Query("api_key") apiKey: String
    ): Call<List<SolarFlareResponseData>>

    @GET("/mars-photos/api/v1/rovers/curiosity/photos")
    fun getMarsImageByDate(
        @Query("earth_date") earth_date: String,
        @Query("api_key") apiKey: String,
    ): Call<MarsPhotosServerResponseData>
}