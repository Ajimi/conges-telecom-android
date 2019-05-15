package com.telecom.conges.ui.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.telecom.conges.R
import com.telecom.conges.data.Result
import com.telecom.conges.data.models.DaysOff
import com.telecom.conges.data.models.Request
import com.telecom.conges.data.services.auth.AuthenticationHelper
import com.telecom.conges.data.services.daysoff.DaysOffHelper
import com.telecom.conges.data.services.request.RequestHelper
import com.telecom.conges.extensions.UiState
import com.telecom.conges.extensions.emitUiState
import com.telecom.conges.util.Event
import kotlinx.coroutines.*
import org.joda.time.Days
import org.joda.time.LocalDate
import ru.cleverpumpkin.calendar.CalendarDate
import java.util.*
import kotlin.random.Random

class CalendarViewModel(
    val requestHelper: RequestHelper,
    val authenticationHelper: AuthenticationHelper,
    val daysOffHelper: DaysOffHelper,
    val context: Context
) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _uiIndicatorsState = MutableLiveData<UiState<String>>()
    val uiIndicatorsState: LiveData<UiState<String>>
        get() = _uiIndicatorsState

    val indicators: MutableList<CalendarDateIndicator> = mutableListOf()
    val setOfColors: MutableSet<Int> = mutableSetOf()
    var colorsList: IntArray = intArrayOf()

    fun loadIndicator(): Unit {
        launchGetIndicators()
    }

    @SuppressLint("NewApi")
    private fun launchGetIndicators() =
        uiScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _uiIndicatorsState.value = emitUiState(showProgress = true)
                colorsList = context.resources.getIntArray(R.array.colors)
            }
            val resultDaysOff = daysOffHelper.getAllDaysOff()
            val resultRequest = requestHelper.getAllRequest()


            when (resultDaysOff) {
                is Result.Success -> {
                    withContext(Dispatchers.Main) {
                        val daysOffs = resultDaysOff.data
                        val calendars = parseDatesDayOff(daysOffs)
                        indicators.addAll(calendars)
                        _uiIndicatorsState.value = emitUiState(showSuccess = Event(""))
                    }

                }
                is Result.Error -> withContext(Dispatchers.Main) {
                    _uiIndicatorsState.value = emitUiState(showError = Event(""))
                }
            }
            when (resultRequest) {
                is Result.Success -> {
                    withContext(Dispatchers.Main) {
                        val requestResult = resultRequest.data
                        val calendars = parseDatesRequest(requestResult)
                        indicators.addAll(calendars)
                        _uiIndicatorsState.value = emitUiState(showSuccess = Event(""))
                    }
                }
                is Result.Error -> withContext(Dispatchers.Main) {
                    withContext(Dispatchers.Main) {
                        _uiIndicatorsState.value = emitUiState(showError = Event(""))
                    }
                }
            }


        }

    @SuppressLint("NewApi")
    private fun parseDatesDayOff(data: List<DaysOff>): List<CalendarDateIndicator> {
        return data.map {
            val (dateStart, days) = numberOfDays(it.dateStart, it.dateEnd)
            val dates = (0..days).map {
                val currentDate = dateStart.plusDays(it)
                currentDate.toDate()
            }
            Pair(Pair(it, dates), getUniqueColor())
        }.flatMap { pair ->
            pair.first.second.map {
                CalendarDateIndicator(
                    CalendarDate(it),
                    pair.second,
                    pair.first.first.name
                )
            }
        }.onEach {
            Log.v("Date Off Date:", "$it")
        }
    }

    @SuppressLint("NewApi")
    private fun parseDatesRequest(data: List<Request>): List<CalendarDateIndicator> {
        return data.filter { it.isApproved }.map { request ->
            val (dateStart, days) = numberOfDays(request.dateStart, request.dateEnd)
            val dates = (0..days).map {
                val currentDate = dateStart.plusDays(it)
                currentDate.toDate()
            }
            Pair(Pair(request, dates), getUniqueColor())
        }.flatMap { pair ->
            pair.first.second.map {
                CalendarDateIndicator(
                    CalendarDate(it),
                    pair.second,
                    pair.first.first.reason,
                    pair.first.first.user?.firstname?.capitalize() + " " + pair.first.first.user?.lastname?.capitalize()

                )
            }
        }.onEach {

            Log.v("Request:", "${it.eventName} date : ${it.date}")
        }
    }

    @SuppressLint("NewApi")
    private fun getUniqueColor(): Int {

        var colorResource: Int
        do {
            val number = Random.nextInt(0, colorsList.size - 1)
            colorResource = getColorsList()[number]
        } while (setOfColors.contains(colorResource))
        setOfColors.add(colorResource)
        return ContextCompat.getColor(context, colorResource)
    }

    @SuppressLint("NewApi")
    private fun numberOfDays(dateStarts: Date, dateEnds: Date): Pair<LocalDate, Int> {
        val dateStart = LocalDate(dateStarts)
        val dateEnd = LocalDate(dateEnds)
//        val dateStart = LocalDate.parse(dateStarts, DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z"))
//        val dateEnd = LocalDate.parse(dateEnds, DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z"))
        val days = Days.daysBetween(dateStart, dateEnd)
        return Pair(dateStart, days.days)
    }


    private fun getColorsList() = mutableListOf<Int>(
        R.color.colorPrimaryDark,
        R.color.colorPrimaryLight,
        R.color.colorAccent,
        R.color.colorAccentDark,
        R.color.colorAccentLight,
        R.color.colortext,
        R.color.colorhint,
        R.color.lightgray,
        R.color.deeppurple,
        R.color.yellow,
        R.color.green,
        R.color.darkblue,
        R.color.testcolorblue,
        R.color.pink,
        R.color.overlay_dark_10,
        R.color.overlay_dark_20,
        R.color.overlay_dark_30,
        R.color.overlay_dark_40,
        R.color.overlay_dark_50,
        R.color.overlay_dark_60,
        R.color.overlay_dark_70,
        R.color.overlay_dark_80,
        R.color.overlay_dark_90,
        R.color.overlay_light_10,
        R.color.overlay_light_20,
        R.color.overlay_light_30,
        R.color.overlay_light_40,
        R.color.overlay_light_50,
        R.color.overlay_light_60,
        R.color.overlay_light_70,
        R.color.overlay_light_80,
        R.color.overlay_light_90,
        R.color.grey_3,
        R.color.grey_5,
        R.color.grey_10,
        R.color.grey_20,
        R.color.grey_40,
        R.color.grey_60,
        R.color.grey_80,
        R.color.grey_90,
        R.color.grey_95,
        R.color.grey_100_,
        R.color.red_50,
        R.color.red_100,
        R.color.red_200,
        R.color.red_300,
        R.color.red_400,
        R.color.red_500,
        R.color.red_600,
        R.color.red_700,
        R.color.red_800,
        R.color.red_900,
        R.color.red_A100,
        R.color.red_A200,
        R.color.red_A400,
        R.color.red_A700,
        R.color.deep_purple_50,
        R.color.deep_purple_100,
        R.color.deep_purple_200,
        R.color.deep_purple_300,
        R.color.deep_purple_400,
        R.color.deep_purple_500,
        R.color.deep_purple_600,
        R.color.deep_purple_700,
        R.color.deep_purple_800,
        R.color.deep_purple_900,
        R.color.deep_purple_A100,
        R.color.deep_purple_A200,
        R.color.deep_purple_A400,
        R.color.deep_purple_A700,
        R.color.light_blue_50,
        R.color.light_blue_100,
        R.color.light_blue_200,
        R.color.light_blue_300,
        R.color.light_blue_400,
        R.color.light_blue_500,
        R.color.light_blue_600,
        R.color.light_blue_700,
        R.color.light_blue_800,
        R.color.light_blue_900,
        R.color.light_blue_A100,
        R.color.light_blue_A200,
        R.color.light_blue_A400,
        R.color.light_blue_A700,
        R.color.green_50,
        R.color.green_100,
        R.color.green_200,
        R.color.green_300,
        R.color.green_400,
        R.color.green_500,
        R.color.green_600,
        R.color.green_700,
        R.color.green_800,
        R.color.green_900,
        R.color.green_A100,
        R.color.green_A200,
        R.color.green_A400,
        R.color.green_A700,
        R.color.yellow_50,
        R.color.yellow_100,
        R.color.yellow_200,
        R.color.yellow_300,
        R.color.yellow_400,
        R.color.yellow_500,
        R.color.yellow_600,
        R.color.yellow_700,
        R.color.yellow_800,
        R.color.yellow_900,
        R.color.yellow_A100,
        R.color.yellow_A200,
        R.color.yellow_A400,
        R.color.yellow_A700,
        R.color.deep_orange_50,
        R.color.deep_orange_100,
        R.color.deep_orange_200,
        R.color.deep_orange_300,
        R.color.deep_orange_400,
        R.color.deep_orange_500,
        R.color.deep_orange_600,
        R.color.deep_orange_700,
        R.color.deep_orange_800,
        R.color.deep_orange_900,
        R.color.deep_orange_A100,
        R.color.deep_orange_A200,
        R.color.deep_orange_A400,
        R.color.deep_orange_A700,
        R.color.blue_grey_50,
        R.color.blue_grey_100,
        R.color.blue_grey_200,
        R.color.blue_grey_300,
        R.color.blue_grey_400,
        R.color.blue_grey_500,
        R.color.blue_grey_600,
        R.color.blue_grey_700,
        R.color.blue_grey_800,
        R.color.blue_grey_900,
        R.color.blue_grey_800_overlay,
        R.color.pink_50,
        R.color.pink_100,
        R.color.pink_200,
        R.color.pink_300,
        R.color.pink_400,
        R.color.pink_500,
        R.color.pink_600,
        R.color.pink_700,
        R.color.pink_800,
        R.color.pink_900,
        R.color.pink_A100,
        R.color.pink_A200,
        R.color.pink_A400,
        R.color.pink_A700,
        R.color.indigo_50,
        R.color.indigo_100,
        R.color.indigo_200,
        R.color.indigo_300,
        R.color.indigo_400,
        R.color.indigo_500,
        R.color.indigo_600,
        R.color.indigo_700,
        R.color.indigo_800,
        R.color.indigo_900,
        R.color.indigo_A100,
        R.color.indigo_A200,
        R.color.indigo_A400,
        R.color.indigo_A700,
        R.color.indigo_800_overlay,
        R.color.cyan_50,
        R.color.cyan_100,
        R.color.cyan_200,
        R.color.cyan_300,
        R.color.cyan_400,
        R.color.cyan_500,
        R.color.cyan_600,
        R.color.cyan_700,
        R.color.cyan_800,
        R.color.cyan_900,
        R.color.cyan_A100,
        R.color.cyan_A200,
        R.color.cyan_A400,
        R.color.cyan_A700,
        R.color.cyan_800_overlay,
        R.color.light_green_50,
        R.color.light_green_100,
        R.color.light_green_200,
        R.color.light_green_300,
        R.color.light_green_400,
        R.color.light_green_500,
        R.color.light_green_600,
        R.color.light_green_700,
        R.color.light_green_800,
        R.color.light_green_900,
        R.color.light_green_A100,
        R.color.light_green_A200,
        R.color.light_green_A400,
        R.color.light_green_A700,
        R.color.amber_50,
        R.color.amber_100,
        R.color.amber_200,
        R.color.amber_300,
        R.color.amber_400,
        R.color.amber_500,
        R.color.amber_600,
        R.color.amber_700,
        R.color.amber_800,
        R.color.amber_900,
        R.color.amber_A100,
        R.color.amber_A200,
        R.color.amber_A400,
        R.color.amber_A700,
        R.color.brown_50,
        R.color.brown_100,
        R.color.brown_200,
        R.color.brown_300,
        R.color.brown_400,
        R.color.brown_500,
        R.color.brown_600,
        R.color.brown_700,
        R.color.brown_800,
        R.color.brown_900,
        R.color.purple_50,
        R.color.purple_100,
        R.color.purple_200,
        R.color.purple_300,
        R.color.purple_400,
        R.color.purple_500,
        R.color.purple_600,
        R.color.purple_700,
        R.color.purple_800,
        R.color.purple_900,
        R.color.purple_A100,
        R.color.purple_A200,
        R.color.purple_A400,
        R.color.purple_A700,
        R.color.blue_50,
        R.color.blue_100,
        R.color.blue_200,
        R.color.blue_300,
        R.color.blue_400,
        R.color.blue_500,
        R.color.blue_600,
        R.color.blue_700,
        R.color.blue_800,
        R.color.blue_900,
        R.color.blue_A100,
        R.color.blue_A200,
        R.color.blue_A400,
        R.color.blue_A700,
        R.color.teal_50,
        R.color.teal_100,
        R.color.teal_200,
        R.color.teal_300,
        R.color.teal_400,
        R.color.teal_500,
        R.color.teal_600,
        R.color.teal_700,
        R.color.teal_800,
        R.color.teal_900,
        R.color.teal_A100,
        R.color.teal_A200,
        R.color.teal_A400,
        R.color.teal_A700,
        R.color.lime_50,
        R.color.lime_100,
        R.color.lime_200,
        R.color.lime_300,
        R.color.lime_400,
        R.color.lime_500,
        R.color.lime_600,
        R.color.lime_700,
        R.color.lime_800,
        R.color.lime_900,
        R.color.lime_A100,
        R.color.lime_A200,
        R.color.lime_A400,
        R.color.lime_A700,
        R.color.orange_50,
        R.color.orange_100,
        R.color.orange_200,
        R.color.orange_300,
        R.color.orange_400,
        R.color.orange_500,
        R.color.orange_600,
        R.color.orange_700,
        R.color.orange_800,
        R.color.orange_900,
        R.color.orange_A100,
        R.color.orange_A200,
        R.color.orange_A400,
        R.color.orange_A700,
        R.color.grey_50,
        R.color.grey_100,
        R.color.grey_200,
        R.color.grey_300,
        R.color.grey_400,
        R.color.grey_500,
        R.color.grey_600,
        R.color.grey_700,
        R.color.grey_800,
        R.color.grey_900,
        R.color.grey_1000,
        R.color.magnitude1,
        R.color.magnitude2,
        R.color.magnitude3,
        R.color.magnitude4,
        R.color.magnitude5,
        R.color.magnitude6,
        R.color.magnitude7,
        R.color.magnitude8,
        R.color.magnitude9,
        R.color.magnitude10plus,
        R.color.textColorEarthquakeDetails,
        R.color.textColorEarthquakeLocation
    )
}