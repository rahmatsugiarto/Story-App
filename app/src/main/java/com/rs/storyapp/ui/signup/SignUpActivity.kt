package com.rs.storyapp.ui.signup

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
import com.rs.storyapp.common.util.grabText
import com.rs.storyapp.common.util.hideSoftKeyboard
import com.rs.storyapp.common.util.isEmailValid
import com.rs.storyapp.common.util.showToastShort
import com.rs.storyapp.data.Result
import com.rs.storyapp.databinding.ActivitySignUpBinding
import com.rs.storyapp.model.request.RequestSignUp
import com.rs.storyapp.viewmodels.SignUpViewModel
import com.rs.storyapp.viewmodels.ViewModelFactory


class SignUpActivity : AppCompatActivity() {
    private val signUpViewModel: SignUpViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }
    private var name = ""
    private var email = ""
    private var pass = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        playAnimation()

        setMyButtonEnable()
        validationEditText()

        binding.btnSignup.setOnClickListener {
            hideSoftKeyboard()
            binding.apply {
                edRegisterEmail.clearFocus()
                edRegisterName.clearFocus()
                edRegisterPassword.clearFocus()
            }
            name = binding.edRegisterName.grabText()
            email = binding.edRegisterEmail.grabText()
            pass = binding.edRegisterPassword.grabText()

            val newUser = RequestSignUp(name = name, email = email, password = pass)

            signUp(newUser)

        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }

    private fun signUp(requestRegister: RequestSignUp) {
        signUpViewModel.userSignUp(requestRegister).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.apply {
                            progressCircular.visibility = View.VISIBLE
                            btnSignup.isEnabled = false

                        }
                    }
                    is Result.Success -> {
                        binding.progressCircular.visibility = View.GONE
                        showToastShort(getString(R.string.success_create_account))
                        finish()
                    }
                    is Result.Error -> {
                        binding.progressCircular.visibility = View.GONE
                        binding.btnSignup.isEnabled = true
                        if (result.error.contains("400")) {
                            showToastShort(getString(R.string.email_already_exists))
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
        binding.edRegisterName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.edRegisterEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
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
        name = binding.edRegisterName.grabText()
        email = binding.edRegisterEmail.grabText()
        pass = binding.edRegisterPassword.grabText()
        binding.btnSignup.isEnabled =
            name.isNotEmpty() && isEmailValid(email) && email.isNotEmpty() && pass.length > 6 && pass.isNotEmpty()
        Log.d("setMyButtonEnable", "setMyButtonEnable: ${pass.isNotEmpty()}")
    }

    private fun playAnimation() {
        val image =
            ObjectAnimator.ofFloat(binding.image, View.ALPHA, 1f).setDuration(500)
        val tilName =
            ObjectAnimator.ofFloat(binding.tilName, View.ALPHA, 1f).setDuration(500)
        val tilEmail =
            ObjectAnimator.ofFloat(binding.tilEmail, View.ALPHA, 1f).setDuration(500)
        val tilPassword =
            ObjectAnimator.ofFloat(binding.tilPassword, View.ALPHA, 1f).setDuration(500)
        val btnSignup =
            ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(500)
        val linearLayout =
            ObjectAnimator.ofFloat(binding.linearLayout, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                image,
                tilName,
                tilEmail,
                tilPassword,
                btnSignup,
                linearLayout,
            )
            startDelay = 500
        }.start()
    }

}