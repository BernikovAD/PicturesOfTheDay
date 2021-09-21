package com.example.picturesoftheday.view.settings


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        val durationMs = 500L
        binding.apply {
            theme1.alpha = 0f
            theme2.alpha = 0f
            theme3.alpha = 0f
        }

        fadeIn(binding.theme1, durationMs)
            .andThen(fadeIn(binding.theme2, durationMs))
            .andThen(fadeIn(binding.theme3, durationMs))
            .subscribe()
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




