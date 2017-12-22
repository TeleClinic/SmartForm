package com.teleclinic.kabdo.smartform

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.AttributeSet
import android.widget.CheckBox
import com.teleclinic.kabdo.smartform.R

/**
 * Created by karimabdo on 12/22/17.
 */
class SmartCheckBox : CheckBox {

    var mandatory: Boolean = true
    var color: Int = resources.getColor(R.color.primary)
    var errorColor: Int = resources.getColor(R.color.primary)
    var labelColor: Int = resources.getColor(R.color.textGray)

    constructor(context: Context) : super(context) {
        init(context, null, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, null)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int?) {
        val arr = context.theme.obtainStyledAttributes(attrs, R.styleable.SmartCheckBox, 0, 0)
        arr?.let {
            mandatory = it.getBoolean(R.styleable.SmartCheckBox_scbMandatory, true)
            color = it.getColor(R.styleable.SmartCheckBox_scbColor, color)
            errorColor = color
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                buttonTintList = ColorStateList(states(), colors())
            errorColor = it.getColor(R.styleable.SmartCheckBox_scbErrorColor, errorColor)
            labelColor = it.getColor(R.styleable.SmartCheckBox_scbTextColor, labelColor)
            setTextColor(labelColor)

            arr.recycle()
        }
    }

    private fun states() = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf())
    private fun colors() = intArrayOf(color, errorColor)

    fun check(): Boolean {
        if (mandatory)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                buttonTintList = ColorStateList(states(), colors())
        val check = isChecked || !mandatory
        if (!check) requestFocus()
        return check
    }
}
