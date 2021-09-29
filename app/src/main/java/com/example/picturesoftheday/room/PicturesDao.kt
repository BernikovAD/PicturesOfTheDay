package com.example.picturesoftheday.room

import androidx.room.*

@Dao
interface PicturesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entityPOD: EntityPOD)

    @Query("SELECT * FROM EntityPOD")
    fun selectAll(): List<EntityPOD>

    @Query("SELECT * FROM EntityPOD WHERE date = :date")
    fun selectByDate(date: String): List<EntityPOD>

    @Query("DELETE FROM EntityPOD WHERE  date = :date")
    fun deleteByDate(date: String)
    @Delete
    fun delete(entityPOD: EntityPOD)
}