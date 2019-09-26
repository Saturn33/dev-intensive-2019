package ru.skillbranch.devintensive.extensions

import android.widget.TextView
import com.google.android.material.snackbar.Snackbar


fun Snackbar.setTextColor(color: Int): Snackbar {
    val tv = view.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    tv.setTextColor(color)

    return this
}
