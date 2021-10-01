package com.example.picturesoftheday.view.favoritePOD

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.picturesoftheday.R
import com.example.picturesoftheday.model.EntityPictures
import com.example.picturesoftheday.view.planets.FragmentMars


class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var picturesList = emptyList<EntityPictures>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun getItemCount(): Int {
        return picturesList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.custom_row_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = picturesList[position]
        holder.itemView.findViewById<TextView>(R.id.id_txt).text = currentItem.id.toString()
        holder.itemView.findViewById<TextView>(R.id.text_date_picture_of_the_day).text =
            currentItem.date
        holder.itemView.findViewById<ConstraintLayout>(R.id.constraint_item).setOnClickListener {
            //TODO вызывается PicturesViewModel.deletePictures(сюда тот элемент который удалить)
        }
    }

    fun setData(entityPictures: List<EntityPictures>) {
        this.picturesList = entityPictures
        notifyDataSetChanged()
    }
}