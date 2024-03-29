package ru.skillbranch.devintensive.utils

import android.content.Context
import android.content.res.Resources
import kotlin.math.roundToInt

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.split(" ")
        var firstName = parts?.getOrNull(0)
        var lastName = parts?.getOrNull(1)

        if (firstName?.length == 0) firstName = null
        if (lastName?.length == 0) lastName = null
        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {
        var result = ""
        for (c in payload)
        {
            val change = when (c.toString().toLowerCase()) {
                "а" -> "a"
                "б" -> "b"
                "в" -> "v"
                "г" -> "g"
                "д" -> "d"
                "е" -> "e"
                "ё" -> "e"
                "ж" -> "zh"
                "з" -> "z"
                "и" -> "i"
                "й" -> "i"
                "к" -> "k"
                "л" -> "l"
                "м" -> "m"
                "н" -> "n"
                "о" -> "o"
                "п" -> "p"
                "р" -> "r"
                "с" -> "s"
                "т" -> "t"
                "у" -> "u"
                "ф" -> "f"
                "х" -> "h"
                "ц" -> "c"
                "ч" -> "ch"
                "ш" -> "sh"
                "щ" -> "sh'"
                "ъ" -> ""
                "ы" -> "i"
                "ь" -> ""
                "э" -> "e"
                "ю" -> "yu"
                "я" -> "ya"
                " " -> divider
                else -> c.toString()
            }
            result += if (c.isUpperCase()) change.toUpperCase() else change
        }
        return result
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val a = firstName?.trim()?.let{it -> if (it.length > 0) it.substring(0, 1) else ""} ?: ""
        val b = lastName?.trim()?.let{it -> if (it.length > 0) it.substring(0, 1) else ""} ?: ""
        return if (a.isEmpty() && b.isEmpty()) null else (a+b).toUpperCase()
    }
    fun declOfNum(number: Int, titles: Array<String>) : String{
        val cases = arrayOf(2, 0, 1, 1, 1, 2)
        return titles[ if (number%100>4 && number%100<20) 2 else cases[if(number%10<5) number%10 else 5] ]
    }

    fun convertDpToPx(dp : Float) = (dp * Resources.getSystem().displayMetrics.density).toInt()
    fun convertPxToDp(px : Int) = (px / Resources.getSystem().displayMetrics.density).toInt()
    fun convertSpToPx(sp: Int) = (sp * Resources.getSystem().displayMetrics.scaledDensity)
}