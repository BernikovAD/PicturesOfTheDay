package com.example.picturesoftheday.view.planets

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.api.load
import com.example.picturesoftheday.R
import com.example.picturesoftheday.databinding.FragmentEarthBinding
import com.example.picturesoftheday.repository.DataPOD
import com.example.picturesoftheday.view.settings.PrefConfing
import com.example.picturesoftheday.viewmodel.AppState
import com.example.picturesoftheday.viewmodel.PODViewModel
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import java.util.*
import kotlin.collections.ArrayList


class FragmentEarth : Fragment() {
    private var isExpanded = false
    private var _binding: FragmentEarthBinding? = null
    private val binding: FragmentEarthBinding
        get() = _binding!!
    private var isHD = false
    private var idPOD: Int = 0
    private lateinit var dataPOD: DataPOD
    val data: MutableList<DataPOD> = ArrayList()
    private val date = Calendar.getInstance()
    private val today = Calendar.getInstance()
    private val videoOfTheDay ="Сегодня у нас без картинки дня, но есть видео дня! Кликни >ЗДЕСЬ< чтобы открыть в новом окне"
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
        animationStart()
        clickIncreaseImagePOD()
        ckickWiki()
        clickPreviousImage()
        clickHDpicture()
        clickNextImage()
        binding.includeEarth.addFavoriteImage.setOnClickListener {
            viewModel.saveDataPODToDB(dataPOD)
        }

    }

    private fun animationStart() {
        val set = AnimatorSet()
        set.playTogether(arrayOf(10f, -10f).map { translation ->
            ObjectAnimator.ofFloat(binding.includeEarth.logoEarth, "translationY", translation)
                .apply {
                    duration = 1000
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.REVERSE
                }
        })
        val animationMoon =
            ObjectAnimator.ofFloat(binding.includeEarth.logoEarthMoon, "rotation", 0f, 1080f)
                .apply {
                    duration = 10000
                    repeatCount = ObjectAnimator.INFINITE
                }
        set.play(animationMoon)
        set.start()
        val durationMs = 500L
        binding.includeEarth.apply {
            logoEarth.alpha = 0f
            discriptionEarth.alpha = 0f
            prevImg.alpha = 0f
            HDPicture.alpha = 0f
            nextPod.alpha = 0f
            inputLayout.alpha = 0f
        }
        fadeIn(binding.includeEarth.logoEarth, durationMs)
            .andThen(fadeIn(binding.includeEarth.discriptionEarth, durationMs))
            .andThen(fadeIn(binding.includeEarth.prevImg, durationMs))
            .andThen(fadeIn(binding.includeEarth.HDPicture, durationMs))
            .andThen(fadeIn(binding.includeEarth.nextPod, durationMs))
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

    private fun clickPreviousImage() {
        binding.includeEarth.prevImg.setOnClickListener {
            date.add(Calendar.DATE, -1)
            var date =
                "${date.get(Calendar.YEAR)}-0${date.get(Calendar.MONTH) + 1}-${date.get(Calendar.DAY_OF_MONTH)}"
            viewModel.getPOD(date, date)
        }
    }

    private fun clickNextImage() {
        binding.includeEarth.nextPod.setOnClickListener {
            if (date != today) {
                date.add(Calendar.DATE, 1)
                var date =
                    "${date.get(Calendar.YEAR)}-0${date.get(Calendar.MONTH) + 1}-${date.get(Calendar.DAY_OF_MONTH)}"
                viewModel.getPOD(date, date)
            }
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
                dataPOD = DataPOD(
                    idPOD++,
                    data.serverResponseData[0].date,
                    data.serverResponseData[0].url
                )
                if (data.serverResponseData[0].mediaType == "video") showAVideoUrl(data.serverResponseData[0].url)
                else {
                    showPhotoUrl(data.serverResponseData[0].url, data.serverResponseData[0].hdurl)
                    var text =
                        "${data.serverResponseData[0].date}\n${data.serverResponseData[0].title}"
                    binding.textDiscriptionPOD.text = text
                }
            }
            is AppState.Success -> {
                dataPOD = DataPOD(idPOD++, data.serverResponseData.date, data.serverResponseData.url)
                if (data.serverResponseData.mediaType == "video") showAVideoUrl(data.serverResponseData.url)
                else {
                    showPhotoUrl(data.serverResponseData.url, data.serverResponseData.hdurl)
                    var text = "${data.serverResponseData.date}\n${data.serverResponseData.title}"
                    binding.textDiscriptionPOD.text = text
                }

            }
        }
    }

    private fun showAVideoUrl(videoUrl: String?) = with(binding) {
        binding.includeEarth.imageViewPOD.visibility = View.GONE
        binding.includeEarth.videoOfTheDay.visibility = View.VISIBLE
        binding.includeEarth.videoOfTheDay.text = videoOfTheDay
        binding.includeEarth.videoOfTheDay.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(videoUrl)
            }
            startActivity(i)
        }
    }

    private fun showPhotoUrl(photoUrl: String?, photoHdurl: String?) {
        binding.includeEarth.imageViewPOD.visibility = View.VISIBLE
        binding.includeEarth.videoOfTheDay.visibility = View.GONE
        binding.includeEarth.imageViewPOD.load(photoUrl) {
            placeholder(R.drawable.progress_animation)
            error(R.drawable.ic_load_error_vector)
            when (PrefConfing.loadBol(requireContext())) {
                true -> {
                    binding.includeEarth.imageViewPOD.load(photoHdurl) {
                        placeholder(R.drawable.progress_animation)
                        error(R.drawable.ic_load_error_vector)
                    }
                }
                false -> {
                    binding.includeEarth.imageViewPOD.load(photoUrl) {
                        placeholder(R.drawable.progress_animation)
                        error(R.drawable.ic_load_error_vector)
                    }
                }
            }

        }
    }
}


