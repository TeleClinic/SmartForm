package com.teleclinic.kabdo.smartform

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.text.Html
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.smart_checkbox.view.*

/**
 * Created by karimabdo on 12/22/17.
 */
class SmartCheckBoxLayout : FrameLayout {
    private var view = View.inflate(context, R.layout.smart_checkbox, null)

    var mandatory: Boolean = true
    var color: Int = resources.getColor(R.color.primary)
    var errorColor: Int = resources.getColor(R.color.primary)
    var labelColor: Int = resources.getColor(R.color.textGray)

    fun checkbox() = checkbox
    fun textView(): TextView = textView

    constructor(context: Context) : super(context) {
        init(context, null, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, null)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    private fun states() = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf())
    private fun colors() = intArrayOf(color, errorColor)

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int?) {
        addView(view)

        val arr = context.theme.obtainStyledAttributes(attrs, R.styleable.SmartCheckBoxLayout, 0, 0)
        arr?.let {
            mandatory = it.getBoolean(R.styleable.SmartCheckBoxLayout_scblMandatory, true)
            color = it.getColor(R.styleable.SmartCheckBoxLayout_scblColor, color)
            errorColor = color
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                checkbox.buttonTintList = ColorStateList(states(), colors())
            errorColor = it.getColor(R.styleable.SmartCheckBoxLayout_scblErrorColor, errorColor)
            labelColor = it.getColor(R.styleable.SmartCheckBoxLayout_scblTextColor, labelColor)
            textView.setTextColor(labelColor)
            val text = it.getString(R.styleable.SmartCheckBoxLayout_scblText)
            textView.text = Html.fromHtml(String.format(text))

            arr.recycle()
        }
    }

    fun check(): Boolean {
        if (mandatory)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                checkbox.buttonTintList = ColorStateList(states(), colors())
        val check = checkbox.isChecked || !mandatory
        if (!check) requestFocus()
        return check
    }
}