package com.example.picturesoftheday.view.settings


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.example.picturesoftheday.R
import com.example.picturesoftheday.databinding.FragmentSettingsBinding
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater)

        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.includeChips.chipGroup.setOnCheckedChangeListener { _, position ->
            Toast.makeText(context, "Click $position ", Toast.LENGTH_SHORT).show()
        }
        binding.includeChips.chipWithClose.setOnCloseIconClickListener {
            Toast.makeText(context, "Click on chipWithClose", Toast.LENGTH_SHORT).show()
        }
        binding.theme2.setOnClickListener {

            PrefConfing.save(requireContext(), 2)
            requireActivity().recreate()
        }
        binding.theme3.setOnClickListener {
            PrefConfing.save(requireContext(), 3)
            requireActivity().recreate()
        }
        binding.theme1.setOnClickListener {
            PrefConfing.save(requireContext(), 1)
            requireActivity().recreate()
        }
/*        binding.bnt1.setOnClickListener{
            PrefConfing.save(requireContext(), 4)
            requireActivity().recreate()
        }*/
        //Если присвоить ID то все ломается
        /*     binding.tabsSnake.getTabAt(0)?.apply {
                 text = "Работает"
             }*/
        val durationMs = 100L
        binding.apply {
            bnt1.alpha = 0f
            bnt2.alpha = 0f
            bnt3.alpha = 0f
            theme1.alpha = 0f
            theme2.alpha = 0f
            theme3.alpha = 0f
            tabsSnake.alpha = 0f
            bottomNavigationView.alpha = 0f
            includeChips.chipGroup.alpha = 0f
            includeChips.chipWithClose.alpha = 0f
            includeChips.chipDefoult.alpha = 0f
            includeChips.chipChoice.alpha = 0f
            includeChips.chipAction.alpha = 0f
            includeChips.chipFilter.alpha = 0f
            includeChips.chipBackgraund.alpha = 0f
            includeChips.chipRipple.alpha = 0f
            includeChips.chipIcon.alpha = 0f
            cardView.alpha = 0f
        }

        fadeIn(binding.includeChips.chipDefoult, durationMs)
            .andThen(fadeIn(binding.includeChips.chipWithClose, durationMs))
            .andThen(fadeIn(binding.includeChips.chipChoice, durationMs))
            .andThen(fadeIn(binding.includeChips.chipAction, durationMs))
            .andThen(fadeIn(binding.includeChips.chipFilter, durationMs))
            .andThen(fadeIn(binding.includeChips.chipBackgraund, durationMs))
            .andThen(fadeIn(binding.includeChips.chipRipple, durationMs))
            .andThen(fadeIn(binding.includeChips.chipIcon, durationMs))
            .andThen(fadeIn(binding.includeChips.chipGroup, durationMs))
            .andThen(fadeIn(binding.tabsSnake, durationMs))
            .andThen(fadeIn(binding.theme1, durationMs))
            .andThen(fadeIn(binding.theme2, durationMs))
            .andThen(fadeIn(binding.theme3, durationMs))
            .andThen(fadeIn(binding.bnt1, durationMs))
            .andThen(fadeIn(binding.bnt2, durationMs))
            .andThen(fadeIn(binding.bnt3, durationMs))
            .andThen(fadeIn(binding.cardView, durationMs))
            .andThen(fadeIn(binding.bottomNavigationView, durationMs))
            .subscribe()
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.theme_1 -> {
                    PrefConfing.save(requireContext(), 1)
                    requireActivity().recreate()
                }
                R.id.theme_2 -> {
                    PrefConfing.save(requireContext(), 2)
                    requireActivity().recreate()
                }
                R.id.theme_3 -> {
                    PrefConfing.save(requireContext(), 3)
                    requireActivity().recreate()
                }
            }
            true
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

    companion object {
        fun newInstance() = SettingsFragment()
    }

}




