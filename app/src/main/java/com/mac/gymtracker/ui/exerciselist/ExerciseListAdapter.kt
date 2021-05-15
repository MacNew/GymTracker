package com.mac.gymtracker.ui.exerciselist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mac.gymtracker.R
import com.mac.gymtracker.ui.exerciselist.data.ExerciseListModle
import kotlinx.android.synthetic.main.exercise_list_adapter.view.*

class ExerciseListAdapter(
    var list: List<ExerciseListModle>,
    var function: (exercise: String, image: String) -> Unit
) : RecyclerView.Adapter<ExerciseListAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.exercise_list_adapter, parent, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position==0) {
            holder.itemView.iv_exercise_list_name.rotation = 180.0F
        }
        holder.itemView.iv_exercise_list_name.setImageResource(list[position].image!!.toInt())
        holder.itemView.tv_exercise_list_name.text = list[position].name
        holder.itemView.cv_parent.setOnClickListener {
            function(list[position].name!!, list[position].image!!)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}