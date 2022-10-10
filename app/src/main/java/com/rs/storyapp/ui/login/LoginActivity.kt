package com.rs.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.rs.storyapp.common.util.goto
import com.rs.storyapp.common.util.grabText
import com.rs.storyapp.common.util.isEmailValid
import com.rs.storyapp.data.local.DataUserPreference
import com.rs.storyapp.databinding.ActivityLoginBinding
import com.rs.storyapp.model.request.RequestLogin
import com.rs.storyapp.ui.liststory.ListStoryActivity
import com.rs.storyapp.ui.signup.SignUpActivity
import com.rs.storyapp.viewmodels.LoginViewModel
import com.rs.storyapp.viewmodels.LoginViewModelFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_pref")

class LoginActivity : AppCompatActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private var email = ""
    private var pass = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        playAnimation()

        val pref = DataUserPreference.getInstance(dataStore)
        val loginViewModel = ViewModelProvider(
            this,
            LoginViewModelFactory(pref)
        )[LoginViewModel::class.java]

        setMyButtonEnable()
        validationEditText()

        binding.tvSignUp.setOnClickListener {
            goto(SignUpActivity::class.java)
        }

        binding.btnLogin.setOnClickListener {
            email = binding.edLoginEmail.grabText()
            pass = binding.edLoginPassword.grabText()

            val requestLogin = RequestLogin(email = email, password = pass)
            loginViewModel.login(requestLogin)
        }

        loginViewModel.isLoading.observe(this) { isLoading ->
            binding.progressCircular.isVisible = isLoading
            binding.btnLogin.isEnabled = !isLoading
        }

        loginViewModel.message.observe(this) { message ->
            Toast.makeText(this, getString(message), Toast.LENGTH_LONG).show()
        }

        loginViewModel.messageWhenFailure.observe(this) { messageWhenFailure ->
            Toast.makeText(this, messageWhenFailure, Toast.LENGTH_LONG).show()
        }

        loginViewModel.isSuccessLogin.observe(this) { isValidLogin ->
            if (isValidLogin) {
                goto(ListStoryActivity::class.java)
                finish()
            }
        }


    }

    private fun validationEditText() {
        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
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