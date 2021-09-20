package com.example.picturesoftheday.repository

import com.example.picturesoftheday.repository.dto.MarsServerResponseData
import com.example.picturesoftheday.repository.dto.PODServerResponseData
import com.example.picturesoftheday.repository.dto.SolarFlareResponseData
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PODRetrofitImpl {
    private val baseUrl = "https://api.nasa.gov/"

    private val api by lazy {
        Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build().create(RetrofitAPI::class.java)
    }

    fun getPictureOfTheDay(apiKey: String, podCallback: Callback<PODServerResponseData>) {
        api.getPictureOfTheDay(apiKey).enqueue(podCallback)
    }

    fun getPictureOfTheDayDate(
        startDay: String,
        endDate: String,
        apiKey: String, podCallback: Callback<List<PODServerResponseData>>
    ) {
        api.getPictureOfTheDayDate(startDay, endDate, apiKey).enqueue(podCallback)
    }

    fun getSolarFlareToday(
        startDate: String,
        apiKey: String,
        podCallback: Callback<List<SolarFlareResponseData>>
    ) {
        api.getSolarFlareToday(startDate, apiKey).enqueue(podCallback)
    }

    fun getSolarFlare(
        startDate: String,
        endDate: String,
        apiKey: String,
        podCallback: Callback<List<SolarFlareResponseData>>
    ) {
        api.getSolarFlare(startDate, endDate, apiKey).enqueue(podCallback)
    }

    fun getMarsPictureByDate(
        sol: Int,
        apiKey: String,
        marsCallbackByDate: Callback<List<MarsServerResponseData>>
    ) {
        api.getMarsImageByDate(sol, apiKey).enqueue(marsCallbackByDate)
    }
}