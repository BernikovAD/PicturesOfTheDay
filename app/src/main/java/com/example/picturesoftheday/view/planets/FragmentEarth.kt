package com.example.picturesoftheday.view.planets

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.picturesoftheday.R
import com.example.picturesoftheday.databinding.FragmentEarthBinding
import com.example.picturesoftheday.utils.CustomBehavior
import com.example.picturesoftheday.view.settings.PrefConfing
import com.example.picturesoftheday.viewmodel.AppState
import com.example.picturesoftheday.viewmodel.PODViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import java.util.*


class FragmentEarth : Fragment() {

    private var _binding: FragmentEarthBinding? = null
    private val binding: FragmentEarthBinding
        get() = _binding!!
    private var isHD = false
    private val viewModel: PODViewModel by lazy {
        ViewModelProvider(this).get(PODViewModel::class.java)
    }

    companion object {
        fun newInstance() = FragmentEarth()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEarthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isHD = PrefConfing.loadBol(requireContext())
        if (isHD) binding.HDPicture.setImageResource(R.drawable.ic_hd)
        else binding.HDPicture.setImageResource(R.drawable.ic_no_hd)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getPOD()
        val c = Calendar.getInstance()

        binding.inputLayout.setEndIconOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://ru.m.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            }
            startActivity(intent)
        }


        binding.prevImg.setOnClickListener {
            c.add(Calendar.DATE, -1)
            var date =
                "${c.get(Calendar.YEAR)}-0${c.get(Calendar.MONTH) + 1}-${c.get(Calendar.DAY_OF_MONTH)}"
            viewModel.getPOD(date, date)
        }

        binding.HDPicture.setOnClickListener {
            if (isHD) {
                binding.HDPicture.setImageResource(R.drawable.ic_hd)
                viewModel.getPOD()
            } else {
                binding.HDPicture.setImageResource(R.drawable.ic_no_hd)
                viewModel.getPOD()
            }
            isHD = !isHD
            PrefConfing.save(requireContext(), isHD)
        }


    }

    private fun renderData(data: AppState) {
        when (data) {
            is AppState.Error -> {
                Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
            }
            is AppState.Loading -> {
                binding.imageView.load(R.drawable.progress_animation) {
                    error(R.drawable.ic_load_error_vector)
                }
            }
            is AppState.SuccessPODDate -> {
                binding.imageView.load(data.serverResponseData[0].url) {
                    placeholder(R.drawable.progress_animation)
                    error(R.drawable.ic_load_error_vector)
                    when (PrefConfing.loadBol(requireContext())) {
                        true -> {
                            binding.imageView.load(data.serverResponseData[0].hdurl) {
                                placeholder(R.drawable.progress_animation)
                                error(R.drawable.ic_load_error_vector)
                            }
                        }

                        false -> {
                            binding.imageView.load(data.serverResponseData[0].url) {
                                placeholder(R.drawable.progress_animation)
                                error(R.drawable.ic_load_error_vector)
                            }
                        }
                    }
                    var text =
                        "${data.serverResponseData[0].date}\n${data.serverResponseData[0].title}"
                    binding.textDiscriptionPOD.text = text
                }
            }
            is AppState.Success -> {
                binding.imageView.load(data.serverResponseData.url) {
                    placeholder(R.drawable.progress_animation)
                    error(R.drawable.ic_load_error_vector)
                    when (PrefConfing.loadBol(requireContext())) {
                        true -> {
                            binding.imageView.load(data.serverResponseData.hdurl) {
                                placeholder(R.drawable.progress_animation)
                                error(R.drawable.ic_load_error_vector)
                            }
                        }

                        false -> {
                            binding.imageView.load(data.serverResponseData.url) {
                                placeholder(R.drawable.progress_animation)
                                error(R.drawable.ic_load_error_vector)
                            }
                        }
                    }
                    var text = "${data.serverResponseData.date}\n${data.serverResponseData.title}"
                    binding.textDiscriptionPOD.text = text
                }
            }
        }
    }
}


