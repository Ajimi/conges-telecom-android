package com.telecom.conges.ui.ferie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.esprit.core.extensions.observeUIState
import com.telecom.conges.R
import com.telecom.conges.data.models.DaysOff
import com.telecom.conges.extensions.toast
import com.telecom.conges.util.Tools
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_ferie_create.*
import kotlinx.android.synthetic.main.activity_request.bt_end_date
import kotlinx.android.synthetic.main.activity_request.bt_start_date
import kotlinx.android.synthetic.main.activity_request.confirm
import kotlinx.android.synthetic.main.activity_request.toolbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class FerieCreateActivity : AppCompatActivity() {

    val daysOffViewModel: DaysOffViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ferie_create)
        initToolbar()
        hideSoftKeyboard()
        initComponent()
        daysOffViewModel.uiCreateDaysOffState.observe(this, Observer {
            val uiModel = it ?: return@Observer
            observeUIState(uiModel, {}, {
                toast(it)
            }, { daysOff ->
                toast(daysOff.toString())
                finish()
            })
        })
    }

    private fun initComponent() {
        val currentCalender = Calendar.getInstance()
        bt_start_date.apply {
            setOnClickListener {
                dialogDatePickerLight(it)
            }
            setText(Tools.getFormattedDateShort(currentCalender.timeInMillis))
        }
        bt_end_date.apply {
            setOnClickListener {
                dialogDatePickerLight(it)
            }
            setText(Tools.getFormattedDateShort(currentCalender.timeInMillis))
        }

        confirm.setOnClickListener {
            val startDate = bt_start_date.text.toString()
            val endDate = bt_end_date.text.toString()
            val name = name_day_off.text.toString()
            DaysOff("", name, startDate, endDate).apply {
                daysOffViewModel.createDaysOff(this)
            }
        }

    }


    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, com.telecom.conges.R.color.grey_5)
        Tools.setSystemBarLight(this)
    }

    private fun dialogDatePickerLight(v: View) {
        val currentCalender = Calendar.getInstance()
        val dateListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val date = calendar.timeInMillis
            (v as EditText).setText(Tools.getFormattedDateShort(date))
        }
        val datePicker = DatePickerDialog.newInstance(
            dateListener,
            currentCalender.get(Calendar.YEAR),
            currentCalender.get(Calendar.MONTH),
            currentCalender.get(Calendar.DAY_OF_MONTH)
        )
        //set dark light
        datePicker.isThemeDark = false;
        datePicker.accentColor = getResources().getColor(R.color.colorPrimary);
        datePicker.minDate = currentCalender;
        datePicker.show(supportFragmentManager, "Start Date");

    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }

    fun hideSoftKeyboard() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    companion object {

        fun starterIntent(context: Context): Intent {
            return Intent(context, FerieCreateActivity::class.java).apply {
                //            putExtra(EXTRA_PAVILION, pavilion)
            }
        }
    }

}
