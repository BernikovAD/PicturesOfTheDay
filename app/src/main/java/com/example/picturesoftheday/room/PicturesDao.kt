package com.example.picturesoftheday.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.picturesoftheday.model.EntityPictures

@Dao
interface PicturesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addPOD(entityPOD: EntityPictures)

    @Query("SELECT * FROM POD_table ORDER BY id ASC")
    fun readAllData(): LiveData<MutableList<EntityPictures>>

    @Delete
     fun deletePicture(entityPOD: EntityPictures)

    @Query("DELETE FROM POD_table")
     fun deleteAllPictures()
}