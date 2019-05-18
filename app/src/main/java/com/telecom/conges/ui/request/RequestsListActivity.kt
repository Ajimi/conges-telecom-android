package com.telecom.conges.ui.request

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.telecom.conges.R
import com.telecom.conges.data.models.Request
import com.telecom.conges.data.models.RequestRole
import com.telecom.conges.extensions.*
import com.telecom.conges.ui.request.history.RequestSupervisorItem
import com.telecom.conges.util.State
import com.telecom.conges.util.Tools
import kotlinx.android.synthetic.main.activity_histories.*
import kotlinx.android.synthetic.main.dialog_term_of_services.*
import kotlinx.android.synthetic.main.no_item.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class RequestsListActivity : AppCompatActivity() {

    val requestViewModel: RequestViewModel by viewModel()

    private lateinit var fastItemAdapter: FastItemAdapter<RequestSupervisorItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_histories)
        initToolbar()

        intent.getStringExtra(EXTRA_USER_ID)?.let { userId ->
            requestViewModel.getRole()?.let { requestViewModel.loadRequestsByType(userId) }
        } ?: run {
            requestViewModel.getRole()?.let { requestViewModel.loadRequestsByType(it.toUpperCase()) }
        }

        fastItemAdapter = FastItemAdapter()
        rv.apply {
            layoutManager = LinearLayoutManager(this@RequestsListActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = fastItemAdapter
        }

        requestViewModel.uiHistoriesState.observe(this, Observer {
            val uiModel = it ?: return@Observer
            observeUIState(uiModel, {}, {
                toast(it)
            }, {
                if (it.isEmpty()) {
                    empty_list.visible()
                    nested_content.gone()
                    empty_list.message.text = "Liste des congés est vides"
                } else {
                    empty_list.gone()
                    nested_content.visible()
                    fastItemAdapter.clear()
                    it.map {
                        fastItemAdapter.add(RequestSupervisorItem(it))
                    }
                }
            })
        })

        fastItemAdapter.withOnClickListener { _, _, item, _ ->
            requestViewModel.getRole()?.let { role ->
                when (role.toUpperCase()) {
                    RequestRole.HR.name -> {
                        if (item.request.state == "ACCEPTED" && item.request.isApproved) {
                            toast("Deja Accepter")
                            return@withOnClickListener true
                        }
                    }
                    RequestRole.SUPERVISOR.name -> {
                        if (item.request.isApproved) {
                            toast("Deja Confirmer")
                            return@withOnClickListener true
                        }
                    }
                }
            }

            when (requestViewModel.getRole()?.toUpperCase()) {
                RequestRole.SUPERVISOR.name -> showConfirmationMenu(item.request)
                RequestRole.HR.name -> showAcceptanceMenu(item.request)
                else -> showConfirmationMenu(item.request)
            }
            true
        }
        acceptAndRefuseHandling()

    }

    private fun acceptAndRefuseHandling() {
        requestViewModel.uiConfirmRequestState.observe(this, Observer {
            val uiModel = it ?: return@Observer
            observeUIState(uiModel, {}, {
                toast(it)
            }, {
                updateList()
            })
        })

        requestViewModel.uiAcceptRequestState.observe(this, Observer {
            val uiModel = it ?: return@Observer
            observeUIState(uiModel, {}, {
                toast(it)
            }, {
                updateList()
            })
        })

        requestViewModel.uiRefuseRequestState.observe(this, Observer {
            val uiModel = it ?: return@Observer
            observeUIState(uiModel, {}, {
                toast(it)
            }, {
                updateList()
            })
        })
    }

    private fun updateList() {
        intent.getStringExtra(EXTRA_USER_ID)?.let { userId ->
            requestViewModel.getRole()?.let { requestViewModel.loadRequestsByType(userId) }
        } ?: run {
            requestViewModel.getRole()?.let { requestViewModel.loadRequestsByType(it.toUpperCase()) }
        }
    }

    private fun showAcceptanceMenu(request: Request) {
        val (dialog, layoutParams) = initDialog(request)


        dialog.close_btn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.bt_accept.setOnClickListener {
            requestViewModel.acceptRequest(request.id.toString())
            toast("Acceptation en cours")
            dialog.dismiss()
        }

        dialog.bt_decline.setOnClickListener {
            requestViewModel.refuseRequest(request.id.toString())
            toast("En train de refuser")
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.attributes = layoutParams
    }

    private fun showConfirmationMenu(request: Request) {

        val (dialog, layoutParams) = initDialog(request)

        dialog.titreConges.invisible()
        dialog.bt_accept.text = "Confirmer"
        dialog.title.text = "Confirmation de congés"

        dialog.close_btn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.bt_accept.setOnClickListener {
            requestViewModel.confirmRequest(request.id.toString())
            toast("Button Accept Clicked", Toast.LENGTH_SHORT)
        }

        dialog.bt_decline.setOnClickListener {
            requestViewModel.refuseRequest(request.id.toString())
            toast("Button Decline Clicked", Toast.LENGTH_SHORT)
        }

        dialog.show()
        dialog.window?.attributes = layoutParams
    }

    private fun initDialog(request: Request): Pair<Dialog, WindowManager.LayoutParams> {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_term_of_services)
        dialog.setCancelable(true)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT


        request.user?.let {
            dialog.fullName.text = it.fullName
            val solde = it.solde - it.consumedSolde
            dialog.soldeEmployee.text = "$solde SOLDE RESTANTE "
        }

        dialog.startingDate.text = request.dateStart.FormatedDate().toUpperCase()
        dialog.endingDate.text = request.dateEnd.FormatedDate().toUpperCase()

        val (_, days) = request.dateStart.numberOfDays(request.dateEnd)
        dialog.totalDays.text = if (days + 1 == 1) "1 JOUR" else "${days + 1} JOURS"
        dialog.raison.text = request.reason

        return Pair(dialog, layoutParams)
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
        supportActionBar!!.title = "Demandes"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, R.color.grey_5)
        Tools.setSystemBarLight(this)
    }


    companion object {
        const val EXTRA_USER_ID = "USER_ID"

        fun starterIntent(context: Context): Intent {
            return Intent(context, RequestsListActivity::class.java).apply {
                //            putExtra(EXTRA_PAVILION, pavilion)
            }
        }

        fun starterIntent(context: Context, id: String): Intent {
            return Intent(context, RequestsListActivity::class.java).apply {
                putExtra(EXTRA_USER_ID, id)
            }
        }
    }

}
