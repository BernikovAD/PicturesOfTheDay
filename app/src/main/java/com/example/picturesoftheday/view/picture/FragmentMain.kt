package com.example.picturesoftheday.view.picture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.picturesoftheday.R
import com.example.picturesoftheday.databinding.FragmentMainBinding
import com.example.picturesoftheday.view.BottomNavigationDrawerFragment
import com.example.picturesoftheday.view.settings.PrefConfing
import com.example.picturesoftheday.view.settings.SettingsFragment
import com.example.picturesoftheday.viewmodel.AppState
import com.example.picturesoftheday.viewmodel.PODViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.*


class FragmentMain : Fragment() {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!
    private var isLike = false
    private val copy = '©'
    private var isHD = false
    private val viewModel: PODViewModel by lazy {
        ViewModelProvider(this).get(PODViewModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = FragmentMain()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getPOD()
        binding.inputLayout.setEndIconOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://ru.m.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            }
            startActivity(intent)
        }
        bottomSheetBehavior = BottomSheetBehavior.from(binding.includeLayout.bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        val c = Calendar.getInstance()
        binding.prevImg.setOnClickListener {
            c.add(Calendar.DATE, -1)
            Log.i(
                "MyTag",
                "${c.get(Calendar.YEAR)}-0${c.get(Calendar.MONTH) + 1}-${c.get(Calendar.DAY_OF_MONTH)}"
            )
            var date =
                "${c.get(Calendar.YEAR)}-0${c.get(Calendar.MONTH) + 1}-${c.get(Calendar.DAY_OF_MONTH)}"
            viewModel.getPOD(date, date)
        }
        isHD = PrefConfing.loadBol(requireContext())
        binding.chipHDPicture.setOnClickListener {
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
                        "Дата : ${data.serverResponseData[0].date} \n ${data.serverResponseData[0].title} \n $copy ${data.serverResponseData[0].copyright} \n"
                    binding.includeLayout.textDiscriptionPOD.text = text
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
                                Log.i("MyTag", "HD")
                            }
                        }

                        false -> {
                            binding.imageView.load(data.serverResponseData.url) {
                                placeholder(R.drawable.progress_animation)
                                error(R.drawable.ic_load_error_vector)
                                Log.i("MyTag", "noHD")
                            }
                        }
                    }
                    var text =
                        "Дата : ${data.serverResponseData.date} \n ${data.serverResponseData.title} \n $copy ${data.serverResponseData.copyright} \n"
                    binding.includeLayout.textDiscriptionPOD.text = text
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.app_bar_fav -> {
                isLike = if (!isLike) {
                    item.setIcon(R.drawable.ic_baseline_favorite_24)
                    true
                } else {
                    item.setIcon(R.drawable.ic_favourite_menu)
                    false
                }
            }
            R.id.app_bar_settings -> {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance()).addToBackStack("")
                    .commit()
            }
            android.R.id.home -> {
                BottomNavigationDrawerFragment.newInstance()
                    .show(requireActivity().supportFragmentManager, "")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}





