package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = fullName?.split(" ")
        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)

        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {
        var result = ""
        for (c in payload)
        {
            val change = when (c.toString()) {
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
        return if (a.isEmpty() && b.isEmpty()) null else a+b
    }
    fun declOfNum(number: Int, titles: Array<String>) : String{
        val cases = arrayOf(2, 0, 1, 1, 1, 2)
        return titles[ if (number%100>4 && number%100<20) 2 else cases[if(number%10<5) number%10 else 5] ]
    }
}