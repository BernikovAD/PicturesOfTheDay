package com.example.picturesoftheday.repository

import com.example.picturesoftheday.room.EntityPOD
import com.example.picturesoftheday.room.PicturesDao

class LocalRepositoryImpl(private val picturesDao: PicturesDao) : LocalRepository {
    override fun getAllPictures(): List<DataPOD> {
        return convertEntityToModel(picturesDao.selectAll())
    }

    override fun saveEntity(dataPOD:DataPOD) {
        convertModelToEntity(dataPOD)?.let { picturesDao.insert(it) }
    }
}

fun convertModelToEntity(dataPOD:DataPOD): EntityPOD? {
   return dataPOD.date?.let { dataPOD.urlPicture?.let { it1 -> EntityPOD(dataPOD.id, it, it1) } }
}
fun convertEntityToModel(entityPOD: List<EntityPOD>): List<DataPOD> {
    return entityPOD.map {
        DataPOD(it.id, it.date, it.url)
    }
}