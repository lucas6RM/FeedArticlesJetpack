package com.mercierlucas.feedarticlesjetpack.utils

import android.icu.text.SimpleDateFormat
import java.util.Locale

fun formatApiDate(dateSt: String): String {
    val dateInputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val dateOutputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
    val objectDatefromdateStr = dateInputFormat.parse(dateSt)
    return dateOutputFormat.format(objectDatefromdateStr) ?: "not a date"
}