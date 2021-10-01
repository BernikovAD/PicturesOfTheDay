package com.example.picturesoftheday.view.favoritePOD

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.picturesoftheday.R
import com.example.picturesoftheday.databinding.FragmentFavoritePicturesBinding
import com.example.picturesoftheday.model.EntityPictures
import com.example.picturesoftheday.viewmodel.PicturesViewModel

class FragmentFavoritePictures : Fragment() {
    private var _binding: FragmentFavoritePicturesBinding? = null
    private val binding: FragmentFavoritePicturesBinding
        get() = _binding!!

    companion object {
        fun newInstance() = FragmentFavoritePictures()
    }

    private val picturesViewModel: PicturesViewModel by lazy {
        ViewModelProvider(this).get(PicturesViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritePicturesBinding.inflate(inflater, container, false)
        val adapter = ListAdapter()
        val recyclerView = binding.recyclerview
        recyclerView.adapter = adapter
        picturesViewModel.readAllData.observe(viewLifecycleOwner, Observer { list ->
            adapter.setData(list)
        })
        return binding.root
    }

    inner class ListAdapter() : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
        private var picturesList: MutableList<EntityPictures> = ArrayList()

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

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
            holder.itemView.findViewById<AppCompatImageView>(R.id.removeItem).setOnClickListener {
                removeItem(position)
                Toast.makeText(
                    requireContext(),
                    "Successfully remove: ${currentItem.date}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        private fun removeItem(position: Int) {
            picturesViewModel.deleteUser(picturesList[position])
            picturesList.removeAt(position)
            notifyItemRemoved(position)
        }

        fun setData(entityPictures: List<EntityPictures>) {
            this.picturesList = entityPictures as MutableList<EntityPictures>
            notifyDataSetChanged()
        }
    }

}
