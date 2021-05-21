package com.mac.gymtracker.ui.lastsummery

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mac.gymtracker.R
import com.mac.gymtracker.ui.exerciserecord.ExerciseRecordAdapter
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel
import com.mac.gymtracker.ui.lastsummery.dao.LastSummeryModel
import com.mac.gymtracker.utils.convertGymTrackerTime
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
        holder.itemView.tv_date_last_summery_row.text =
            myObject.date.toLong().convertGymTrackerTime()
        holder.itemView.iv_exercise_image_last_summery.setImageResource(myObject.image.toInt())
        holder.itemView.rv_exercise_list_row.layoutManager =
            LinearLayoutManager(holder.itemView.context)
        var arrayList: ArrayList<ExerciseRecordModel> = ArrayList()
        val adapter = ExerciseRecordAdapter(arrayList) {

        }
        holder.itemView.rv_exercise_list_row.adapter = adapter

        holder.itemView.cv_parent.setOnClickListener {
            Log.e("adapter", "called me")
            if (myObject.isShow) {
                arrayList.clear()
                adapter.notifyDataSetChanged()
                holder.itemView.cv_parent.setCardBackgroundColor(
                    ContextCompat.getColor(
                        it.context,
                        R.color.white
                    )
                )
                myObject.isShow = false
            } else {
                arrayList.clear()
                holder.itemView.cv_parent.setCardBackgroundColor(
                    ContextCompat.getColor(
                        it.context,
                        R.color.card_back_ground_color
                    )
                )
                arrayList.addAll(myObject.list!!)
                adapter.notifyDataSetChanged()
                myObject.isShow = true
            }
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

}