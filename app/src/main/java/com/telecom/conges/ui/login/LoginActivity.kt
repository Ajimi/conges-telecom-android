package com.telecom.conges.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.telecom.conges.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        signin1.setOnClickListener {
            // Call Sign In
        }
    }
}
