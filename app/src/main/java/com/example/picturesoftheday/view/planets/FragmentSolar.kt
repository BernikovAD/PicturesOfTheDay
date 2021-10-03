package com.example.picturesoftheday.view.planets

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.picturesoftheday.databinding.FragmentSolarStartBinding
import com.example.picturesoftheday.viewmodel.AppState
import com.example.picturesoftheday.viewmodel.PODViewModel
import com.google.android.material.snackbar.Snackbar

class FragmentSolar : Fragment() {
    private var _binding: FragmentSolarStartBinding? = null
    private val binding: FragmentSolarStartBinding
        get() = _binding!!
    private var startDate = ""
    private var endDate = ""
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
        _binding = FragmentSolarStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, { render(it) })
        var text="Solar flares from $startDate to $endDate"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.datePickerStart.setOnDateChangedListener { _, i, i2, i3 ->
                startDate = "$i-0${i2 + 1}-$i3"
            }
            binding.datePickerEnd.setOnDateChangedListener { _, i, i2, i3 ->
                endDate = "$i-0${(i2 + 1)}-$i3"
            }
        }
        binding.buttonDate.setOnClickListener {
            viewModel.getSolarFlare(startDate, endDate)
        }
        binding.buttonToday.setOnClickListener {
            viewModel.getSolarFlare(viewModel.getDate())
        }


        binding.discription.text = text
        val set = AnimatorSet()
        set.playTogether(arrayOf(30f, -30f).map { translation ->
            ObjectAnimator.ofFloat(binding.logoSolar, "translationY", translation).apply {
                duration = 500
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }
        })
        set.start()
    }

    private fun render(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
                Snackbar.make(binding.root, appState.error.toString(), Snackbar.LENGTH_SHORT).show()
            }
            is AppState.Loading -> {
            }
            is AppState.SuccessWeather -> {
                var informationFromSolar: String = ""
                if (appState.solarFlareResponseData.size == 0) {
                    binding.discription.text = "There were no flares in the sun on this date"
                }
                for (i in appState.solarFlareResponseData.indices) {
                    informationFromSolar += "\n$i) ${appState.solarFlareResponseData[i].flrID}"
                }
                binding.contentText.text = informationFromSolar
            }
        }
    }

    companion object {
        fun newInstance() = FragmentSolar()
    }


}