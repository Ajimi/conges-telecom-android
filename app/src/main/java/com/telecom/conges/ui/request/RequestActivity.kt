package com.telecom.conges.ui.request

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.telecom.conges.R
import com.telecom.conges.data.models.Request
import com.telecom.conges.extensions.invisible
import com.telecom.conges.extensions.observeUIState
import com.telecom.conges.extensions.toast
import com.telecom.conges.util.State
import com.telecom.conges.util.Tools
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.android.synthetic.main.activity_request.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class RequestActivity : AppCompatActivity() {

    var selectedButton: Button? = null
    val requestViewModel: RequestViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)
        initToolbar()
        initComponent()
        hideSoftKeyboard()
        requestViewModel.uiCreateRequestState.observe(this, androidx.lifecycle.Observer {
            val uiModel = it ?: return@Observer
            observeUIState(uiModel, {}, {
                toast(it)
            }, {
                finish()
            })
        })
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Demande de congés"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, com.telecom.conges.R.color.grey_5)
        Tools.setSystemBarLight(this)
    }


    private fun initComponent() {

        val list = listOf<String>(
            "Maladie",
            "Congé Déces",
            "Congés sans solde",
            "Obligations Militaires",
            "Congés parental",
            "Autre"
        )

        val buttons = list.map {
            createButton(it)
        }

        buttons.map {
            raison.addView(it)
        }

        seek_bar.setOnSeekbarChangeListener { minValue ->
            price_min.text = "$minValue"
        }

        // TODO get Max Solde
        val currentCalender = Calendar.getInstance()

        seek_bar.setOnSeekbarFinalValueListener {
            updateEndDate(it)
        }
        bt_start_date.apply {
            setOnClickListener {
                dialogDatePickerLight(it)
            }
            setText(Tools.getFormattedDateShort(currentCalender.timeInMillis))
        }

        bt_end_date.apply {
            setText(Tools.getFormattedDateShort(currentCalender.timeInMillis))
        }

        confirm.setOnClickListener {
            if (selectedButton != null && selectedButton?.isSelected == true) {
                val startDate = Tools.getDateShort(bt_start_date.text.toString())
                val endDate = Tools.getDateShort(bt_end_date.text.toString())
                sendRequest(selectedButton?.text.toString(), startDate, endDate)
            } else {
                toast("S'il vous selectioner une raison")
            }
        }

        clear.setOnClickListener {
            selectedButton?.isSelected = false
            seek_bar.minValue = 1f
            with(Tools.getFormattedDateShort(currentCalender.timeInMillis)) {
                bt_start_date.setText(this)
                bt_end_date.setText(this)
            }
        }


        val restDays = requestViewModel.getMaxSolde().toFloat()
        seek_bar.maxValue = restDays
        if (restDays < 1) {
            seek_bar.invisible()
            price_min.text = "Désolé votre solde et expiré"
            price_min.setTextColor(ContextCompat.getColor(this, R.color.red_100))
            clear.isEnabled = false
            confirm.isEnabled = false
            bt_end_date.isEnabled = false
            bt_start_date.isEnabled = false
            buttons.map {
                it.isEnabled = false
            }
        }
    }

    private fun sendRequest(reason: String, startDate: Date, endDate: Date) {
        val request = Request(endDate, startDate, 0, reason, State.WAITING.name, false)
        requestViewModel.createRequest(request)
    }

    private fun updateEndDate(days: Number) {
        val startDate = bt_start_date.text
        val time = Calendar.getInstance().apply {
            time = Tools.getDateShort(startDate.toString())
            add(Calendar.DATE, days.toInt())
        }.let {
            Tools.getFormattedDateShort(it.timeInMillis)
        }
        bt_end_date.setText(time)
    }

    @SuppressLint("NewApi")
    private fun createButton(name: String) = Button(this).apply {
        isSelected = false
        text = name
        setTextAppearance(resources.getIdentifier("TextAppearance.AppCompat.Title", "style", packageName))
        setTextColor(getColor(com.telecom.conges.R.color.grey_90))
        setOnClickListener {
            btToggleClick(it)
        }
        isAllCaps = false
        textSize = 15f
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = 15
        }
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
            updateEndDate(seek_bar.selectedMinValue)
        }
        val datePicker = DatePickerDialog.newInstance(
            dateListener,
            currentCalender.get(Calendar.YEAR),
            currentCalender.get(Calendar.MONTH),
            currentCalender.get(Calendar.DAY_OF_MONTH)
        )
        //set dark light
        datePicker.isThemeDark = false
        datePicker.accentColor = resources.getColor(R.color.colorPrimary)
        datePicker.minDate = currentCalender
        datePicker.show(supportFragmentManager, "Start Date")

    }


    fun btToggleClick(view: View) {

        if (view is Button) {

            if (view.isSelected) {
                view.setTextColor(resources.getColor(com.telecom.conges.R.color.grey_40))
            } else {
                view.setTextColor(Color.WHITE)
            }
            view.isSelected = !view.isSelected
            selectedButton?.let {
                if (it != view) {
                    it.setTextColor(resources.getColor(com.telecom.conges.R.color.grey_40))
                    it.isSelected = false
                }
            }
            selectedButton = view

        }

    }


    fun hideSoftKeyboard() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        Tools.changeMenuIconColor(menu, resources.getColor(R.color.grey_60))
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else {
            Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }


    companion object {

        fun starterIntent(context: Context): Intent {
            return Intent(context, RequestActivity::class.java).apply {}
        }
    }

}
