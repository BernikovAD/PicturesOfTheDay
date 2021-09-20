package com.example.picturesoftheday.repository.dto

import com.google.gson.annotations.SerializedName

data class SolarFlareResponseData(
    @field:SerializedName("flrID") val flrID: String,
    @field:SerializedName("beginTime") val beginTime: String,
    @field:SerializedName("peakTime") val peakTime: String,
    @field:SerializedName("endTime") val endTime: Any? = null,
    @field:SerializedName("classType") val classType: String,
    @field:SerializedName("sourceLocation") val sourceLocation: String,
    @field:SerializedName("activeRegionNum") val activeRegionNum: Long,
    @field:SerializedName("link") val link: String
)
