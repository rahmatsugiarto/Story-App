package com.rs.storyapp.common.mycustomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.rs.storyapp.R

class CustomEtPass : AppCompatEditText, View.OnTouchListener {
    private lateinit var visibilityIcon: Drawable


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        visibilityIcon = ContextCompat.getDrawable(context, R.drawable.ic_visibility) as Drawable
        transformationMethod = PasswordTransformationMethod.getInstance()
        setOnTouchListener(this)
        showVisibilityIcon()

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                showVisibilityIcon()
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkPassValid()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun showVisibilityIcon() {
        setButtonDrawables(endOfTheText = visibilityIcon)
    }


    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )

    }

    private fun checkPassValid() {
        val pass = text?.trim()
        error = when {
            pass.isNullOrEmpty() -> {
                resources.getString(R.string.password_required)
            }
            pass.length < 6 -> {
                resources.getString(R.string.pass_length)
            }
            else -> {
                return
            }
        }
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (!focused) checkPassValid()
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isVisibilityButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (visibilityIcon.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isVisibilityButtonClicked = true
                }
            } else {
                clearButtonStart = (width - paddingEnd - visibilityIcon.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isVisibilityButtonClicked = true
                }
            }
            if (isVisibilityButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        visibilityIcon = ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_visibility_off
                        ) as Drawable
                        transformationMethod = HideReturnsTransformationMethod.getInstance()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        visibilityIcon = ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_visibility
                        ) as Drawable
                        when {
                            text != null -> transformationMethod =
                                PasswordTransformationMethod.getInstance()
                        }
                        showVisibilityIcon()
                        return true
                    }
                    else -> return false
                }
            } else return false
        }
        return false

    }
}