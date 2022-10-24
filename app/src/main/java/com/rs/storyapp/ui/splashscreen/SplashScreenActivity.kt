package com.rs.storyapp.ui.splashscreen

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.rs.storyapp.common.util.goto
import com.rs.storyapp.common.util.gotoWithToken
import com.rs.storyapp.databinding.ActivitySplashScreenBinding
import com.rs.storyapp.ui.liststory.ListStoryActivity
import com.rs.storyapp.ui.login.LoginActivity
import com.rs.storyapp.viewmodels.SplashScreenViewModel
import com.rs.storyapp.viewmodels.ViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashScreenBinding.inflate(layoutInflater) }

    private val splashScreenViewModel: SplashScreenViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        playAnimation()

        val delaySplashScreen = 2000L
        Handler(Looper.getMainLooper()).postDelayed({
            splashScreenViewModel.getToken().observe(this) { token ->
                if (token == "") {
                    goto(LoginActivity::class.java)

                } else {
                    gotoWithToken(ListStoryActivity::class.java, token)
                }
                finish()
            }
        }, delaySplashScreen)
    }

    private fun playAnimation() {
        val icon =
            ObjectAnimator.ofFloat(binding.icon, View.ALPHA, 1f).setDuration(500)
        val storyApp =
            ObjectAnimator.ofFloat(binding.storyApp, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                icon,
                storyApp,
            )
            startDelay = 500
        }.start()
    }
}