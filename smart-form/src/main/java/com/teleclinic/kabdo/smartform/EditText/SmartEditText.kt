package com.teleclinic.kabdo.smartform

import android.content.Context
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import android.widget.FrameLayout
import java.util.regex.Pattern
import com.pawegio.kandroid.d
import android.text.InputType
import com.pawegio.kandroid.textWatcher
import com.teleclinic.kabdo.smartform.R
import kotlinx.android.synthetic.main.smart_edittext.view.*


/**
 * Created by karimabdo on 12/20/17.
 */
class SmartEditText : FrameLayout {

    private var mContext: Context? = null
    private var attrs: AttributeSet? = null
    private var styleAttr: Int? = null
    private var view = View.inflate(context, R.layout.smart_edittext, null)

    var mandatory: Boolean = true
    var mandatoryErrorMessage: String = "Mandatory Field"
    var regex: Pattern? = null
    var regexErrorMessage: String = "Wrong Format"
    var validateOnKeyPressed: Boolean = true
    val passwordLength = 8
    fun editText() = editText
    fun setLabel(label: String) {
        smartTextInputLayout.hint = label
    }

    fun setError(error: String) {
        smartTextInputLayout.error = error
    }

    fun text() = editText.text.toString().trim()
    fun setHelperText(text: String) {
        smartTextInputLayout.setHelperText(text)
    }

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
        editText.textWatcher { afterTextChanged { smartTextInputLayout.isErrorEnabled = false } }
        readAttributes()
    }

    private fun readAttributes() {
        val arr = mContext?.theme?.obtainStyledAttributes(attrs, R.styleable.SmartEditText, 0, 0)
        arr?.let {
            it.getString(R.styleable.SmartEditText_setLabel)?.let {
                smartTextInputLayout.hint = it
            }
            it.getString(R.styleable.SmartEditText_setMandatoryErrorMsg)?.let {
                mandatoryErrorMessage = it
            }
            it.getString(R.styleable.SmartEditText_setRegexErrorMsg)?.let {
                regexErrorMessage = it
            }
            if (it.getInt(R.styleable.SmartEditText_setRegexType, -1) != -1) {
                setValidationType(ValidationType.values()[it.getInt(R.styleable.SmartEditText_setRegexType, -1)])
            }
            it.getString(R.styleable.SmartEditText_setRegexString)?.let {
                setValidationRegex(it)
            }
            if (it.getBoolean(R.styleable.SmartEditText_setValidateOnKey, false)) {
                editText.textWatcher { afterTextChanged { check(false) } }
            }
            if (it.getBoolean(R.styleable.SmartEditText_setPasswordField, false)) {
                smartTextInputLayout.isPasswordVisibilityToggleEnabled = true
                editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            val color = it.getColor(R.styleable.SmartEditText_setTextColor, resources.getColor(R.color.textGray))
            changeTextColor(color)

            mandatory = it.getBoolean(R.styleable.SmartEditText_setMandatory, true)

            arr.recycle()
        }
    }

    fun changeTextColor(color: Int) {
        editText.setHintTextColor(color)
        editText.setTextColor(color)
    }

    fun check() = check(true)

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
        regex?.let {
            if (!it.matcher(editText.text.toString()).find()) {
                if (error)
                    smartTextInputLayout.error = regexErrorMessage
                else {
                    smartTextInputLayout.setHelperText(regexErrorMessage)
                }
                return false
            }
        }
        smartTextInputLayout.isErrorEnabled = false
        return true
    }

    private val small = ".*[a-z].*"
    private val capital = ".*[A-Z].*"
    private val digit = ".*\\d.*"
    private val sign = ".*[^A-Za-z0-9].*"

    private val mediumPassword = Pattern.compile("^(((?=$small)(?=$digit))|((?=$small)(?=$capital))|((?=$digit)(?=$capital))|((?=$small)(?=$sign))|((?=$sign)(?=$capital))|((?=$digit)(?=$sign)))(?=.{$passwordLength,})")
    private val complexPassword = Pattern.compile("^(?=$small)(?=$capital)(?=$digit)(?=$sign)(?=.{$passwordLength,})")
    private val namePattern = Pattern.compile("^((?=$small)|(?=$capital))(?=.{2,})")

    fun setValidationType(type: ValidationType) {
        regex = when (type) {
            ValidationType.EMAIL_VALIDATION -> Patterns.EMAIL_ADDRESS
            ValidationType.MEDIUM_PASSWORD_VALIDATION -> mediumPassword
            ValidationType.COMPLEX_PASSWORD_VALIDATION -> complexPassword
            ValidationType.PHONE_NUMBER_VALIDATION -> Patterns.PHONE
            ValidationType.NAME_VALIDATION -> namePattern
        }
        d("REGEX ::::", regex.toString())
    }

    fun setValidationRegex(regexString: String) {
        regex = Pattern.compile(regexString)
    }
}

enum class ValidationType {
    EMAIL_VALIDATION,
    MEDIUM_PASSWORD_VALIDATION,
    COMPLEX_PASSWORD_VALIDATION,
    PHONE_NUMBER_VALIDATION,
    NAME_VALIDATION,
}


