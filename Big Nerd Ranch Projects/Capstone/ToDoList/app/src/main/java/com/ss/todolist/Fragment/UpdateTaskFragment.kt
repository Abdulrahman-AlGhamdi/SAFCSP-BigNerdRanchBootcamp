package com.ss.todolist.Fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ss.todolist.Model.Task
import com.ss.todolist.R
import com.ss.todolist.Utils.Calculations
import com.ss.todolist.ViewModels.TaskViewModel
import kotlinx.android.synthetic.main.fragment_add_task.*
import kotlinx.android.synthetic.main.fragment_update_task.*
import java.util.*

class UpdateTaskFragment : Fragment(R.layout.fragment_update_task),
    TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private var cleanDate = ""
    private var cleanTime = ""
    private lateinit var taskViewModel: TaskViewModel
    private val argument by navArgs<UpdateTaskFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setUpDateAndTimePicker()
    }

    private fun initViews() {
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        update_title.setText(argument.selectedTask.title)
        update_description.setText(argument.selectedTask.description)
        update_check_completed.isChecked = argument.selectedTask.isCompleted
        update_date_selected.text = argument.selectedTask.date
        update_time_selected.text = argument.selectedTask.time

        update_navigation_bar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.update_icon -> updateTaskInDatabase()
                R.id.update_delete_icon -> deleteTask(argument.selectedTask)
            }
            true
        }
    }

    private fun updateTaskInDatabase() {
        val title = update_title.text.toString()
        val description = update_description.text.toString()
        val date = argument.selectedTask.date
        val time = argument.selectedTask.time
        val createdDate = argument.selectedTask.createdTime
        val isCompleted = update_check_completed.isChecked
        var timePassed = false

        if (title.isEmpty())
            add_title.error = "Enter a Title"

        if (description.isEmpty())
            add_description.error = "Enter a Description"

        if (title.isNotBlank() && description.isNotBlank() && cleanDate.isNotBlank() && cleanTime.isNotBlank()) {
            timePassed = Calculations.checkTaskDate("$cleanDate $cleanTime")
            timePassed = timePassed && !isCompleted
            val task = Task(
                argument.selectedTask.id,
                title,
                description,
                cleanDate,
                cleanTime,
                createdDate,
                isCompleted,
                timePassed
            )
            taskViewModel.updateTask(task)
            Toast.makeText(context, "Task Updated Successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateTaskFragment_to_taskListFragment)

        } else if (title.isNotBlank() && description.isNotBlank() && cleanDate.isNotBlank()) {
            timePassed = Calculations.checkTaskDate("$cleanDate $time")
            timePassed = timePassed && !isCompleted
            val task = Task(
                argument.selectedTask.id,
                title,
                description,
                cleanDate,
                time,
                createdDate,
                isCompleted,
                timePassed
            )
            taskViewModel.updateTask(task)
            Toast.makeText(context, "Task Updated Successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateTaskFragment_to_taskListFragment)
        } else if (title.isNotBlank() && description.isNotBlank() && cleanTime.isNotBlank()) {
            timePassed = Calculations.checkTaskDate("$date $cleanTime")
            timePassed = timePassed && !isCompleted
            val task = Task(
                argument.selectedTask.id,
                title,
                description,
                date,
                cleanTime,
                createdDate,
                isCompleted,
                timePassed
            )
            taskViewModel.updateTask(task)
            Toast.makeText(context, "Task Updated Successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateTaskFragment_to_taskListFragment)
        } else {
            timePassed = Calculations.checkTaskDate("$date $time")
            timePassed = timePassed && !isCompleted
            val task = Task(
                argument.selectedTask.id,
                title,
                description,
                date,
                time,
                createdDate,
                isCompleted,
                timePassed
            )
            taskViewModel.updateTask(task)
            Toast.makeText(context, "Task Updated Successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateTaskFragment_to_taskListFragment)
        }
    }

    private fun deleteTask(task: Task) {
        taskViewModel.deleteTask(task)
        Toast.makeText(context, "Task Successfully Deleted", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_updateTaskFragment_to_taskListFragment)
    }

    private fun setUpDateAndTimePicker() {
        val calender = Calendar.getInstance()

        update_date.setOnClickListener {
            val day = calender.get(Calendar.DAY_OF_MONTH)
            val month = calender.get(Calendar.MONTH)
            val year = calender.get(Calendar.YEAR)
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        update_time.setOnClickListener {
            val hour = calender.get(Calendar.HOUR_OF_DAY)
            val minute = calender.get(Calendar.MINUTE)
            TimePickerDialog(context, this, hour, minute, true).show()
        }
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        cleanTime = Calculations.cleanTime(p1, p2)
        update_time_selected.text = "Time: $cleanTime"
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        cleanDate = Calculations.cleanDate(p3, p2, p1)
        update_date_selected.text = "Date: $cleanDate"
    }
}