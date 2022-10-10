package com.rs.storyapp.ui.splashscreen

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.rs.storyapp.common.util.goto
import com.rs.storyapp.data.local.DataUserPreference
import com.rs.storyapp.databinding.ActivitySplashScreenBinding
import com.rs.storyapp.ui.liststory.ListStoryActivity
import com.rs.storyapp.ui.login.LoginActivity
import com.rs.storyapp.ui.login.dataStore
import com.rs.storyapp.viewmodels.SplashScreenViewModel
import com.rs.storyapp.viewmodels.SplashScreenViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySplashScreenBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        playAnimation()

        val pref = DataUserPreference.getInstance(dataStore)
        val splashScreenViewModel = ViewModelProvider(
            this,
            SplashScreenViewModelFactory(pref)
        )[SplashScreenViewModel::class.java]

        val delaySplashScreen = 2000L
        Handler(Looper.getMainLooper()).postDelayed({
            splashScreenViewModel.getIsLogin().observe(this) { isLogin ->
                if (isLogin == true) {
                    goto(ListStoryActivity::class.java)
                } else {
                    goto(LoginActivity::class.java)
                }
            }
            finish()
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