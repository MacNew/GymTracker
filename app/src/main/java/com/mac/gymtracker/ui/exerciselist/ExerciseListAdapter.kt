package com.mac.gymtracker.ui.exerciselist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mac.gymtracker.R
import com.mac.gymtracker.ui.exerciselist.data.ExerciseListModle
import com.mac.gymtracker.utils.CHEST_ID
import com.mac.gymtracker.utils.toLocalBitMap
import kotlinx.android.synthetic.main.exercise_list_adapter.view.*
import java.util.*

class ExerciseListAdapter(
    var list: List<ExerciseListModle>,
    var exerciseId: Int,
    var function: (exercise: String, image: String) -> Unit
) : RecyclerView.Adapter<ExerciseListAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.exercise_list_adapter, parent, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position==0&& exerciseId == CHEST_ID) {
            holder.itemView.iv_exercise_list_name.rotation = 180.0F
        }
        if (list[position].imageString == null)
        holder.itemView.iv_exercise_list_name.setImageResource(
            list[position].image!!.toInt()
        ) else
           holder.itemView.iv_exercise_list_name.setImageBitmap(
               list[position].imageString.toLocalBitMap()
           )

        holder.itemView.tv_exercise_list_name.text = list[position].name
        holder.itemView.cv_parent.setOnClickListener {
            if (list[position].imageString == null)
            function(list[position].name!!, list[position].image!!)
            else
                function(list[position].name!!, list[position].imageString!!)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}

