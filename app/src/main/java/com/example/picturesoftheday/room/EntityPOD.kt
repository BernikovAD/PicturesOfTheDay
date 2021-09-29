package com.example.picturesoftheday.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityPOD(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val date: String = "",
    val url: String = ""
)