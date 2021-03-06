package com.example.picturesoftheday.view.planets

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.text.toSpannable
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
import com.example.picturesoftheday.model.EntityPictures
import com.example.picturesoftheday.settings.PrefConfing
import com.example.picturesoftheday.viewmodel.AppState
import com.example.picturesoftheday.viewmodel.PODViewModel
import com.example.picturesoftheday.viewmodel.PicturesViewModel
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import java.util.*
import android.text.SpannableString





class FragmentEarth : Fragment() {
    private var isExpanded = false
    private var _binding: FragmentEarthBinding? = null
    private val binding: FragmentEarthBinding
        get() = _binding!!
    private var isHD = false
    private var urlPictures: String = ""
    private var datePictures: String = ""
    private val date = Calendar.getInstance()
    private val today = Calendar.getInstance()
    private val videoOfTheDay =
        ("Сегодня у нас без картинки дня, но есть видео дня! Кликни >ЗДЕСЬ< чтобы открыть в новом окне").toSpannable()
    private val viewModel: PODViewModel by lazy {
        ViewModelProvider(this).get(PODViewModel::class.java)
    }
    private val picturesViewModel: PicturesViewModel by lazy {
        ViewModelProvider(this).get(PicturesViewModel::class.java)
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
        if (!isHD) binding.includeEarth.HDPicture.setImageResource(R.drawable.ic_no_hd)
        else binding.includeEarth.HDPicture.setImageResource(R.drawable.ic_hd)

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getPOD()
        animationStart()
        clickIncreaseImagePOD()
        clickWiki()
        clickPreviousImage()
        clickHDpicture()
        clickNextImage()
        binding.includeEarth.addFavoriteImage.setOnClickListener {

            if(checkDuplicates(datePictures)) insertDataToDatabase(datePictures, urlPictures)
        }

    }
    private var picturesList = emptyList<EntityPictures>()
    private fun checkDuplicates(datePictures: String):Boolean {
        picturesViewModel.readAllData.observe(viewLifecycleOwner, Observer { list ->
            setData(list)
        })
        picturesList.forEach{
            if(it.date.equals(datePictures)) return false
        }
        return true
    }
    private fun setData(entityPictures: List<EntityPictures>) {
        this.picturesList = entityPictures
    }
    private fun insertDataToDatabase(datePictures: String, urlPictures: String) {
        val entityPictures = EntityPictures(0, datePictures, urlPictures)
        picturesViewModel.addPictures(entityPictures)
        Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_SHORT).show()
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

    private fun clickWiki() {
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
                if (isEmptyData(data.serverResponseData[0].date, data.serverResponseData[0].url)) {
                    datePictures = data.serverResponseData[0].date!!
                    urlPictures = data.serverResponseData[0].url!!
                }

                if (data.serverResponseData[0].mediaType == "video") showAVideoUrl(data.serverResponseData[0].url)
                else {
                    showPhotoUrl(data.serverResponseData[0].url, data.serverResponseData[0].hdurl)
                    var text =
                        "${data.serverResponseData[0].date}\n${data.serverResponseData[0].title}"
                    binding.textDiscriptionPOD.text = text
                }
            }
            is AppState.Success -> {
                if (isEmptyData(data.serverResponseData.date, data.serverResponseData.url)) {
                    datePictures = data.serverResponseData.date!!
                    urlPictures = data.serverResponseData.url!!
                }
                if (data.serverResponseData.mediaType == "video") showAVideoUrl(data.serverResponseData.url)
                else {
                    showPhotoUrl(data.serverResponseData.url, data.serverResponseData.hdurl)
                    var text = "${data.serverResponseData.date}\n${data.serverResponseData.title}"
                    val spannable = SpannableStringBuilder(text)
                    spannable.setSpan(ForegroundColorSpan(resources.getColor(R.color.yelow)),0,
                        10,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.textDiscriptionPOD.text = spannable

                }

            }
        }
    }

    private fun isEmptyData(date: String?, url: String?): Boolean {
        return (date != null) && (url != null)
    }
    private fun showAVideoUrl(videoUrl: String?) = with(binding) {
        binding.includeEarth.imageViewPOD.visibility = View.GONE
        binding.includeEarth.videoOfTheDay.visibility = View.VISIBLE
        val spannable: Spannable = SpannableString(videoOfTheDay)
        spannable.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                Toast.makeText(requireContext(),"Click youtube.com",Toast.LENGTH_SHORT).show()
            } },58, 65,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.includeEarth.videoOfTheDay.text = spannable
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


