package com.telecom.conges.ui.request.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.esprit.core.extensions.observeUIState
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.telecom.conges.R
import com.telecom.conges.extensions.toast
import com.telecom.conges.ui.request.RequestViewModel
import com.telecom.conges.ui.request.detail.RequestDetailActivity
import com.telecom.conges.util.State
import com.telecom.conges.util.Tools
import kotlinx.android.synthetic.main.activity_histories.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class HistoriesActivity : AppCompatActivity() {

    val requestViewModel: RequestViewModel by viewModel()

    private lateinit var fastItemAdapter: FastItemAdapter<RequestItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_histories)
        initToolbar()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_historique, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_filter -> {
                showFilteringPopUpMenu()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }

    private fun showFilteringPopUpMenu() {
        val view = findViewById<View>(R.id.menu_filter) ?: return
        PopupMenu(this, view).run {
            menuInflater.inflate(R.menu.filter_historique, menu)

            setOnMenuItemClickListener {
                val filter = when (it.itemId) {
                    R.id.all -> "ALL"
                    R.id.refused -> State.REFUSED.name
                    R.id.waiting -> State.WAITING.name
                    else -> State.ACCEPTED.name
                }
                requestViewModel.filterRequest(filter)
                true
            }
            show()
        }
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Historique de demandes"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.grey_5)
        Tools.setSystemBarLight(this)
    }


    companion object {

        fun starterIntent(context: Context): Intent {
            return Intent(context, HistoriesActivity::class.java).apply {
                //            putExtra(EXTRA_PAVILION, pavilion)
            }
        }
    }

}
