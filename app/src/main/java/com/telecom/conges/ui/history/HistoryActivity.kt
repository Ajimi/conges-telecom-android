package com.telecom.conges.ui.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.telecom.conges.R

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
    }


    companion object {

        fun starterIntent(context: Context): Intent {
            return Intent(context, HistoryActivity::class.java).apply {
                //            putExtra(EXTRA_PAVILION, pavilion)
            }
        }
    }

}
