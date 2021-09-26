package com.example.picturesoftheday.view.planets

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.*
import coil.api.load
import com.example.picturesoftheday.R
import com.example.picturesoftheday.databinding.FragmentEarthBinding
import com.example.picturesoftheday.utils.CustomBehavior
import com.example.picturesoftheday.view.settings.PrefConfing
import com.example.picturesoftheday.viewmodel.AppState
import com.example.picturesoftheday.viewmodel.PODViewModel
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import java.util.*


class FragmentEarth : Fragment() {
    private var isExpanded = false
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
        if (isHD) binding.includeEarth.HDPicture.setImageResource(R.drawable.ic_hd)
        else binding.includeEarth.HDPicture.setImageResource(R.drawable.ic_no_hd)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getPOD()
        //animationStart()
        clickIncreaseImagePOD()
        ckickWiki()
        clickPreviusImage()
        clickHDpicture()
        val set = AnimatorSet()
        set.playTogether(arrayOf(30f, -30f).map { translation ->
            ObjectAnimator.ofFloat(binding.includeEarth.logoEarth, "translationY", translation).apply {
                duration = 500
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }
        })
        set.start()
    }

    private fun animationStart() {
        val durationMs = 500L
        binding.includeEarth.apply {
            logoEarth.alpha = 0f
            discriptionEarth.alpha = 0f
            prevImg.alpha = 0f
            HDPicture.alpha = 0f
            infoPod.alpha = 0f
            inputLayout.alpha = 0f
        }
        fadeIn(binding.includeEarth.logoEarth, durationMs)
            .andThen(fadeIn(binding.includeEarth.discriptionEarth, durationMs))
            .andThen(fadeIn(binding.includeEarth.prevImg, durationMs))
            .andThen(fadeIn(binding.includeEarth.HDPicture, durationMs))
            .andThen(fadeIn(binding.includeEarth.infoPod, durationMs))
            .andThen(fadeIn(binding.includeEarth.inputLayout, durationMs))
            .subscribe()
    }

    private fun clickIncreaseImagePOD() {
        binding.includeEarth.imageViewPOD.setOnClickListener {
            isExpanded = !isExpanded

            val set = TransitionSet()
                .addTransition(ChangeBounds())
                .addTransition(ChangeImageTransform())

            TransitionManager.beginDelayedTransition(binding.includeEarth.scrollPOD, set)
            binding.includeEarth.imageViewPOD.scaleType = if (isExpanded) {
                ImageView.ScaleType.CENTER_CROP
            } else {
                ImageView.ScaleType.FIT_CENTER
            }
        }
    }

    fun fadeIn(view: View, duration: Long): Completable {
        val animationSubject = CompletableSubject.create()
        return animationSubject.doOnSubscribe {
            ViewCompat.animate(view)
                .setDuration(duration)
                .alpha(1f)
                .withEndAction {
                    animationSubject.onComplete()
                }
        }
    }
    private fun ckickWiki() {
        binding.includeEarth.inputLayout.setEndIconOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://ru.m.wikipedia.org/wiki/${binding.includeEarth.inputEditText.text.toString()}")
            }
            startActivity(intent)
        }
    }

    private fun clickPreviusImage() {
        val c = Calendar.getInstance()
        binding.includeEarth.prevImg.setOnClickListener {
            c.add(Calendar.DATE, -1)
            var date =
                "${c.get(Calendar.YEAR)}-0${c.get(Calendar.MONTH) + 1}-${c.get(Calendar.DAY_OF_MONTH)}"
            viewModel.getPOD(date, date)
        }
    }

    private fun clickHDpicture() {
        binding.includeEarth.HDPicture.setOnClickListener {
            if (isHD) {
                binding.includeEarth.HDPicture.setImageResource(R.drawable.ic_hd)
                viewModel.getPOD()
            } else {
                binding.includeEarth.HDPicture.setImageResource(R.drawable.ic_no_hd)
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

                binding.includeEarth.imageViewPOD.load(R.drawable.progress_animation) {
                    error(R.drawable.ic_load_error_vector)
                }
            }
            is AppState.SuccessPODDate -> {

                binding.includeEarth.imageViewPOD.load(data.serverResponseData[0].url) {
                    placeholder(R.drawable.progress_animation)
                    error(R.drawable.ic_load_error_vector)
                    when (PrefConfing.loadBol(requireContext())) {
                        true -> {
                            binding.includeEarth.imageViewPOD.load(data.serverResponseData[0].hdurl) {
                                placeholder(R.drawable.progress_animation)
                                error(R.drawable.ic_load_error_vector)
                            }
                        }
                        false -> {
                            binding.includeEarth.imageViewPOD.load(data.serverResponseData[0].url) {
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
                binding.includeEarth.imageViewPOD.load(data.serverResponseData.url) {
                    placeholder(R.drawable.progress_animation)
                    error(R.drawable.ic_load_error_vector)
                    when (PrefConfing.loadBol(requireContext())) {
                        true -> {
                            binding.includeEarth.imageViewPOD.load(data.serverResponseData.hdurl) {
                                placeholder(R.drawable.progress_animation)
                                error(R.drawable.ic_load_error_vector)
                            }
                        }

                        false -> {
                            binding.includeEarth.imageViewPOD.load(data.serverResponseData.url) {
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


