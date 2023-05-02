package org.obd.graphs.ui.common

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import org.obd.graphs.R.color
import org.obd.graphs.getContext

fun String.colorize(color: Int, typeface: Int, size: Float) : SpannableString {

    var valText: String? = this
    if (valText == null) {
        valText = ""
    }

    val valSpanString = SpannableString(valText)
    valSpanString.setSpan(RelativeSizeSpan(size), 0, valSpanString.length, 0) // set size
    valSpanString.setSpan(StyleSpan(typeface), 0, valSpanString.length, 0)
    valSpanString.setSpan(
        ForegroundColorSpan(color),
        0,
        valSpanString.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return valSpanString
}

class Colors {

    private val base: List<Int> =  mutableListOf<Int>().apply {
        add(COLOR_CARDINAL)
        add(COLOR_PHILIPPINE_GREEN)
        add(Color.parseColor("#1C3D72"))
        add(Color.parseColor("#BBBBBB"))

        add(Color.parseColor("#C0CA33"))
        add(Color.parseColor("#FF9800"))
        add(Color.parseColor("#F44336"))
        add(Color.parseColor("#4A148C"))
        add(Color.parseColor("#FFFF00"))
        add(Color.parseColor("#42A5F5"))
        add(Color.parseColor("#4DB6AC"))
        add(Color.parseColor("#3F51B5"))

        add(Color.parseColor("#FF6F00"))
        add(Color.parseColor("#E8F5E9"))
        add(Color.parseColor("#757575"))
        add(Color.parseColor("#FFCCBC"))
        add(Color.parseColor("#00C853"))
        add(Color.parseColor("#66BB6A"))
    }

    fun generate(): IntIterator {
        return base.toIntArray().iterator()
    }
}

val COLOR_CARDINAL: Int = color(color.cardinal)
val COLOR_PHILIPPINE_GREEN: Int = color(color.philippine_green)
val COLOR_RAINBOW_INDIGO: Int = color(color.rainbow_indigo)
val COLOR_LIGHT_SHADE_GRAY: Int = color(color.light_shade_gray)
val COLOR_DARK_LIGHT: Int = color(color.gray_light)

fun color(id: Int) = ContextCompat.getColor(getContext()!!, id)
