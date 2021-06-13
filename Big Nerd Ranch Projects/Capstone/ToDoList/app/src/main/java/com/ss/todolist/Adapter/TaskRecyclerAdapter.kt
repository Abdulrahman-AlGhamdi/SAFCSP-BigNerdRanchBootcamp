package com.ss.todolist.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ss.todolist.Fragment.TaskListFragmentDirections
import com.ss.todolist.Model.Task
import com.ss.todolist.R
import com.ss.todolist.Utils.Calculations
import kotlinx.android.synthetic.main.task_row_item.view.*

class TaskListAdapter : RecyclerView.Adapter<TaskListAdapter.ViewHolder>() {

    var taskList = emptyList<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.task_row_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTask = taskList[position]
        holder.itemView.task_title.text = currentTask.title
        holder.itemView.task_description.text = currentTask.description
        holder.itemView.task_date.text =
            Calculations.calculateTimeBetweenDates("${currentTask.date} ${currentTask.time}")
        holder.itemView.task_created_time.text = currentTask.createdTime
        holder.itemView.task_check.isEnabled = true
        holder.itemView.task_check.isChecked = currentTask.isCompleted
        holder.itemView.task_check.isEnabled = false

        currentTask.timePassed = Calculations.checkTaskDate(
            "${currentTask.date} ${currentTask.time}"
        )

        if (currentTask.timePassed && !currentTask.isCompleted) {
            holder.itemView.time_passed.visibility = View.VISIBLE
        } else {
            holder.itemView.time_passed.visibility = View.GONE
        }

        if (currentTask.isCompleted) {
            holder.itemView.task_check.setTextColor(Color.GREEN)
        } else {
            holder.itemView.task_check.setTextColor(Color.WHITE)
        }

        holder.itemView.setOnClickListener {
            val action =
                TaskListFragmentDirections.actionTaskListFragmentToUpdateTaskFragment(currentTask)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun setData(habit: List<Task>) {
        this.taskList = habit
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.list_item.setOnClickListener {
                val position = adapterPosition
                val action =
                    TaskListFragmentDirections.actionTaskListFragmentToUpdateTaskFragment(taskList[position])
                itemView.findNavController().navigate(action)
            }
        }
    }
}