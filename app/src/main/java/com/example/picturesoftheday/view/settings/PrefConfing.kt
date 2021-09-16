package com.example.picturesoftheday.view.settings

import android.content.Context
import android.content.SharedPreferences


    object PrefConfing {
        private const val FILE = "Theme_change"
        private const val KEY = "key_theme"
        fun save(context: Context, total: Int) {
            val pref: SharedPreferences = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            val editor = pref.edit()
            editor.putInt(KEY, total)
            editor.apply()
        }

        fun load(context: Context): Int {
            val pref: SharedPreferences = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            return pref.getInt(KEY, 0)
        }
    }