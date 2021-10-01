package com.example.picturesoftheday.view.favoritePOD

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.picturesoftheday.databinding.FragmentFavoritePicturesBinding
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


}