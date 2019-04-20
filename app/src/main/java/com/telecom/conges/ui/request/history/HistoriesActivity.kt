package com.telecom.conges.ui.request.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.esprit.core.extensions.observeUIState
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.telecom.conges.extensions.toast
import com.telecom.conges.ui.request.RequestViewModel
import com.telecom.conges.ui.request.detail.RequestDetailActivity
import kotlinx.android.synthetic.main.activity_histories.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class HistoriesActivity : AppCompatActivity() {

    val requestViewModel: RequestViewModel by viewModel()

    private lateinit var fastItemAdapter: FastItemAdapter<RequestItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.telecom.conges.R.layout.activity_histories)
        requestViewModel.loadRequestsByType()

        fastItemAdapter = FastItemAdapter()
        rv.apply {
            layoutManager = LinearLayoutManager(this@HistoriesActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = fastItemAdapter
        }


        requestViewModel.uiHistoriesState.observe(this, Observer {
            val uiModel = it ?: return@Observer
            observeUIState(uiModel, {}, {
                toast(it)
            }, {
                fastItemAdapter.clear()
                it.map {
                    fastItemAdapter.add(RequestItem(it))
                }
            })
        })

        fastItemAdapter.withOnClickListener { _, _, item, _ ->
            startActivity(RequestDetailActivity.starterIntent(this@HistoriesActivity, item.request.id.toString()))
            true
        }

    }


    companion object {

        fun starterIntent(context: Context): Intent {
            return Intent(context, HistoriesActivity::class.java).apply {
                //            putExtra(EXTRA_PAVILION, pavilion)
            }
        }
    }

}
