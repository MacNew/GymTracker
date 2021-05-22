package com.mac.gymtracker.ui.report

import android.view.View
import android.widget.TextView
import com.kizitonwose.calendarview.ui.ViewContainer
import com.mac.gymtracker.R

class MonthViewContainer(view: View): ViewContainer(view) {
    val textView = view.findViewById<TextView>(R.id.headerTextView)
}