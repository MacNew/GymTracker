package com.mac.gymtracker.ui.report

import android.view.View
import android.widget.TextView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.ViewContainer
import com.mac.gymtracker.R
import com.mac.gymtracker.databinding.CalendarDayLayoutBinding
import java.time.LocalDate

class DayViewContainer(view: View, function: (dayDate: LocalDate) -> Unit) : ViewContainer(view) {
    lateinit var day: CalendarDay // Will be set when this container is bound.
    val binding = CalendarDayLayoutBinding.bind(view)

    init {
        view.setOnClickListener {
            if (day.owner == DayOwner.THIS_MONTH) {
                function(day.date)
            }
        }
    }
}
