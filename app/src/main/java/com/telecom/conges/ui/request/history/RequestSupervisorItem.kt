package com.telecom.conges.ui.request.history

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.telecom.conges.R
import com.telecom.conges.data.models.Request
import kotlinx.android.synthetic.main.historique_list_item.view.*
import org.joda.time.Days
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import java.util.*


class RequestSupervisorItem(val request: Request) :
    AbstractItem<RequestSupervisorItem, RequestSupervisorItem.ViewHolder>() {

    //The unique ID for this type of item
    @SuppressLint("ResourceType")
    override fun getType(): Int {
        return 16
    }

    //The layout to be used for this type of item
    override fun getLayoutRes(): Int {
        return R.layout.historique_list_item
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    /**
     * our ViewHolder
     */
    class ViewHolder(val view: View) : FastAdapter.ViewHolder<RequestSupervisorItem>(view) {
        @SuppressLint("NewApi")
        override fun bindView(item: RequestSupervisorItem, payloads: List<Any>) {
            with(view) {
                item.request.let {
                    request_state.text = it.state.take(2).toUpperCase()
                    val backgroundResourceId = when (it.state.toLowerCase()) {
                        "refused" -> R.drawable.circle_refused
                        "waiting" -> R.drawable.circle_waiting
                        "accepted" -> R.drawable.circle_accepted
                        else -> R.drawable.circle_waiting
                    }
                    Log.v("STATE", it.state)
                    request_state.background = context.getDrawable(backgroundResourceId)
                    start_date.text = formattedDate(it.dateStart)
                    end_date.text = formattedDate(it.dateEnd)
                    type_request.text = it.reason
                    val (startingDate, days) = numberOfDays(it.dateStart, it.dateEnd)
                    jours.text = if (days <= 1) "Un jour" else "$days jours"
                }
            }
        }

        override fun unbindView(item: RequestSupervisorItem) {
//            view.material_drawer_name.text = null
//            view.material_drawer_description.text = null
        }

        private fun numberOfDays(dateStarts: Date, dateEnds: Date): Pair<LocalDate, Int> {
            val dateStart = LocalDate(dateStarts)
            val dateEnd = LocalDate(dateEnds)
            val days = Days.daysBetween(dateStart, dateEnd)
            return Pair(dateStart, days.days)
        }

        private fun formattedDate(date: Date): String? {
            val localDate = LocalDate(date)
            return DateTimeFormat.forPattern("E dd MMM Y").withLocale(Locale.FRENCH).print(localDate)
        }


    }

}

