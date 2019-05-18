package com.telecom.conges.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.telecom.conges.R
import com.telecom.conges.ui.account.AccountActivity
import com.telecom.conges.ui.calendar.CalendarActivity
import com.telecom.conges.ui.ferie.FerieDisplayActivity
import com.telecom.conges.ui.login.LoginActivity
import com.telecom.conges.ui.request.RequestActivity
import com.telecom.conges.ui.request.UserListActivity
import com.telecom.conges.ui.request.history.HistoriesActivity
import com.telecom.conges.ui.statistics.StatisticActivity
import com.telecom.conges.util.Tools
import kotlinx.android.synthetic.main.activity_request.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {


    val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainViewModel.getLayoutByRole())
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
            R.id.statistiques -> {
                startActivity(StatisticActivity.starterIntent(this@MainActivity))
            }
            R.id.ferie -> {
                startActivity(FerieDisplayActivity.starterIntent(this@MainActivity))
            }
            R.id.demande -> {
//                startActivity(RequestsListActivity.starterIntent(this@MainActivity))
                startActivity(UserListActivity.starterIntent(this@MainActivity))

            }
            R.id.calendrier -> {
                startActivity(CalendarActivity.starterIntent(this@MainActivity))
            }
            else -> {
                Log.v("UNHANDLED ", "Clicked has no implementation Yet")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.action_logout -> {
                mainViewModel.logout()
                startActivity(LoginActivity.starterIntent(this))
                true
            }
            else -> false
        }


    companion object {

        fun starterIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
    }
}
