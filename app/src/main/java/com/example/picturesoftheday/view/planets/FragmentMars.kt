package com.example.picturesoftheday.view.planets

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.picturesoftheday.R
import com.example.picturesoftheday.databinding.FragmentMarsStartBinding
import com.example.picturesoftheday.viewmodel.AppState
import com.example.picturesoftheday.viewmodel.PODViewModel
import com.google.android.material.snackbar.Snackbar
import java.io.File

class FragmentMars : Fragment() {
    private var _binding: FragmentMarsStartBinding? = null
    private val binding: FragmentMarsStartBinding
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
        _binding = FragmentMarsStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, { render(it) })
        viewModel.getMarsPicture()
        animationLogo()
    }

    private fun animationLogo() {
        val set = AnimatorSet()
        val animationLogoOne = ObjectAnimator.ofFloat(binding.logoMarsTwo,"rotation", 0f,360f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        val animationLogoTwo = ObjectAnimator.ofFloat(binding.logoMarsThree,"rotation", 0f,360f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        val animationLogoThree = ObjectAnimator.ofFloat(binding.logoMarsFour,"rotation", 0f,360f).apply {
            duration = 7000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        set.playTogether(arrayOf(30f, -30f).map { translation ->
            ObjectAnimator.ofFloat(binding.logoMarsOne, "translationY", translation).apply {
                duration = 500
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }
        })
        set.play(animationLogoOne).with(animationLogoTwo).with(animationLogoThree)
        set.start()
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
                binding.imgMars.load(appState.marsServerResponseData.photos.first().imgSrc) {
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