package com.teleclinic.kabdo.smartform.EditText

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.teleclinic.kabdo.smartform.R
import kotlinx.android.synthetic.main.activity_phone_number.*


class PhoneNumberPopup(mContext: Context,
                       private val label: String,
                       private val ok: String,
                       private val errorMessage: String,
                       private val function: (String) -> Int) : Dialog(mContext) {

    fun okButton(): View = okButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number)
        setTitle("")
        ccp.registerCarrierNumberEditText(editTextLayout.editText())
        editTextLayout.setLabel(label)
        okButton.text = ok
        okButton.setOnClickListener {
            val result = getResult()
            if (result != "error") {
                function.invoke(result)
                dismiss()
            }
        }
    }

    fun getResult() = when {
        ccp.isValidFullNumber -> {
            val input = "+" + ccp.selectedCountryCode + editTextLayout.text()
                    .removePrefix("0")
            input
        }
        editTextLayout.text().isEmpty() -> {
            ""
        }
        else -> {
            editTextLayout.setError(errorMessage)
            "error"
        }
    }
}
