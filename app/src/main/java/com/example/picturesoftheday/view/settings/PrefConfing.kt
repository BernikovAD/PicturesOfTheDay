package com.example.picturesoftheday.view.settings

import android.content.Context
import android.content.SharedPreferences
import java.io.File


object PrefConfing {
    private const val FILE = "Theme_change"
    private const val FILE_HD = "choice_hd"
    private const val KEY = "key_theme"
    private const val KEY_HD = "key_hd"
    fun save(context: Context, total: Int) {
        val pref: SharedPreferences = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(KEY, total)
        editor.apply()
    }
    fun save(context: Context, total: Boolean) {
        val pref: SharedPreferences = context.getSharedPreferences(FILE_HD, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(KEY_HD, total)
        editor.apply()
    }
    fun loadBol(context: Context): Boolean {
        val pref: SharedPreferences = context.getSharedPreferences(FILE_HD, Context.MODE_PRIVATE)
        return pref.getBoolean(KEY_HD,false)
    }
    fun load(context: Context): Int {
        val pref: SharedPreferences = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
        return pref.getInt(KEY, 0)
    }
}