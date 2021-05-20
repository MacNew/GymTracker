package com.mac.gymtracker.ui.lastsummery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mac.gymtracker.R
import com.mac.gymtracker.ui.exerciserecord.ExerciseRecordAdapter
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel
import com.mac.gymtracker.ui.lastsummery.dao.LastSummeryModel
import kotlinx.android.synthetic.main.row_last_summary_parent.view.*

class LastSummeryRecyclerViewAdapter(var list: List<LastSummeryModel>) :
    RecyclerView.Adapter<LastSummeryRecyclerViewAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_last_summary_parent, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var myObject = list[position]
        holder.itemView.exercise_name_row.text = myObject.exerciseListName
        holder.itemView.tv_exercise_list_name_row.text = myObject.exerciseName
        holder.itemView.tv_date_last_summery_row.text = myObject.date
        holder.itemView.iv_exercise_image_last_summery.setImageResource(myObject.image.toInt())
        holder.itemView.rv_exercise_list_row.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.itemView.rv_exercise_list_row.adapter = ExerciseRecordAdapter(myObject.list as List<ExerciseRecordModel>) {

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}