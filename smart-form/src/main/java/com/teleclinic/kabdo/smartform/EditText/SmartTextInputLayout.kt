package com.teleclinic.kabdo.smartform

import android.content.Context
import android.content.res.ColorStateList
import android.support.design.widget.TextInputLayout
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.widget.EditText
import android.widget.TextView

open class SmartTextInputLayout : TextInputLayout {

    private var mHelperText: CharSequence? = null
    private var mHelperTextColor: ColorStateList? = null
    private var mHelperTextEnabled = false
    private var mErrorEnabled = false
    private var mHelperView: TextView? = null
    var helperTextAppearance = R.style.HelperTextAppearance

    constructor(_context: Context) : super(_context)

    constructor(_context: Context, _attrs: AttributeSet, _defStyleAttr: Int) : super(_context, _attrs, _defStyleAttr) {
        init(_context, _attrs)
    }

    constructor(_context: Context, _attrs: AttributeSet) : super(_context, _attrs) {
        init(_context, _attrs)
    }

    private fun init(_context: Context, _attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(
                _attrs, R.styleable.SmartTextInputLayout, 0, 0)
        try {
            mHelperTextColor = a.getColorStateList(R.styleable.SmartTextInputLayout_helperText)
            mHelperText = a.getText(R.styleable.SmartTextInputLayout_helperText)
        } finally {
            a.recycle()
        }
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        super.addView(child, index, params)
        if (child is EditText) {
            if (!TextUtils.isEmpty(mHelperText)) {
                setHelperText(mHelperText ?: "")
            }
        }
    }

    override fun setHelperTextColor(_helperTextColor: ColorStateList?) {
        mHelperTextColor = _helperTextColor
    }

    override fun setHelperTextEnabled(_enabled: Boolean) {
        if (mHelperTextEnabled == _enabled) return
        if (_enabled && mErrorEnabled) {
            isErrorEnabled = false
        }
        if (this.mHelperTextEnabled != _enabled) {
            if (_enabled) {
                this.mHelperView = TextView(this.context)
                this.mHelperView?.setTextAppearance(this.context, this.helperTextAppearance)
                if (mHelperTextColor != null) {
                    this.mHelperView!!.setTextColor(mHelperTextColor)
                }
                this.mHelperView?.visibility = View.INVISIBLE
                this.addView(this.mHelperView)
                if (this.mHelperView != null) {
                    ViewCompat.setPaddingRelative(
                            this.mHelperView!!,
                            ViewCompat.getPaddingStart(this.editText as View),
                            0, ViewCompat.getPaddingEnd(this.editText as View),
                            editText?.paddingBottom ?: 0)
                }
            } else {
                this.removeView(this.mHelperView)
                this.mHelperView = null
            }

            this.mHelperTextEnabled = _enabled
        }
    }

    override fun setHelperText(_helperText: CharSequence?) {
        if (mHelperText ?: "" == _helperText)
            return
        isErrorEnabled = false
        mHelperText = _helperText
        if (!this.mHelperTextEnabled) {
            if (TextUtils.isEmpty(mHelperText)) {
                return
            }
            this.setHelperTextEnabled(true)
        }

        if (!TextUtils.isEmpty(mHelperText)) {
            this.mHelperView?.text = mHelperText
            this.mHelperView?.visibility = View.VISIBLE
            ViewCompat.setAlpha(this.mHelperView, 0.0f)
            ViewCompat.animate(this.mHelperView!!)
                    .alpha(1.0f).setDuration(200L)
                    .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                    .setListener(null).start()
        } else if (this.mHelperView?.visibility == View.VISIBLE) {
            ViewCompat.animate(this.mHelperView!!)
                    .alpha(0.0f).setDuration(200L)
                    .setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR)
                    .setListener(object : ViewPropertyAnimatorListenerAdapter() {
                        override fun onAnimationEnd(view: View?) {
                            mHelperView?.text = null
                            mHelperView?.visibility = View.INVISIBLE
                        }
                    }).start()
        }
        this.sendAccessibilityEvent(2048)
    }

    override fun setErrorEnabled(_enabled: Boolean) {
        if (mErrorEnabled == _enabled) return
        mErrorEnabled = _enabled
        if (_enabled && mHelperTextEnabled) {
            setHelperTextEnabled(false)
        }

        super.setErrorEnabled(_enabled)

        if (!(_enabled || TextUtils.isEmpty(mHelperText))) {
            setHelperText(mHelperText ?: "")
        }
    }

    fun getText() = editText?.text.toString()

    override fun setError(_helperText: CharSequence?) {
        if (error == _helperText)
            return
        super.setError(_helperText)
        isErrorEnabled = true
    }

    companion object {

        internal val FAST_OUT_SLOW_IN_INTERPOLATOR: Interpolator = FastOutSlowInInterpolator()
    }
}
