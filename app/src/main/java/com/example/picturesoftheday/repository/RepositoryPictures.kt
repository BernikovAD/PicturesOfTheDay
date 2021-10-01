package com.example.picturesoftheday.repository

import androidx.lifecycle.LiveData
import com.example.picturesoftheday.model.EntityPictures
import com.example.picturesoftheday.room.PicturesDao

class RepositoryPictures(private val picturesDao: PicturesDao) {

    val readAllData: LiveData<List<EntityPictures>> = picturesDao.readAllData()

    fun addPictures(entityPictures: EntityPictures) {
        picturesDao.addPOD(entityPictures)
    }

    fun deletePicture(entityPictures: EntityPictures) {
        picturesDao.deletePicture(entityPictures)
    }

    fun deleteAllPictures() {
        picturesDao.deleteAllPictures()
    }
}