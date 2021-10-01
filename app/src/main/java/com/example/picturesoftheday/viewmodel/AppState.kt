package com.example.picturesoftheday.viewmodel

import com.example.picturesoftheday.repository.dto.MarsPhotosServerResponseData
import com.example.picturesoftheday.repository.dto.PODServerResponseData
import com.example.picturesoftheday.repository.dto.SolarFlareResponseData

sealed class AppState {
    data class SuccessWeather(val solarFlareResponseData: List<SolarFlareResponseData>) : AppState()
    data class Success(val serverResponseData: PODServerResponseData) : AppState()
    data class SuccessPODDate(val serverResponseData: List<PODServerResponseData>) : AppState()
    data class SuccessMars(val marsServerResponseData: MarsPhotosServerResponseData) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()

}

