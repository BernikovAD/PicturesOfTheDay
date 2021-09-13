package com.example.picturesoftheday.viewmodel

import com.example.picturesoftheday.repository.PODServerResponseData

sealed class PODData {
    data class Success(val serverResponseData: PODServerResponseData):PODData()
    data class Error(val error: Throwable):PODData()
    object Loading: PODData()

}

