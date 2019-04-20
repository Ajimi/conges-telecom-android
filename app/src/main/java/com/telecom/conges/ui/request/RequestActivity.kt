package com.telecom.conges.ui.request

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.telecom.conges.R

class RequestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)
    }


    companion object {

        fun starterIntent(context: Context): Intent {
            return Intent(context, RequestActivity::class.java).apply {
                //            putExtra(EXTRA_PAVILION, pavilion)
            }
        }
    }

}
