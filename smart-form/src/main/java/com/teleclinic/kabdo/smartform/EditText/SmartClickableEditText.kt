package com.teleclinic.kabdo.smartform

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.smart_edittext_noteditable.view.*

/**
 * Created by karimabdo on 12/20/17.
 */
class SmartClickableEditText : FrameLayout {

    private var mContext: Context? = null
    private var attrs: AttributeSet? = null
    private var styleAttr: Int? = null
    private var view = View.inflate(context, R.layout.smart_edittext_noteditable, null)
    var mandatory: Boolean = true
    var mandatoryErrorMessage: String = "Mandatory Field"
    fun editText() = editText
    fun setError(error: String) {
        smartTextInputLayout.error = error
    }
    fun setHelperText(text:String){
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
    }

    private fun readAttributes() {
        val arr = mContext?.theme?.obtainStyledAttributes(attrs, R.styleable.SmartClickableEditText, 0, 0)
        arr?.let {
            it.getString(R.styleable.SmartClickableEditText_scetLabel)?.let {
                smartTextInputLayout.hint = it
            }
            it.getString(R.styleable.SmartClickableEditText_scetMandatoryErrorMsg)?.let {
                mandatoryErrorMessage = it
            }
            val color = it.getColor(R.styleable.SmartClickableEditText_scetTextColor, resources.getColor(R.color.textGray))
            changeTextColor(color)

            mandatory = it.getBoolean(R.styleable.SmartClickableEditText_scetMandatory, true)

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
