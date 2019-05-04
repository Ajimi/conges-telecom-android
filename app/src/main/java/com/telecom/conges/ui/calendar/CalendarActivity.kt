package com.telecom.conges.ui.calendar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.esprit.core.extensions.observeUIState
import com.telecom.conges.R
import com.telecom.conges.extensions.toast
import com.telecom.conges.util.Tools
import kotlinx.android.synthetic.main.activity_calendar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.cleverpumpkin.calendar.CalendarView

class CalendarActivity : AppCompatActivity() {

    val calendarViewModel: CalendarViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        initToolbar()
//        calendarView.datesIndicators = generateCalendarDateIndicators()

        calendarView.onDateClickListener = { date ->
            val dateIndicators = calendarView.getDateIndicators(date)
                .filterIsInstance<CalendarDateIndicator>()
                .toTypedArray()

            if (dateIndicators.isNotEmpty()) {
                val builder = AlertDialog.Builder(this)
                    .setTitle("$date")
                    .setAdapter(DateIndicatorsDialogAdapter(this, dateIndicators), null)

                val dialog = builder.create()
                dialog.show()
            }
        }

        calendarViewModel.loadIndicator()

        if (savedInstanceState == null) {
            calendarView.setupCalendar(selectionMode = CalendarView.SelectionMode.NON)
        }


        calendarViewModel.uiIndicatorsState.observe(this, androidx.lifecycle.Observer {
            val uiModel = it ?: return@Observer
            observeUIState(uiModel, {}, {
                toast(it)
            }, { daysOff ->
                calendarView.datesIndicators = calendarViewModel.indicators
                Log.v("ViewModel", calendarViewModel.indicators.toString())
            })
        })
    }


    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, com.telecom.conges.R.color.grey_5)
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

        fun starterIntent(context: Context): Intent {
            return Intent(context, CalendarActivity::class.java).apply {
                //            putExtra(EXTRA_PAVILION, pavilion)
            }
        }
    }

}


