package com.example.picturesoftheday.view.planets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.picturesoftheday.databinding.FragmentPlanetsBinding
import com.example.picturesoftheday.view.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class FragmentPlanets : Fragment() {
    private var _binding:FragmentPlanetsBinding? = null
    private val binding:FragmentPlanetsBinding
        get()=_binding!!

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
        _binding = FragmentPlanetsBinding.inflate(inflater,container,false)
        binding.viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position){
                0->  tab.text = "Earth"
                1->  tab.text = "Mars"
                2-> tab.text = "System"
            }
        }.attach()
        return binding.root

    }

    companion object {

        fun newInstance() = FragmentPlanets()

    }
}