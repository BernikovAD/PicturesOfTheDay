package com.example.picturesoftheday.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.picturesoftheday.databinding.ActivitySplashBinding
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject

class SplashActivity : AppCompatActivity() {
    private lateinit var handler: Handler
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        animations()
        handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 3000)

    }

    private fun animations() {
        val animationsOne = arrayOf(30f, -30f).map { translation ->
            ObjectAnimator.ofFloat(binding.logoSpaceOne, "translationY", translation).apply {
                duration = 500
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }
        }
        val set = AnimatorSet()

        val animationsTwo = arrayOf(30f, -30f).map { translation ->
            ObjectAnimator.ofFloat(binding.logoSpaceOne, "translationX", translation).apply {
                duration = 1000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }
        }
        val animationsThree = arrayOf(30f, -30f).map { translation ->
            ObjectAnimator.ofFloat(binding.logoSpaceThree, "translationY", translation).apply {
                duration = 1000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }
        }
        set.playTogether(animationsOne)
        set.playTogether(animationsTwo)
        set.playTogether(animationsThree)
        set.start()

        binding.apply {
            logoSpaceThree.alpha = 0f
            logoSpaceTwo.alpha = 0f
            appName.alpha = 0f
        }
        fadeIn(binding.logoSpaceThree).andThen(fadeIn(binding.logoSpaceTwo))
            .andThen(fadeIn(binding.appName)).subscribe()
    }

    private fun fadeIn(view: View): Completable {
        val animationSubject = CompletableSubject.create()
        return animationSubject.doOnSubscribe {
            ViewCompat.animate(view)
                .setDuration(1000L)
                .alpha(1f)
                .withEndAction {
                    animationSubject.onComplete()
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}