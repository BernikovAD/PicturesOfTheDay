package com.example.picturesoftheday.view.planets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.picturesoftheday.databinding.FragmentPlanetsBinding
import com.example.picturesoftheday.view.settings.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.math.abs
import kotlin.math.max


class FragmentPlanets : Fragment() {
    private var _binding: FragmentPlanetsBinding? = null
    private val binding: FragmentPlanetsBinding
        get() = _binding!!

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlanetsBinding.inflate(inflater, container, false)
        binding.viewPager.adapter = ViewPagerAdapter(this)
        binding.viewPager.setPageTransformer { _, position ->
            view?.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> {
                        alpha = 0f
                    }
                    position <= 1 -> {
                        val scaleFactor = max(MIN_SCALE, 1 - abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }
                        scaleX = scaleFactor
                        scaleY = scaleFactor
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> {
                        alpha = 0f
                    }
                }
            }

        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Earth"
                1 -> tab.text = "Mars"
                2 -> tab.text = "System"
            }
        }.attach()
        return binding.root

    }

    companion object {
        private const val MIN_SCALE = 0.85f
        private const val MIN_ALPHA = 0.5f
        fun newInstance() = FragmentPlanets()

    }

}

