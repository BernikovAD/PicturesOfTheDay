package com.example.picturesoftheday.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.picturesoftheday.databinding.FragmentMainBinding
import com.example.picturesoftheday.view.settings.ViewPagerAdapter
import kotlin.math.abs


class FragmentMain : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
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
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.viewPager.adapter = ViewPagerAdapter(this)
        binding.viewPager.setPageTransformer { page, position ->
            view?.apply {
                page.translationX = -position * page.width;
                page.cameraDistance = 12000F;
                if (position < 0.5 && position > -0.5) {
                    page.visibility = View.VISIBLE;
                } else {
                    page.visibility = View.INVISIBLE;
                }
                when {
                    position < -1 -> {
                        page.alpha = 0F;
                    }
                    position <= 0 -> {
                        page.alpha = 1F;
                        page.rotationY = 180 * (1 - abs(position) + 1);
                    }
                    position <= 1 -> {
                        page.alpha = 1F;
                        page.rotationY = -180 * (1 - abs(position) + 1);
                    }
                    else -> {
                        page.alpha = 0F;
                    }
                }
            }
        }
        return binding.root
    }

    companion object {
        fun newInstance() = FragmentMain()
    }
}

