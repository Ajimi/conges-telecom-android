package com.telecom.conges.ui.request

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.telecom.conges.R
import com.telecom.conges.adapter.AdapterListUser
import com.telecom.conges.extensions.gone
import com.telecom.conges.extensions.observeUIState
import com.telecom.conges.extensions.toast
import com.telecom.conges.extensions.visible
import com.telecom.conges.ui.account.AccountActivity
import com.telecom.conges.util.ItemAnimation
import kotlinx.android.synthetic.main.activity_list_user.*
import kotlinx.android.synthetic.main.no_item.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserListActivity : AppCompatActivity() {
    val requestViewModel: RequestViewModel by viewModel()

    private var parent_view: View? = null

    private lateinit var mAdapter: AdapterListUser
    private val animation_type = ItemAnimation.BOTTOM_UP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)
        parent_view = findViewById(android.R.id.content)

        initToolbar()
        initComponent()

        requestViewModel.getRole()?.let { requestViewModel.loadUsersByType(it.toUpperCase()) }
        requestViewModel.uiUsersState.observe(this, Observer {
            val uiModel = it ?: return@Observer
            observeUIState(uiModel, {}, {
                toast(it)
            }, {
                if (it.isEmpty()) {
                    empty_list.visible()
                    recyclerView.gone()
                    empty_list.message.text = "vous n'avez pas encore des employees"
                } else {
                    empty_list.gone()
                    recyclerView.visible()
                    mAdapter.addData(it)
                }
            })
        })

    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle("Employees")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initComponent() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        setAdapter()
    }

    private fun setAdapter() {
        //set data and list adapter
        mAdapter = AdapterListUser(this, mutableListOf(), animation_type)
        recyclerView.adapter = mAdapter

        // on item list clicked
        mAdapter.setOnItemClickListener { view, user, position ->
            startActivity(AccountActivity.starterIntent(this@UserListActivity, userId = user.id.toString()))
        }

        mAdapter.setOnItemLongClickListener { view, user, position ->
            startActivity(RequestsListActivity.starterIntent(this@UserListActivity, user.id.toString()))
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }

    companion object {

        private val ANIMATION_TYPE = arrayOf("Bottom Up", "Fade In", "Left to Right", "Right to Left")
        fun starterIntent(context: Context): Intent {
            return Intent(context, UserListActivity::class.java).apply {
                //            putExtra(EXTRA_PAVILION, pavilion)
            }
        }

    }
}