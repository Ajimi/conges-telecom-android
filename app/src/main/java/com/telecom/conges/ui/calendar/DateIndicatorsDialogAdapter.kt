package com.telecom.conges.ui.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.telecom.conges.R

class DateIndicatorsDialogAdapter(
    context: Context,
    events: Array<CalendarDateIndicator>
) : ArrayAdapter<CalendarDateIndicator>(context, 0, events) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = if (convertView == null) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_dialog_date_indicator, parent, false)
        } else {
            convertView
        }

        val event = getItem(position)
        view.findViewById<View>(R.id.color_view).setBackgroundColor(event.color)
        view.findViewById<TextView>(R.id.event_name_view).text = event.eventName

        return view
    }
}