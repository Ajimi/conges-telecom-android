package com.telecom.conges.ui.calendar

import ru.cleverpumpkin.calendar.CalendarDate
import ru.cleverpumpkin.calendar.CalendarView

class CalendarDateIndicator(
    override val date: CalendarDate,
    override val color: Int,
    val eventName: String,
    val name: String? = ""
) : CalendarView.DateIndicator