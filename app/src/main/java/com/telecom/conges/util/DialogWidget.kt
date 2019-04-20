package com.telecom.conges.util

import android.content.Context
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.telecom.conges.R
import kotlinx.android.synthetic.main.dialog_layout.view.*

class DialogWidget {

    operator fun invoke(context: Context, title: String?, message: String, withProgress: Boolean): MaterialDialog {
        val dialog = MaterialDialog(context)

        if (title != null)
            dialog.title(null, title)

        if (withProgress) {
            dialog.customView(R.layout.dialog_layout)
            val customView: View = dialog.getCustomView()
            customView.dialogMessage.text = message
        } else {
            dialog.message(text = message)
        }

        return dialog
    }

}