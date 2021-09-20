package com.example.picturesoftheday.viewmodel

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.picturesoftheday.BuildConfig
import com.example.picturesoftheday.repository.PODRetrofitImpl
import com.example.picturesoftheday.repository.dto.MarsServerResponseData
import com.example.picturesoftheday.repository.dto.PODServerResponseData
import com.example.picturesoftheday.repository.dto.SolarFlareResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class PODViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) : ViewModel() {
    private val apiKey = BuildConfig.NASA_API_KEY
    fun getLiveData(): LiveData<AppState> {
        return liveDataToObserve
    }

    fun getPOD() {
        liveDataToObserve.postValue(AppState.Loading)
        if (apiKey.isNotBlank()) {
            retrofitImpl.getPictureOfTheDay(apiKey, PODCallback)
        }
    }
    fun getPOD(startDate: String, endDate:String) {
        liveDataToObserve.postValue(AppState.Loading)
        if (apiKey.isNotBlank()) {
            retrofitImpl.getPictureOfTheDayDate(startDate,endDate, apiKey,PODCallbacklist)
        }
    }
    fun getSolarFlare(startDate:String,endDate:String){
        liveDataToObserve.postValue(AppState.Loading)
        if(apiKey.isNotBlank()){
            retrofitImpl.getSolarFlare(startDate,endDate,apiKey,solarFlareCallback)
        }
    }
    fun getSolarFlare(today:String){
        liveDataToObserve.postValue(AppState.Loading)
        if(apiKey.isNotBlank()){
            retrofitImpl.getSolarFlareToday(today,apiKey,solarFlareCallback)
        }
    }
    val PODCallback = object : Callback<PODServerResponseData> {
        override fun onResponse(
            call: Call<PODServerResponseData>,
            response: Response<PODServerResponseData>
        ) {
            if (response.isSuccessful && response.body() != null) {
                liveDataToObserve.postValue(AppState.Success(response.body()!!))
            }
        }
        override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
            liveDataToObserve.postValue(AppState.Error(t))
        }
    }
    val PODCallbacklist = object : Callback<List<PODServerResponseData>> {
        override fun onResponse(
            call: Call<List<PODServerResponseData>>,
            response: Response<List<PODServerResponseData>>
        ) {
            if (response.isSuccessful && response.body() != null) {
                liveDataToObserve.postValue(AppState.SuccessPODDate(response.body()!!))
            }
        }
        override fun onFailure(call: Call<List<PODServerResponseData>>, t: Throwable) {
            liveDataToObserve.postValue(AppState.Error(t))
        }
    }
    val solarFlareCallback = object : Callback<List<SolarFlareResponseData>> {
        override fun onResponse(
            call: Call<List<SolarFlareResponseData>>,
            response: Response<List<SolarFlareResponseData>>
        ) {
            if (response.isSuccessful && response.body() != null) {
                liveDataToObserve.postValue(AppState.SuccessWeather(response.body()!!))
            }
        }

        override fun onFailure(call: Call<List<SolarFlareResponseData>>, t: Throwable) {
            liveDataToObserve.postValue(AppState.Error(t))
        }
    }
    fun getMarsPicture() {
        liveDataToObserve.postValue(AppState.Loading)
        retrofitImpl.getMarsPictureByDate(1000,apiKey, marsCallback)
    }
    private val marsCallback = object : Callback<List<MarsServerResponseData>> {
        override fun onResponse(
            call: Call<List<MarsServerResponseData>>,
            response: Response<List<MarsServerResponseData>>,
        ) {
            if (response.isSuccessful && response.body() != null) {
                liveDataToObserve.postValue(AppState.SuccessMars(response.body()!!))
            } else {
                val message = response.message()
                if (message.isNullOrEmpty()) {
                    liveDataToObserve.postValue(AppState.Error(Throwable("Unidentified error")))
                } else {
                    liveDataToObserve.postValue(AppState.Error(Throwable(message)))
                }
            }
        }

        override fun onFailure(call: Call<List<MarsServerResponseData>>, t: Throwable) {
            liveDataToObserve.postValue(AppState.Error(t))
        }
    }
    @SuppressLint("SimpleDateFormat")
    fun getDate(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val yesterday = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            return yesterday.format(formatter)
        } else {
            val cal: Calendar = Calendar.getInstance()
            val s = SimpleDateFormat("yyyy-MM-dd")
            return s.format(cal)
        }
    }
}
