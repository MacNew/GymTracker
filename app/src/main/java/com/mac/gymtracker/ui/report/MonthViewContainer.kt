package com.mac.gymtracker.ui.report

import android.view.View
import android.widget.TextView
import com.kizitonwose.calendarview.ui.ViewContainer
import com.mac.gymtracker.R
import com.mac.gymtracker.databinding.CalendarMonthHeaderBinding

class MonthViewContainer(view: View): ViewContainer(view) {
   val legendLayout = CalendarMonthHeaderBinding.bind(view).legendLayout.root
}
