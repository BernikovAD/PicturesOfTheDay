package com.example.picturesoftheday.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.picturesoftheday.R
import com.example.picturesoftheday.databinding.BottomNavigationLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomNavigationDrawerFragment: BottomSheetDialogFragment() {
    private var _binding: BottomNavigationLayoutBinding? = null
    val binding: BottomNavigationLayoutBinding
        get() = _binding!!

    companion object {
        fun newInstance() = BottomNavigationDrawerFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomNavigationLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navigationView.setNavigationItemSelectedListener { it ->
            when (it.itemId) {
                R.id.app_bar_fav ->{
                    Toast.makeText(context,"Like", Toast.LENGTH_SHORT).show()
                }
                R.id.app_bar_settings ->{
                    Toast.makeText(context,"Настройки", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }
}
