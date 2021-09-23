package com.example.picturesoftheday.view.planets

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
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
        var startDate = ""
        var endDate = ""
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
    }

    private fun render(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
                Snackbar.make(binding.root, appState.error.toString(), Snackbar.LENGTH_SHORT).show()
            }
            is AppState.Loading -> {
            }
            is AppState.SuccessWeather -> {
                var info: String = ""
                if (appState.solarFlareResponseData.size == 0) {
                    info = "No solar flares have been detected today"
                }
                for (i in appState.solarFlareResponseData.indices) {
                    info += "\n${appState.solarFlareResponseData[i].flrID}"
                }
                binding.contentText.text = info
            }
        }
    }

    companion object {
        fun newInstance() = FragmentSolar()
    }


}