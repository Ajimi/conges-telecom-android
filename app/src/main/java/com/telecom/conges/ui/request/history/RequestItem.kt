package com.telecom.conges.ui.request.history

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.telecom.conges.R
import com.telecom.conges.data.models.Request
import kotlinx.android.synthetic.main.historique_list_item.view.*


class RequestItem(val request: Request) : AbstractItem<RequestItem, RequestItem.ViewHolder>() {

    //The unique ID for this type of item
    @SuppressLint("ResourceType")
    override fun getType(): Int {
        return 15
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
    class ViewHolder(val view: View) : FastAdapter.ViewHolder<RequestItem>(view) {
        @SuppressLint("NewApi")
        override fun bindView(item: RequestItem, payloads: List<Any>) {
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
                    start_date.text = it.dateStart
                    end_date.text = it.dateEnd
                    type_request.text = it.reason
                }
            }
        }

        override fun unbindView(item: RequestItem) {
//            view.material_drawer_name.text = null
//            view.material_drawer_description.text = null
        }
    }

}

