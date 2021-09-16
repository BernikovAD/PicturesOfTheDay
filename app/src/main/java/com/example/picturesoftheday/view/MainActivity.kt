package com.example.picturesoftheday.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.picturesoftheday.R
import com.example.picturesoftheday.view.picture.FragmentMain
import com.example.picturesoftheday.view.settings.PrefConfing

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when(PrefConfing.load(this)){
            1-> setTheme(R.style.Theme_PicturesOfTheDay)
            2-> setTheme(R.style.Theme_2)
            3-> setTheme(R.style.Theme_3)
            //4-> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        setContentView(R.layout.activity_main)
        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction().replace(R.id.container,FragmentMain.newInstance()).commit()
        }
    }
}