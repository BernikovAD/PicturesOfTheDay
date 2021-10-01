package com.example.picturesoftheday.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "POD_table")
data class EntityPictures(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val date: String = "",
    val url: String = ""
)