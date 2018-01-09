package com.teleclinic.kabdo.smartform

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener { if (form.check()) continueWithYourCode() }
        termsCheckbox.textView().setOnClickListener { Toast.makeText(this, "Do something", Toast.LENGTH_SHORT).show() }
    }

    private fun continueWithYourCode() {
        finish()
    }
}
