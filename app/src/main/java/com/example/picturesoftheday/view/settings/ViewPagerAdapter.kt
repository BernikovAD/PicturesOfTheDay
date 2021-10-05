package com.example.picturesoftheday.view.settings

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.picturesoftheday.view.favoritePOD.FragmentFavoritePictures
import com.example.picturesoftheday.view.main.FragmentMain
import com.example.picturesoftheday.view.planets.FragmentEarth
import com.example.picturesoftheday.view.planets.FragmentMars
import com.example.picturesoftheday.view.planets.FragmentSolar

private const val EARTH = 0
private const val MARS = 1
private const val SYSTEM = 2
private const val FAVORITE = 3
private const val SETTINGS = 4

class ViewPagerAdapter(private val fragmentActivity: FragmentMain) :
    FragmentStateAdapter(fragmentActivity) {
    private val fragments = arrayOf(
        FragmentEarth.newInstance(),
        FragmentMars.newInstance(),
        FragmentSolar.newInstance(),
        FragmentFavoritePictures.newInstance(),
        SettingsFragment.newInstance()
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> fragments[EARTH]
            1 -> fragments[MARS]
            2 -> fragments[SYSTEM]
            3 -> fragments[FAVORITE]
            4 -> fragments[SETTINGS]
            else -> fragments[EARTH]
        }
    }

}
