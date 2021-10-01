package com.example.picturesoftheday.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.picturesoftheday.model.EntityPictures

@Database(entities = [EntityPictures::class],version = 1)
abstract class PicturesDatabase :RoomDatabase(){
    abstract fun picturesDao(): PicturesDao

    companion object{
        @Volatile
        private var INSTANCE: PicturesDatabase? = null

        fun getDatabase(context:Context): PicturesDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null ) return tempInstance
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PicturesDatabase::class.java,
                    "POD_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}