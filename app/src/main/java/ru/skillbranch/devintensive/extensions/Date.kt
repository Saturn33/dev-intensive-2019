package ru.skillbranch.devintensive.extensions

import ru.skillbranch.devintensive.utils.Utils
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern:String= "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }

    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val diff = this.time - date.time
    return when (diff)
    {
        in -1* SECOND..1* SECOND -> "только что"
        in 1* SECOND..45* SECOND -> "через несколько секунд"
        in -45* SECOND..-1* SECOND -> "несколько секунд назад"
        in 45* SECOND..75* SECOND -> "через минуту"
        in -75* SECOND..-45* SECOND -> "минуту назад"
        in 75* SECOND..45* MINUTE -> {
            val value = (diff / MINUTE).toInt().absoluteValue
            "через $value ${Utils.declOfNum(value, arrayOf("минуту", "минуты", "минут"))}"
        }
        in -45* MINUTE..-75* SECOND -> {
            val value = (diff / MINUTE).toInt().absoluteValue
            "$value ${Utils.declOfNum(value, arrayOf("минуту", "минуты", "минут"))} назад"
        }
        in 45* MINUTE..75* MINUTE -> "через час"
        in -75* MINUTE..-45* MINUTE -> "час назад"
        in 75* MINUTE..22* HOUR -> {
            val value = (diff / HOUR).toInt().absoluteValue
            "через $value ${Utils.declOfNum(value, arrayOf("час", "часа", "часов"))}"
        }
        in -22* HOUR..-75* MINUTE -> {
            val value = (diff / HOUR).toInt().absoluteValue
            "$value ${Utils.declOfNum(value, arrayOf("час", "часа", "часов"))} назад"
        }
        in 22* HOUR..26* HOUR -> "через день"
        in -26* HOUR..-22* HOUR -> "день назад"
        in 26* HOUR..360* DAY -> {
            val value = (diff / DAY).toInt().absoluteValue
            "через $value ${Utils.declOfNum(value, arrayOf("день", "дня", "дней"))}"
        }
        in -360 * DAY..-26* HOUR -> {
            val value = (diff / DAY).toInt().absoluteValue
            "$value ${Utils.declOfNum(value, arrayOf("день", "дня", "дней"))} назад"
        }
        in 360* DAY..Long.MAX_VALUE -> "более чем через год"
        in Long.MIN_VALUE..-360* DAY -> "более года назад"
        else -> ""
    }
}


enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}