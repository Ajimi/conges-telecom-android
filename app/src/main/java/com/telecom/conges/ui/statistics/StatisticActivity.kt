package com.telecom.conges.ui.statistics

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.telecom.conges.util.Tools
import kotlinx.android.synthetic.main.activity_statistic.*


class StatisticActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.telecom.conges.R.layout.activity_statistic)
        initToolbar()
        chart1.apply {

            setUsePercentValues(true)
            centerText = generateCenterSpannableText()
            description.isEnabled = false
            setExtraOffsets(5f, 10f, 5f, 5f)
            dragDecelerationFrictionCoef = 0.95f
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(110)
            holeRadius = 58f
            transparentCircleRadius = 61f
            setDrawCenterText(true)
            rotationAngle = 0f
            isHighlightPerTapEnabled = true
            animateY(1400, Easing.EaseInOutBack)
        }
        setData(4, 10f)

    }


    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Tools.setSystemBarColor(this, com.telecom.conges.R.color.grey_5)
        Tools.setSystemBarLight(this)
    }

    protected val parties = arrayOf(
        "Maladie",
        "Obligation Militaire",
        "Autre",
        "Congés parental",
        "Congé Déces",
        "Congés Sans solde"
    )

    private fun setData(count: Int, range: Float) {
        val entries = mutableListOf<PieEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0 until count) {
            entries.add(
                PieEntry(
                    (Math.random() * range + range / 5).toFloat(),
                    parties[i % parties.size],
                    resources.getDrawable(com.telecom.conges.R.drawable.ic_add)
                )
            )
        }

        val dataSet = PieDataSet(entries, "Congés").apply {
            setDrawIcons(false)

            sliceSpace = 3f
            iconsOffset = MPPointF(0f, 40f)
            selectionShift = 5f
            valueTextColor = Color.GRAY
        }


        // add a lot of colors

        val colors = mutableListOf<Int>()

        for (c in ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS)
            colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS)
            colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())

        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);

        val data = PieData(dataSet).apply {
            setValueFormatter(PercentFormatter(chart1))
            setValueTextSize(11f)
            setValueTextColor(Color.GRAY)
        }
        chart1.apply {
            this.data = data
            highlightValues(null)
            invalidate()
        }
    }


    private fun generateCenterSpannableText() = SpannableString("Statistique\n par mois").apply {
        //        setSpan(RelativeSizeSpan(1.7f), 0, 14, 0)
//        setSpan(StyleSpan(Typeface.NORMAL), 14, length - 15, 0)
//        setSpan(ForegroundColorSpan(Color.GRAY), 14, length - 15, 0)
//        setSpan(RelativeSizeSpan(.8f), 14, length - 15, 0)
//        setSpan(StyleSpan(Typeface.ITALIC), length - 14, length, 0)
//        setSpan(ForegroundColorSpan(ColorTemplate.getHoloBlue()), length - 14, length, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }

    companion object {

        fun starterIntent(context: Context): Intent {
            return Intent(context, StatisticActivity::class.java).apply {
                //            putExtra(EXTRA_PAVILION, pavilion)
            }
        }

    }

}
