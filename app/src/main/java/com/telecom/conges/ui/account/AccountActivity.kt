package com.telecom.conges.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.telecom.conges.R

class AccountActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
    }


    companion object {

        fun starterIntent(context: Context): Intent {
            return Intent(context, AccountActivity::class.java).apply {
                //            putExtra(EXTRA_PAVILION, pavilion)
            }
        }
    }

}
