package ru.skillbranch.devintensive.extensions

fun String.truncate(limit: Int = 16) : String{
    var s = this.trim()
    if (s.length < limit) return s
    else {
        s = this.substring(0, limit)
        if (s.last() == ' ') s = s.substring(0, s.lastIndex)
        s += "..."
        return s
    }
}

fun String.stripHtml() : String {
    var s = this
    s = s.replace(Regex("<[^>]+>"), "")
    s = s.replace(Regex("&[^;]+;"), "")
    s = s.replace(Regex(" {2,}"), " ")
    return s
}