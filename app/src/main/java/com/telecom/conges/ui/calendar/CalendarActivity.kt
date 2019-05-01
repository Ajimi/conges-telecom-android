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
import ru.cleverpumpkin.calendar.CalendarDate
import ru.cleverpumpkin.calendar.CalendarView
import ru.cleverpumpkin.calendar.utils.getColorInt
import java.util.*

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


    private fun generateCalendarDateIndicators(): List<CalendarView.DateIndicator> {
        val context = this
        val calendar = Calendar.getInstance()

        val indicators = mutableListOf<CalendarView.DateIndicator>()

        repeat(3) {
            indicators += CalendarDateIndicator(
                eventName = "Indicator #1",
                date = CalendarDate(calendar.time),
                color = context.getColorInt(R.color.grey_10)
            )

            indicators += CalendarDateIndicator(
                eventName = "Indicator #2",
                date = CalendarDate(calendar.time),
                color = context.getColorInt(R.color.blue_100)
            )

            indicators += CalendarDateIndicator(
                eventName = "Indicator #3",
                date = CalendarDate(calendar.time),
                color = context.getColorInt(R.color.red_100)
            )

            indicators += CalendarDateIndicator(
                eventName = "Indicator #4",
                date = CalendarDate(calendar.time),
                color = context.getColorInt(R.color.yellow)
            )

            indicators += CalendarDateIndicator(
                eventName = "Indicator #5",
                date = CalendarDate(calendar.time),
                color = context.getColorInt(R.color.green)
            )

            calendar.add(Calendar.DAY_OF_MONTH, 5)
        }

        return indicators
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


