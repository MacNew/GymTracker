package com.mac.gymtracker.ui.exerciselist

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.mac.gymtracker.R
import com.mac.gymtracker.ui.exerciselist.data.ExerciseListModle
import com.mac.gymtracker.utils.toLocalBitMap
import kotlinx.android.synthetic.main.exercise_list_adapter.view.*
class ExerciseListAdapter(
    var list: List<ExerciseListModle>,
    var exerciseId: Int,
    var menuItem: (exerciseListModle: ExerciseListModle, isEdit:Boolean ) -> Unit ,
    var function: (exercise: String, image: String ) -> Unit
) : RecyclerView.Adapter<ExerciseListAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.exercise_list_adapter, parent, false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (list[position].name == "Barbell Bench Press") {
            holder.itemView.iv_exercise_list_name.rotation = 180.0F
        }
        if (list[position].imageString == null)
            holder.itemView.iv_exercise_list_name.setImageBitmap(
                list[position].image!!.toLocalBitMap()
            ) else
            holder.itemView.iv_exercise_list_name.setImageBitmap(
                list[position].imageString.toLocalBitMap()
            )

        holder.itemView.tv_exercise_list_name.text = list[position].name
        holder.itemView.iv_exercise_list_name.setOnClickListener {
            if (list[position].imageString == null)
                function(list[position].name!!, list[position].image!!)
            else
                function(list[position].name!!, list[position].imageString!!)
        }

         list[position]
        if (list[position].imageString != null) {
            holder.itemView.textViewOptions.setOnClickListener {
                var popup = PopupMenu(it.context, it)
                popup.inflate(R.menu.menu_recycler_view)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_edit -> {
                            menuItem(list[position], true)
                            true
                        }
                        R.id.action_delete -> {
                            menuItem(list[position], false)
                            true
                        }                         //handle menu2 click
                        else -> false
                    }
                }
                popup.show()
            }
        } else {
            holder.itemView.textViewOptions.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

private fun PopupMenu.setOnMenuItemClickListener(function: (MenuItem) -> Unit) {
}