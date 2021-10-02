package com.example.picturesoftheday.view.favoritePOD

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.picturesoftheday.model.EntityPictures

abstract class BaseViewHolder(view: View): RecyclerView.ViewHolder(view) {
    abstract fun bind(pair: Pair<EntityPictures,Boolean>)
}