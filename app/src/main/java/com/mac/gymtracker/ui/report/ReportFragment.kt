package com.mac.gymtracker.ui.report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.mac.gymtracker.MainActivity
import com.mac.gymtracker.R
import com.mac.gymtracker.databinding.FragmentReportBinding
import com.mac.gymtracker.ui.exercise.data.TrackExerciseLocalDataSource
import com.mac.gymtracker.ui.exerciserecord.ExerciseRecordFactory
import com.mac.gymtracker.ui.exerciserecord.ExerciseRecordViewModle
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordRepo
import com.mac.gymtracker.ui.lastsummery.LastSummeryRecyclerViewAdapter
import com.mac.gymtracker.ui.lastsummery.dao.LastSummeryModel
import com.mac.gymtracker.utils.daysOfWeekFromLocale
import com.mac.gymtracker.utils.makeInVisible
import com.mac.gymtracker.utils.makeVisible
import com.mac.gymtracker.utils.setTextColorRes
import kotlinx.android.synthetic.main.app_bar_main.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class ReportFragment : Fragment() {

    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    val toolbar: androidx.appcompat.widget.Toolbar
      get() = (activity as MainActivity).toolbar
    private val titleSameYearFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val titleFormatter = DateTimeFormatter.ofPattern("MMM yyyy")
    private lateinit var viewmodel: ExerciseRecordViewModle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewmodel = ViewModelProvider(
            this,
            ExerciseRecordFactory(
                ExerciseRecordRepo(activity!!.applicationContext),
                "", TrackExerciseLocalDataSource(activity!!.applicationContext)
            )
        ).get(
            ExerciseRecordViewModle::class.java)

        _binding = FragmentReportBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        binding.rvReport.apply {
            this.layoutManager = LinearLayoutManager(context)
        }

        viewmodel.lastSummery.observe(this, {
            binding.rvReport.adapter = LastSummeryRecyclerViewAdapter(it)
            binding.rvReport.adapter!!.notifyDataSetChanged()
        })

        viewmodel.stringDate.observe(this, {
            ExerciseRecordRepo(context!!).getListByDate(it) { list->
                var hsMap:HashMap<String,ArrayList<ExerciseRecordModel>> = HashMap()
                var key:HashSet<String> = HashSet<String>();
                list.forEach { exerciseRecord ->
                    key.add(exerciseRecord.saveTime)
                    if (!hsMap.containsKey(exerciseRecord.saveTime)) {
                        var exerciseRecordList: ArrayList<ExerciseRecordModel> = ArrayList();
                        exerciseRecordList.add(exerciseRecord)
                        hsMap[exerciseRecord.saveTime] = exerciseRecordList;
                    } else {
                        hsMap[exerciseRecord.saveTime]!!.add(exerciseRecord)
                    }
                }

                var lastSummeryModel: ArrayList<LastSummeryModel> = ArrayList()
                key.forEach {
                    lastSummeryModel.add(LastSummeryModel(false, it,
                        hsMap[it]?.get(0)!!.mainExercise, hsMap[it]?.get(0)?.exerciseName!!,
                        hsMap[it]?.get(0)!!.image , hsMap[it]
                    ))
                }
                viewmodel.updateList(lastSummeryModel)
            }
        })


        binding.exThreeCalendar.apply {
            setup(currentMonth.minusMonths(10), currentMonth.plusMonths(10), daysOfWeek.first())
            scrollToMonth(currentMonth)

            if (savedInstanceState == null) {
                binding.exThreeCalendar.post {// 1621793700000
                    selectDate(today)
                }
            }
        }
        binding.exThreeCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view) {
                selectDate(it)

            }

            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.exThreeDayText
                val dotView = container.binding.exThreeDotView

                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.makeVisible()
                    when (day.date) {
                        today -> {
                            textView.setTextColorRes(R.color.example_3_white)
                            textView.setBackgroundResource(R.drawable.bg_color_calender)
                            dotView.makeInVisible()
                            Log.e("Tag", "Called today ")
                        }
                        selectedDate -> {
                            textView.setTextColorRes(R.color.example_3_blue)
                            textView.setBackgroundResource(R.drawable.selected_day)
                            dotView.makeInVisible()
                            Log.e("Tag", "Called me ");
                        }
                        else -> {
                            textView.setTextColorRes(R.color.example_3_black)
                            textView.background = null
                            dotView.visibility = View.GONE
                            Log.e("Tag", "called else part")
                    //         dotView.isVisible = events[day.date].orEmpty().isNotEmpty()
                        }
                    }

                } else {
                    textView.makeInVisible()
                    dotView.makeInVisible()
                }
            }
        }
        binding.exThreeCalendar.monthScrollListener = {
            toolbar.title = if (it.year == today.year) {
                titleSameYearFormatter.format(it.yearMonth)
            } else {
                titleFormatter.format(it.yearMonth)
            }
            selectDate(it.yearMonth.atDay(1))
        }
        binding.exThreeCalendar.monthHeaderBinder = object:MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                if (container.legendLayout.tag == null) {
                    container.legendLayout
                    container.legendLayout.tag = month.yearMonth
                }

            }
        }

    }

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()
    // private val events = mutableMapOf<LocalDate, List<Event>>()


    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { binding.exThreeCalendar.notifyDateChanged(it) }
            binding.exThreeCalendar.notifyDateChanged(date)
            updateAdapterForDate(date)
        }
    }

    private val selectionFormatter = DateTimeFormatter.ofPattern("d MMM yyyy")
    private fun updateAdapterForDate(date: LocalDate) {
     var myDate = selectionFormatter.format(date)
     var df = SimpleDateFormat("d MMM yyyy")
        try {
            var startDate = df.parse(myDate)
            Log.e("TAG", df.format(startDate))
            Log.e("TAG", startDate.time.toString())
            viewmodel.sendDate(df.format(startDate))


        }catch (exception:Exception) {
            Log.e("TAG", exception.message!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}