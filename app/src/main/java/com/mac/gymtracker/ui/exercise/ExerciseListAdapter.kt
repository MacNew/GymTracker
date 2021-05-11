package com.mac.gymtracker.ui.exercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mac.gymtracker.R
import com.mac.gymtracker.ui.exercise.data.TrackExerciseModel
import kotlinx.android.synthetic.main.exercise_list_adapter.view.*

class ExerciseListAdapter(var list: List<TrackExerciseModel>) :
    RecyclerView.Adapter<ExerciseListAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.exercise_list_adapter, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.iv_exercise_name.setImageResource(list[position].image!!.toInt())
        holder.itemView.tv_exercise_name.text = list[position].name
    }

    override fun getItemCount(): Int {
        return list.size;
    }
}