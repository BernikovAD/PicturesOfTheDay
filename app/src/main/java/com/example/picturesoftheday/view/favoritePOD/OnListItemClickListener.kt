package com.example.picturesoftheday.view.favoritePOD

import com.example.picturesoftheday.model.EntityPictures

interface OnListItemClickListener {
    fun onItemClick(entityPictures: EntityPictures)
}