package com.telecom.conges.ui.request.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.esprit.core.extensions.observeUIState
import com.telecom.conges.extensions.toast
import com.telecom.conges.ui.request.RequestViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RequestDetailActivity : AppCompatActivity() {

    val requestViewModel: RequestViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.telecom.conges.R.layout.activity_request_detail)

        if (intent.hasExtra(EXTRA_REQUEST_ID)) {
            requestViewModel.launchLoadRequest(intent.getStringExtra(EXTRA_REQUEST_ID))
        }
        requestViewModel.uiRequestState.observe(this, Observer {
            val uiModel = it ?: return@Observer
            observeUIState(uiModel, {}, {
                toast(it)
            }, {
                Log.v("Request", "$it")
            })
        })
    }

    companion object {
        private const val EXTRA_REQUEST_ID = "REQUEST_ID"
        fun starterIntent(context: Context, id: String): Intent {
            return Intent(context, RequestDetailActivity::class.java).apply {
                putExtra(EXTRA_REQUEST_ID, id)
            }
        }

    }
}