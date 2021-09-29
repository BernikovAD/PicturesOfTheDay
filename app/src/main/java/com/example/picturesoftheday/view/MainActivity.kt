package com.example.picturesoftheday.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.picturesoftheday.R
import com.example.picturesoftheday.databinding.ActivityMainBinding
import com.example.picturesoftheday.view.favoritePOD.FragmentFavoritePictures
import com.example.picturesoftheday.view.planets.FragmentEarth
import com.example.picturesoftheday.view.planets.FragmentMars
import com.example.picturesoftheday.view.settings.PrefConfing
import com.example.picturesoftheday.view.settings.SettingsFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when(PrefConfing.load(this)){
            1-> setTheme(R.style.Theme_PicturesOfTheDay)
            2-> setTheme(R.style.Theme_2)
            3-> setTheme(R.style.Theme_3)
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction().replace(R.id.container, FragmentMain.newInstance()).commit()
        }
        binding.bottomAppBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.app_bar_earth -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, FragmentEarth.newInstance()).addToBackStack(null).commit()
                    true
                }
                R.id.app_bar_mars -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, FragmentMars.newInstance()).addToBackStack(null).commit()
                    true
                }
                R.id.app_bar_solar -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, FragmentFavoritePictures.newInstance()).addToBackStack(null).commit()
                    true
                }
                R.id.app_bar_settings -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, SettingsFragment.newInstance()).addToBackStack(null).commit()
                    true
                }
                else -> false
            }
        }

        binding.bottomAppBar.getOrCreateBadge(R.id.app_bar_settings)
    }


}