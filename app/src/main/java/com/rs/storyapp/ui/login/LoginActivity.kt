package com.rs.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.rs.storyapp.R
import com.rs.storyapp.common.util.*
import com.rs.storyapp.data.Result
import com.rs.storyapp.databinding.ActivityLoginBinding
import com.rs.storyapp.model.request.RequestLogin
import com.rs.storyapp.ui.liststory.ListStoryActivity
import com.rs.storyapp.ui.signup.SignUpActivity
import com.rs.storyapp.viewmodels.LoginViewModel
import com.rs.storyapp.viewmodels.ViewModelFactory


class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val loginViewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private var email = ""
    private var pass = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        playAnimation()


        setMyButtonEnable()
        validationEditText()

        binding.tvSignUp.setOnClickListener {
            goto(SignUpActivity::class.java)
        }

        binding.btnLogin.setOnClickListener {
            hideSoftKeyboard()
            binding.apply {
                edLoginEmail.clearFocus()
                edLoginPassword.clearFocus()
            }
            email = binding.edLoginEmail.grabText()
            pass = binding.edLoginPassword.grabText()

            val requestLogin = RequestLogin(email = email, password = pass)
            login(requestLogin)
        }
    }

    private fun login(requestLogin: RequestLogin) {
        loginViewModel.userLogin(requestLogin).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressCircular.visibility = View.VISIBLE
                        binding.btnLogin.isEnabled = false
                    }
                    is Result.Success -> {
                        binding.progressCircular.visibility = View.GONE
                        loginViewModel.saveToken(result.data.loginResult.token)
                        gotoWithToken(ListStoryActivity::class.java, result.data.loginResult.token)
                        finish()
                    }
                    is Result.Error -> {
                        binding.progressCircular.visibility = View.GONE
                        binding.btnLogin.isEnabled = true
                        Log.d("errorLogin", "login: ${result.error}")

                        if (result.error.contains("401")) {
                            showToastShort(getString(R.string.invalid_email_pass))
                        } else if (result.error.contains("408")) {
                            showToastShort(getString(R.string.timeout))
                        } else {
                            showToastShort(result.error)
                        }
                    }
                }
            }
        }
    }

    private fun validationEditText() {
        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun setMyButtonEnable() {
        val email = binding.edLoginEmail.grabText()
        val password = binding.edLoginPassword.grabText()
        binding.btnLogin.isEnabled =
            email.isNotEmpty() && isEmailValid(email) && password.length > 6 && password.isNotEmpty()
    }

    private fun playAnimation() {
        val tvStoryApp =
            ObjectAnimator.ofFloat(binding.tvStoryApp, View.ALPHA, 1f).setDuration(500)
        val tilEmail =
            ObjectAnimator.ofFloat(binding.tilEmail, View.ALPHA, 1f).setDuration(500)
        val tilPass =
            ObjectAnimator.ofFloat(binding.tilPass, View.ALPHA, 1f).setDuration(500)
        val btnLogin =
            ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val linearLayout =
            ObjectAnimator.ofFloat(binding.linearLayout, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                tvStoryApp,
                tilEmail,
                tilPass,
                linearLayout,
                btnLogin
            )
            startDelay = 500
        }.start()
    }
}