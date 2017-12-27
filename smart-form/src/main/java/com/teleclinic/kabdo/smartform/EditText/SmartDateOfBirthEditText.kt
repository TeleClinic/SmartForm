package com.teleclinic.kabdo.smartform

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.teleclinic.kabdo.smartform.helper.date.SpinnerDatePickerDialogBuilder
import kotlinx.android.synthetic.main.smart_edittext_noteditable.view.*
import java.util.*


/**
 * Created by karimabdo on 12/20/17.
 */
class SmartDateOfBirthEditText : FrameLayout {

    private var mContext: Context? = null
    private var attrs: AttributeSet? = null
    private var styleAttr: Int? = null
    private var view = View.inflate(context, R.layout.smart_edittext_noteditable, null)
    private var dateOfBirth: String = ""
    var calendar = Calendar.getInstance()
    var mandatory: Boolean = true
    var mandatoryErrorMessage: String = "Mandatory Field"
    fun editText() = editText
    fun setError(error: String) {
        smartTextInputLayout.error = error
    }
    fun setHelperText(text:String){
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
        readAttributes()
        editText.setOnClickListener { showDate(calendar, R.style.DatePickerSpinner) }
    }

    private fun showDate(calendar: Calendar, spinnerTheme: Int) {
        SpinnerDatePickerDialogBuilder()
                .context(context)
                .maxDate(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                .callback({ _, year, monthOfYear, dayOfMonth ->
                    dateOfBirth = "$year-$monthOfYear-$dayOfMonth"
                    val day = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                    val realMonth = monthOfYear + 1
                    val month = if (realMonth < 10) "0$realMonth" else "$realMonth"
                    editText.setText("$day.$month.$year")
                    calendar.set(year, monthOfYear, dayOfMonth)
                })
                .defaultDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .spinnerTheme(spinnerTheme)
                .build()
                .show()
    }

    private fun readAttributes() {
        val arr = mContext?.theme?.obtainStyledAttributes(attrs, R.styleable.SmartDateOfBirthEditText, 0, 0)
        arr?.let {
            it.getString(R.styleable.SmartDateOfBirthEditText_sdofLabel)?.let {
                smartTextInputLayout.hint = it
            }
            it.getString(R.styleable.SmartDateOfBirthEditText_sdofMandatoryErrorMsg)?.let {
                mandatoryErrorMessage = it
            }
            val color = it.getColor(R.styleable.SmartDateOfBirthEditText_sdofTextColor, resources.getColor(R.color.textGray))
            changeTextColor(color)

            mandatory = it.getBoolean(R.styleable.SmartDateOfBirthEditText_sdofMandatory, true)

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
