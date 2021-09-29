package com.example.picturesoftheday.viewmodel

import android.annotation.SuppressLint
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.picturesoftheday.BuildConfig
import com.example.picturesoftheday.app.App
import com.example.picturesoftheday.repository.DataPOD
import com.example.picturesoftheday.repository.LocalRepositoryImpl
import com.example.picturesoftheday.repository.PODRetrofitImpl
import com.example.picturesoftheday.repository.dto.MarsPhotosServerResponseData
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
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl(),
    private val picturesRepository: LocalRepositoryImpl = LocalRepositoryImpl(App.getPicturesDao())
) : ViewModel() {
    private val apiKey = BuildConfig.NASA_API_KEY
    fun getLiveData(): LiveData<AppState> {
        return liveDataToObserve
    }
    fun saveDataPODToDB(dataPOD: DataPOD){
        picturesRepository.saveEntity(dataPOD)
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
    fun getMarsPicture() {
        liveDataToObserve.postValue(AppState.Loading)
        val earthDate = getDayBeforeYesterday()
        retrofitImpl.getMarsPictureByDate(earthDate,BuildConfig.NASA_API_KEY, marsCallback)
    }
    private val PODCallback = object : Callback<PODServerResponseData> {
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
    private val PODCallbacklist = object : Callback<List<PODServerResponseData>> {
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
    private val solarFlareCallback = object : Callback<List<SolarFlareResponseData>> {
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
     private fun getDayBeforeYesterday(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val yesterday = LocalDateTime.now().minusDays(2)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            return yesterday.format(formatter)
        } else {
            val cal: Calendar = Calendar.getInstance()
            val s = SimpleDateFormat("yyyy-MM-dd")
            cal.add(Calendar.DAY_OF_YEAR, -2)
            return s.format(cal.time)
        }
    }

    private val marsCallback = object : Callback<MarsPhotosServerResponseData> {

        override fun onResponse(
            call: Call<MarsPhotosServerResponseData>,
            response: Response<MarsPhotosServerResponseData>,
        ) {
            if (response.isSuccessful && response.body() != null) {
                liveDataToObserve.postValue(AppState.SuccessMars(response.body()!!))
            } else {
                val message = response.message()
                if (message.isNullOrEmpty()) {
                    liveDataToObserve.postValue(AppState.Error(Throwable("dsfsdfsdfsdfsdf")))
                } else {
                    liveDataToObserve.postValue(AppState.Error(Throwable(message)))
                }
            }
        }

        override fun onFailure(call: Call<MarsPhotosServerResponseData>, t: Throwable) {
            liveDataToObserve.postValue(AppState.Error(t))
        }
    }
    @SuppressLint("SimpleDateFormat")
    fun getDate(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val yesterday = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            yesterday.format(formatter)
        } else {
            val cal: Calendar = Calendar.getInstance()
            val s = SimpleDateFormat("yyyy-MM-dd")
            s.format(cal)
        }
    }
}
