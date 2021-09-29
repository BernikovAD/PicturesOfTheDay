package com.example.picturesoftheday.repository

interface LocalRepository {

    fun getAllPictures(): List<DataPOD>
    fun saveEntity(dataPOD:DataPOD)
}