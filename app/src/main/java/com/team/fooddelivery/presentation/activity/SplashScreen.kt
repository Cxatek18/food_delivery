package com.team.fooddelivery.presentation.activity

import android.animation.Animator
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieDrawable
import com.team.fooddelivery.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startSplashScreen()
    }

    private fun setSettingsAnimation() {
        with(binding) {
            lottieViewSplashScreen.repeatCount = 0
            lottieViewSplashScreen.repeatMode = LottieDrawable.RESTART
            lottieViewSplashScreen.playAnimation()
        }
    }

    private fun startSplashScreen() {
        setSettingsAnimation()
        binding.lottieViewSplashScreen.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                val intent: Intent = MainActivity.newInstance(
                    context = this@SplashScreen
                )
                val enterAnimation = com.google.android.material
                    .R.anim.m3_side_sheet_enter_from_right
                val exitAnimation = androidx.appcompat.R.anim.abc_fade_out
                val activityOptions = ActivityOptions.makeCustomAnimation(
                    applicationContext, enterAnimation, exitAnimation
                )
                startActivity(intent, activityOptions.toBundle())
                finish()
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
    }
}