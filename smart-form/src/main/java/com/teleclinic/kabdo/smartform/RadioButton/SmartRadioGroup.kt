package com.teleclinic.kabdo.smartform.RadioButton

/**
 * Created by karimabdo on 1/5/18.
 */
import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.util.AttributeSet
import android.widget.RadioButton
import android.widget.RadioGroup
import com.pawegio.kandroid.views
import com.teleclinic.kabdo.smartform.R

/**
 * Created by karimabdo on 12/28/17.
 */
class SmartRadioGroup : RadioGroup {

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

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int?) {
        val arr = context.theme.obtainStyledAttributes(attrs, R.styleable.SmartRadioGroup, 0, 0)
        arr?.let {
            mandatory = it.getBoolean(R.styleable.SmartRadioGroup_srgMandatory, true)
            color = it.getColor(R.styleable.SmartRadioGroup_srgColor, color)
            errorColor = color
            labelColor = it.getColor(R.styleable.SmartRadioGroup_srgTextColor, labelColor)

            val string = it.getString(R.styleable.SmartRadioGroup_srgLabels)
            string.split(",").forEachIndexed { index, it ->
                val radio = RadioButton(context)
                radio.text = it
                radio.id = index
                radio.setTextColor(labelColor)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    radio.buttonTintList = ColorStateList(states(), colors())
                radio.setOnClickListener { removeErrors() }
                addView(radio)
            }

            errorColor = it.getColor(R.styleable.SmartRadioGroup_srgErrorColor, errorColor)
            arr.recycle()
        }
    }

    private fun removeErrors() {
        views.forEach {
            if (it is RadioButton)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    it.buttonTintList = ColorStateList(states(), intArrayOf(color, color))
        }
    }

    private fun states() = arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf())
    private fun colors() = intArrayOf(color, errorColor)

    fun check(): Boolean {
        val radioChecked = checkedIndex()
        val check = radioChecked != -1 || !mandatory
        if (!check)
            views.forEach {
                if (it is RadioButton)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        it.buttonTintList = ColorStateList(states(), colors())
            }
        return radioChecked != -1 || !mandatory
    }

    fun checkedIndex(): Int {
        var radioChecked = -1
        views.forEachIndexed { index, view ->
            if (view is RadioButton)
                if (view.isChecked) radioChecked = index
        }
        return radioChecked
    }
}