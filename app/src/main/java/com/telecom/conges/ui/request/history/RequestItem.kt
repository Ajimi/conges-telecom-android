package com.telecom.conges.ui.request.history

import android.annotation.SuppressLint
import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.telecom.conges.R
import com.telecom.conges.data.models.Request
import kotlinx.android.synthetic.main.sample_item.view.*

class RequestItem(val request: Request) : AbstractItem<RequestItem, RequestItem.ViewHolder>() {

    //The unique ID for this type of item
    @SuppressLint("ResourceType")
    override fun getType(): Int {
        return 15
    }

    //The layout to be used for this type of item
    override fun getLayoutRes(): Int {
        return R.layout.sample_item
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    /**
     * our ViewHolder
     */
    class ViewHolder(val view: View) : FastAdapter.ViewHolder<RequestItem>(view) {
        override fun bindView(item: RequestItem, payloads: List<Any>) {
            with(view) {
                item.request.apply {

                    material_drawer_description.text = id.toString()
                    view.material_drawer_description.text = isApproved.toString()
                }
            }
        }

        override fun unbindView(item: RequestItem) {
            view.material_drawer_name.text = null
            view.material_drawer_description.text = null
        }
    }
}