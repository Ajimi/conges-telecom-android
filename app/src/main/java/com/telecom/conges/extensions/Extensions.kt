package com.telecom.conges.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout

/** Convenience for callbacks/listeners whose return value indicates an event was consumed. */
inline fun consume(f: () -> Unit): Boolean {
    f()
    return true
}

val <T> T.exhaustive: T
    get() = this


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}


fun Context?.toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) =
    this?.let { Toast.makeText(it, text, duration).show() }

fun Fragment?.toast(text: CharSequence, duration: Int = Toast.LENGTH_LONG) =
    this?.let { activity.toast(text, duration) }


fun Context.getDrawables(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

inline val Context.connectivityManager: ConnectivityManager?
    get() = getSystemService(CONNECTIVITY_SERVICE) as? ConnectivityManager

val TabLayout.determineTabMode: Int
    get() {
        val slidingTabStrip = getChildAt(0) as LinearLayout
        val childCount = slidingTabStrip.childCount

        // NOTE: slidingTabStrip.getMeasuredWidth() method does not return correct width!
        // Need to measure each tabs and calculate the sum of them.

        val tabLayoutWidth = measuredWidth - paddingLeft - paddingRight
        val tabLayoutHeight = measuredHeight - paddingTop - paddingBottom

        if (childCount == 0) {
            return TabLayout.MODE_FIXED
        }

        var stripWidth = 0
        var maxWidthTab = 0
        val tabHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(tabLayoutHeight, View.MeasureSpec.EXACTLY)

        for (i in 0 until childCount) {
            val tabView = slidingTabStrip.getChildAt(i)
            tabView.measure(View.MeasureSpec.UNSPECIFIED, tabHeightMeasureSpec)
            val tabWidth = tabView.measuredWidth
            stripWidth += tabWidth
            maxWidthTab = Math.max(maxWidthTab, tabWidth)
        }

        return if (stripWidth < tabLayoutWidth && maxWidthTab < tabLayoutWidth / childCount)
            TabLayout.MODE_FIXED
        else
            TabLayout.MODE_SCROLLABLE
    }

@SuppressLint("ResourceAsColor")
fun Snackbar.action(text: String, @ColorRes color: Int? = null, listener: (View) -> Unit) {
    setAction(text, listener)
    color?.let { setActionTextColor(color) }
}

inline fun Context.hasNetworkConnection(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetworkInfo?.isConnected ?: false
}

inline fun Context.hasWifiConnection(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm.activeNetworkInfo?.let { it.type == ConnectivityManager.TYPE_WIFI } ?: false
}
