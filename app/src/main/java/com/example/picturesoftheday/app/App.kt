package com.example.picturesoftheday.app

import android.app.Application
import androidx.room.Room
import com.example.picturesoftheday.room.PODDataBase
import com.example.picturesoftheday.room.PicturesDao


class App :Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }
    companion object{
        private var appInstance : App? = null
        private var db: PODDataBase? = null
        private var nameDB = "PicturesDB"

        fun getPicturesDao():PicturesDao{
            if(db==null){
                val builder = Room.databaseBuilder(appInstance!!.applicationContext,
                    PODDataBase::class.java,
                    nameDB)
                db = builder.allowMainThreadQueries().build()
            }
            return db!!.historyDao()
        }
    }
}