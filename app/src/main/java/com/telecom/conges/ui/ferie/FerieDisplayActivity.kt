package com.telecom.conges.ui.ferie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.telecom.conges.R
import com.telecom.conges.adapter.DaysOffAdapter
import com.telecom.conges.extensions.observeUIState
import com.telecom.conges.extensions.toast
import com.telecom.conges.util.Tools
import kotlinx.android.synthetic.main.activity_ferie_display.*
import kotlinx.android.synthetic.main.activity_request.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class FerieDisplayActivity : AppCompatActivity() {

    val daysOffViewModel: DaysOffViewModel by viewModel()
    lateinit var mDaysOffAdapter: DaysOffAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ferie_display)
        initToolbar()
        hideSoftKeyboard()
        initComponent()
        daysOffViewModel.uiDaysOffState.observe(this, Observer {
            val uiModel = it ?: return@Observer
            observeUIState(uiModel, {}, {
                toast(it)
            }, {
                mDaysOffAdapter.addData(it)
            })
        })
    }

    private fun initComponent() {

        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.setHasFixedSize(true)
        mDaysOffAdapter = DaysOffAdapter(this, mutableListOf())
        recyclerview.adapter = mDaysOffAdapter
        mDaysOffAdapter.setOnMoreButtonClickListener { view, daysOff, item, position ->
            daysOffViewModel.deleteDaysOff(daysOff.id)
            mDaysOffAdapter.updateAt(position)
        }

        add.setOnClickListener {
            startActivity(FerieCreateActivity.starterIntent(this@FerieDisplayActivity))
        }
    }

    override fun onStart() {
        super.onStart()
        daysOffViewModel.loadAllDaysOff()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, com.telecom.conges.R.color.grey_5)
        Tools.setSystemBarLight(this)
    }

    fun hideSoftKeyboard() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
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

        fun starterIntent(context: Context): Intent {
            return Intent(context, FerieDisplayActivity::class.java).apply {
                //            putExtra(EXTRA_PAVILION, pavilion)
            }
        }
    }

}
