package com.telecom.conges.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.telecom.conges.R
import com.telecom.conges.ui.account.AccountActivity
import com.telecom.conges.ui.calendar.CalendarActivity
import com.telecom.conges.ui.ferie.FerieDisplayActivity
import com.telecom.conges.ui.request.RequestActivity
import com.telecom.conges.ui.request.history.HistoriesActivity
import com.telecom.conges.util.Tools
import kotlinx.android.synthetic.main.activity_request.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolbar()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        Tools.setSystemBarColor(this, R.color.grey_5)
        Tools.setSystemBarLight(this)
    }

    fun handleClick(view: View) {
        when (view.id) {
            R.id.historique -> {
                startActivity(HistoriesActivity.starterIntent(this@MainActivity))
            }
            R.id.request -> {
                startActivity(RequestActivity.starterIntent(this@MainActivity))

            }
            R.id.account -> {
                startActivity(AccountActivity.starterIntent(this@MainActivity))

            }
            R.id.ferie -> {
                startActivity(FerieDisplayActivity.starterIntent(this@MainActivity))
            }
            R.id.calendrier -> {
                startActivity(CalendarActivity.starterIntent(this@MainActivity))
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
