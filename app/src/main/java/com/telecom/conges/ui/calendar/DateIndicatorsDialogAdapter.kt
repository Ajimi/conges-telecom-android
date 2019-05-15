package com.telecom.conges.ui.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.telecom.conges.R
import com.telecom.conges.extensions.gone
import com.telecom.conges.extensions.visible
import kotlinx.android.synthetic.main.item_dialog_date_indicator.view.*

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
        view.color_view.setBackgroundColor(event.color)
        view.event_name_view.text = event?.eventName
        event?.name?.let {

            view.event_username.visible()
            view.event_username.text = it
        } ?: run {
            view.event_username.gone()
        }

        return view
    }
}