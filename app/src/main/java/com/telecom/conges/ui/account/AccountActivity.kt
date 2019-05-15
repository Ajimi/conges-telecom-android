package com.telecom.conges.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.telecom.conges.R
import com.telecom.conges.data.models.User
import com.telecom.conges.extensions.observeUIState
import com.telecom.conges.extensions.toast
import com.telecom.conges.ui.request.RequestActivity
import com.telecom.conges.ui.request.history.HistoriesActivity
import com.telecom.conges.util.Tools
import kotlinx.android.synthetic.main.activity_account.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountActivity : AppCompatActivity() {
    val viewModel: AccountViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        initToolbar()
        intent.getStringExtra(EXTRA_USER_ID)?.let { userId ->
            viewModel.loadUserDetails(userId)
        } ?: run {
            viewModel.user?.let { user ->
                toast("Loading user details")
            } ?: run {
                toast("Woops Can't show the profile")
                finish()
            }
        }

        viewModel.uiUserState.observe(this, Observer {
            val uiModel = it ?: return@Observer
            observeUIState(uiModel, {}, {}, {
                displayDataFor(user = it)
            })
        })

        history.setOnClickListener {
            startActivity(HistoriesActivity.starterIntent(this))
        }

        demande.setOnClickListener {
            startActivity(RequestActivity.starterIntent(this))
        }
    }

    private fun displayDataFor(user: User) {
        Log.v("Helo", user.fullName)
        name.text = user.fullName

        total.text = getTotalSoldText(user.solde)
        val reste = user.solde - user.consumedSolde
        rest.text = getRestSoldText(reste)
        val percent = reste.toFloat() / user.solde.toFloat() * 100
        Log.v("Hello", "$percent")
        crpv.percent = 100 - percent
    }

    private fun getTotalSoldText(solde: Int): String = "Total: $solde Jours"
    private fun getRestSoldText(reste: Int): String = "Reste: $reste Jours"


    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Compte"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.grey_5)
        Tools.setSystemBarLight(this)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }


    companion object {
        const val EXTRA_USER_ID = "USER_ID"

        fun starterIntent(context: Context): Intent {
            return Intent(context, AccountActivity::class.java).apply {
                //            putExtra(EXTRA_PAVILION, pavilion)
            }
        }

        fun starterIntent(context: Context, userId: String): Intent {
            return Intent(context, AccountActivity::class.java).apply {
                putExtra(EXTRA_USER_ID, userId)
            }
        }
    }

}
