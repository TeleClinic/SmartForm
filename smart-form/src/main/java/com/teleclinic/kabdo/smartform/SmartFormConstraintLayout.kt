package com.teleclinic.kabdo.smartform

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import com.pawegio.kandroid.views
import com.teleclinic.kabdo.smartform.RadioButton.SmartRadioGroup

/**
 * Created by karimabdo on 12/22/17.
 */
class SmartFormConstraintLayout : ConstraintLayout {

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
    }

    fun check(): Boolean {
        var flag = true
        var firstError: View? = null
        views.forEach {
            when (it) {
                is SmartCheckBox -> flag = it.check() && flag
                is SmartEditText -> flag = it.check() && flag
                is SmartCheckBoxLayout -> flag = it.check() && flag
                is SmartDateOfBirthEditText -> flag = it.check() && flag
                is SmartClickableEditText -> flag = it.check() && flag
                is SmartRadioGroup -> flag = it.check() && flag
                is SmartPhoneNumberEditText -> flag = it.check() && flag
                is SmartFormLinearLayout -> flag = it.check() && flag
                is SmartFormRelativeLayout -> flag = it.check() && flag
                is SmartFormConstraintLayout -> flag = it.check() && flag
                else -> {
                }
            }
            if (!flag && firstError == null) {
                firstError = it
            }
        }
        firstError?.requestFocus()
        return flag
    }
}