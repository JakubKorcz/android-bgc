package edu.put.boardgamecollector151855

import android.icu.text.SimpleDateFormat
import java.util.*

class CurrentDate {

    val sdf = SimpleDateFormat("yyyy-MM-dd")
    val currentDate=sdf.format(Date())

    fun GetCurrentDate():String
    {
        return currentDate
    }
}