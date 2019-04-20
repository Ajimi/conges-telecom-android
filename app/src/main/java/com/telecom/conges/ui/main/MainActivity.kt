package com.telecom.conges.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.telecom.conges.R
import com.telecom.conges.ui.account.AccountActivity
import com.telecom.conges.ui.history.HistoryActivity
import com.telecom.conges.ui.request.RequestActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun handleClick(view: View) {
        when (view.id) {
            R.id.historique -> {
                HistoryActivity.starterIntent(this@MainActivity)
            }
            R.id.request -> {
                RequestActivity.starterIntent(this@MainActivity)

            }
            R.id.account -> {
                AccountActivity.starterIntent(this@MainActivity)

            }
            else -> {
                Log.v("UNHANDLED ", "Clicked has no implementation Yet")
            }
        }
    }

    companion object {

        fun starterIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java).apply {
                //            putExtra(EXTRA_PAVILION, pavilion)
            }
        }
    }
}
