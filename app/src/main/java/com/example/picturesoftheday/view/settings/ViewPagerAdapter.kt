package com.example.picturesoftheday.view.settings

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.picturesoftheday.view.planets.FragmentEarth
import com.example.picturesoftheday.view.planets.FragmentMars
import com.example.picturesoftheday.view.planets.FragmentPlanets
import com.example.picturesoftheday.view.planets.FragmentSolar

private const val EARTH = 0
private const val MARS = 1
private const val SYSTEM = 2

class ViewPagerAdapter(private val fragmentActivity: FragmentPlanets):
    FragmentStateAdapter(fragmentActivity) {
    private val fragments = arrayOf(
        FragmentEarth.newInstance(), FragmentMars.newInstance(),
        FragmentSolar.newInstance())
    override fun getItemCount(): Int {
       return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->fragments[EARTH]
            1->fragments[MARS]
            2->fragments[SYSTEM]
            else ->fragments[EARTH]
        }
    }

}
