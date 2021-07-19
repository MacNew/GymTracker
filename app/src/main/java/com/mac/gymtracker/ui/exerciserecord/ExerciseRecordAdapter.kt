package com.mac.gymtracker.ui.exerciserecord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mac.gymtracker.R
import com.mac.gymtracker.ui.exerciserecord.data.ExerciseRecordModel
import kotlinx.android.synthetic.main.exercise_record_adapter_row.view.*

class ExerciseRecordAdapter(
    var list: ArrayList<ExerciseRecordModel>,
    var function: (primaryKey: String) -> Unit
) : RecyclerView.Adapter<ExerciseRecordAdapter.ViewHolderExerciseRecordAdapter>() {

    class ViewHolderExerciseRecordAdapter(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderExerciseRecordAdapter {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.exercise_record_adapter_row, parent, false)
        return ViewHolderExerciseRecordAdapter(view)
    }

    override fun onBindViewHolder(holder: ViewHolderExerciseRecordAdapter, position: Int) {
        var objects = list[position]
        holder.itemView.tv_set.text = objects.set!!
        holder.itemView.tv_rip.text = objects.reps!!
        holder.itemView.tv_weight.text = objects.weight!!
        holder.itemView.tv_time_row.text = objects.timeInSecond!!
        holder.itemView.tv_time_row_rest.text = objects.restTimeSecond
    }

    override fun getItemCount(): Int {
         return list.size
    }
}