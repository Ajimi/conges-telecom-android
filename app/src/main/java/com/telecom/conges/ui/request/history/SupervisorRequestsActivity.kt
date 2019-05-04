package com.telecom.conges.ui.request.history

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.esprit.core.extensions.observeUIState
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.telecom.conges.R
import com.telecom.conges.data.models.RequestRole
import com.telecom.conges.extensions.toast
import com.telecom.conges.ui.request.RequestViewModel
import com.telecom.conges.util.State
import com.telecom.conges.util.Tools
import kotlinx.android.synthetic.main.activity_histories.*
import kotlinx.android.synthetic.main.dialog_term_of_services.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class SupervisorRequestsActivity : AppCompatActivity() {

    val requestViewModel: RequestViewModel by viewModel()

    private lateinit var fastItemAdapter: FastItemAdapter<RequestSupervisorItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_histories)
        initToolbar()
        requestViewModel.loadRequestsByType(RequestRole.SUPERVISOR.name)
        fastItemAdapter = FastItemAdapter()
        rv.apply {
            layoutManager = LinearLayoutManager(this@SupervisorRequestsActivity)
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
                    fastItemAdapter.add(RequestSupervisorItem(it))
                }
                if (it.isEmpty()) {
                    // TODO display Empty List
                }
            })
        })

        fastItemAdapter.withOnClickListener { _, _, item, _ ->
            showTermServicesDialog()
            Log.v("ITEM REQUEST", item.request.toString())
            true
        }

    }

    private fun showTermServicesDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_term_of_services)
        dialog.setCancelable(true)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT

//        findViewById<ImageButton>(R.id.bt_close).setOnClickListener { dialog.dismiss() }

        dialog.close_btn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.bt_accept.setOnClickListener {
            toast("Button Accept Clicked", Toast.LENGTH_SHORT)
        }

        dialog.bt_decline.setOnClickListener {
            toast("Button Decline Clicked", Toast.LENGTH_SHORT)
        }

        dialog.show()
        dialog.window?.attributes = layoutParams
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.telecom.conges.R.menu.menu_historique, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            com.telecom.conges.R.id.menu_filter -> {
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
        supportActionBar!!.title = "Demandes"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.grey_5)
        Tools.setSystemBarLight(this)
    }


    companion object {

        fun starterIntent(context: Context): Intent {
            return Intent(context, SupervisorRequestsActivity::class.java).apply {
                //            putExtra(EXTRA_PAVILION, pavilion)
            }
        }
    }

}
