package com.example.picturesoftheday.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(EntityPOD::class),version = 1)
abstract class PODDataBase : RoomDatabase() {
    abstract fun historyDao():PicturesDao
}
