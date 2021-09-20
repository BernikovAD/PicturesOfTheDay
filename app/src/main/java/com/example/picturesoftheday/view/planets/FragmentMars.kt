package com.example.picturesoftheday.view.planets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.picturesoftheday.R
import com.example.picturesoftheday.databinding.FragmentMarsBinding
import com.example.picturesoftheday.viewmodel.AppState
import com.example.picturesoftheday.viewmodel.PODViewModel
import com.google.android.material.snackbar.Snackbar

class FragmentMars : Fragment() {
    private var _binding: FragmentMarsBinding? = null
    private val binding: FragmentMarsBinding
        get() = _binding!!
    private val viewModel: PODViewModel by lazy {
        ViewModelProvider(this).get(PODViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, { render(it) })
        viewModel.getMarsPicture()
    }


    private fun render(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
                Snackbar.make(binding.root, appState.error.toString(), Snackbar.LENGTH_SHORT).show()
            }
            is AppState.Loading -> {
                binding.imgMars.load(R.drawable.progress_animation) {
                    error(R.drawable.ic_load_error_vector)
                }
            }
            is AppState.SuccessMars -> {
                binding.imgMars.load(appState.marsServerResponseData[0].imgSrc) {
                    placeholder(R.drawable.progress_animation)
                    error(R.drawable.ic_load_error_vector)
                }
            }
        }
    }

    companion object {
        fun newInstance() = FragmentMars()
    }
}