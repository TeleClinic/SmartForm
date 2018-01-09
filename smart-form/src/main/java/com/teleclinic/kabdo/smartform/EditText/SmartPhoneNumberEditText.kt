package com.teleclinic.kabdo.smartform

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.pawegio.kandroid.d
import com.teleclinic.kabdo.smartform.EditText.PhoneNumberPopup
import kotlinx.android.synthetic.main.smart_edittext_noteditable.view.*

/**
 * Created by karimabdo on 12/20/17.
 */
class SmartPhoneNumberEditText : FrameLayout {

    private var mContext: Context? = null
    private var attrs: AttributeSet? = null
    private var styleAttr: Int? = null
    private var view = View.inflate(context, R.layout.smart_edittext_noteditable, null)
    private var label = "Phone number"
    private var ok = "OK"
    private var errorMessage = "Invalid number"

    var mandatory: Boolean = true
    var mandatoryErrorMessage: String = "Mandatory Field"

    fun editText() = editText
    fun setError(error: String) {
        smartTextInputLayout.error = error
    }

    fun setHelperText(text: String) {
        smartTextInputLayout.setHelperText(text)
    }

    fun text() = editText.text.toString().trim()

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
        this.mContext = context
        this.attrs = attrs
        this.styleAttr = defStyleAttr
        addView(view)
        readAttributes()
        editText.setOnClickListener { openPhoneActivity() }
    }

    private fun openPhoneActivity() {
        val alert = PhoneNumberPopup(context, label, ok, errorMessage, this::setResult)
        alert.show()
    }

    fun setResult(result:String): Int {
        editText.setText(result)
        return 0
    }

    private fun readAttributes() {
        val arr = mContext?.theme?.obtainStyledAttributes(attrs, R.styleable.SmartPhoneNumberEditText, 0, 0)
        arr?.let {
            it.getString(R.styleable.SmartPhoneNumberEditText_spnLabel)?.let {
                smartTextInputLayout.hint = it
                label = it
            }
            it.getString(R.styleable.SmartPhoneNumberEditText_spnMandatoryErrorMsg)?.let {
                mandatoryErrorMessage = it
            }
            it.getString(R.styleable.SmartPhoneNumberEditText_spnOk)?.let {
                ok = it
            }
            it.getString(R.styleable.SmartPhoneNumberEditText_spnErrorMessage)?.let {
                errorMessage = it
            }
            val color = it.getColor(R.styleable.SmartPhoneNumberEditText_spnTextColor, resources.getColor(R.color.textGray))
            changeTextColor(color)

            mandatory = it.getBoolean(R.styleable.SmartPhoneNumberEditText_spnMandatory, true)

            arr.recycle()
        }
    }

    fun changeTextColor(color: Int) {
        editText.setHintTextColor(color)
        editText.setTextColor(color)
    }

    fun check() = check(true)
    fun getText() = editText?.text.toString()

    private fun check(error: Boolean): Boolean {
        if (mandatory)
            if (editText.text.trim().isEmpty()) {
                if (error)
                    smartTextInputLayout.error = mandatoryErrorMessage
                else {
                    smartTextInputLayout.setHelperText(mandatoryErrorMessage)
                }
                return false
            }
        smartTextInputLayout.isErrorEnabled = false
        return true
    }
}
