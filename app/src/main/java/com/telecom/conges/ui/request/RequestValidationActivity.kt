package com.telecom.conges.ui.request

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.esprit.core.extensions.observeUIState
import com.telecom.conges.R
import com.telecom.conges.extensions.toast
import com.telecom.conges.util.Tools
import kotlinx.android.synthetic.main.activity_request.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RequestValidationActivity : AppCompatActivity() {

    val requestViewModel: RequestViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_validation)
        initToolbar()

        requestViewModel.uiAcceptRequestState.observe(this, Observer {
            val uiModel = it ?: return@Observer
            observeUIState(uiModel, {}, {
                toast(it)
            }, {

            })
        })
        requestViewModel.uiRefuseRequestState.observe(this, Observer {
            val uiModel = it ?: return@Observer
            observeUIState(uiModel, {}, {
                toast(it)
            }, {

            })
        })
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Demande de congés"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, com.telecom.conges.R.color.grey_5)
        Tools.setSystemBarLight(this)
    }

    private fun acceptRequest(id: String) {
        requestViewModel.acceptRequest(id)
    }

    private fun refuseRequest(id: String) {
        requestViewModel.acceptRequest(id)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }
}
