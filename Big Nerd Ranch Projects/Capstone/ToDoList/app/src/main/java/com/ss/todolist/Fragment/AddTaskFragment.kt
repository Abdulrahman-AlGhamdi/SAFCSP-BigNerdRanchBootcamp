package com.ss.todolist.Fragment

import com.ss.todolist.Utils.Calculations
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
import com.ss.todolist.Model.Task
import com.ss.todolist.R
import com.ss.todolist.ViewModels.TaskViewModel
import kotlinx.android.synthetic.main.fragment_add_task.*
import java.util.*

class AddTaskFragment : Fragment(R.layout.fragment_add_task),
    TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private var cleanDate = ""
    private var cleanTime = ""
    private lateinit var taskViewModel: TaskViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initView()
        setUpDateAndTimePicker()
    }

    private fun initView() {
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        add_task_button.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.add_icon -> addTaskToDatabase()
            }
            true
        }
    }

    private fun addTaskToDatabase() {
        val title = add_title.text.toString()
        val description = add_description.text.toString()
        val date = cleanDate
        val time = cleanTime
        val createdTime = Calculations.createdDate()
        val isCompleted = add_check_completed.isChecked
        var timePassed = false

        if (title.isEmpty())
            add_title.error = "Enter a Title"

        if (description.isEmpty())
            add_description.error = "Enter a Description"

        if (title.isNotBlank() && description.isNotBlank() && cleanDate.isNotBlank() && cleanTime.isNotBlank()) {
            timePassed = Calculations.checkTaskDate("$date $time")
            timePassed = timePassed && !isCompleted
            val task = Task(0, title, description, date, time, createdTime, isCompleted, timePassed)
            taskViewModel.addTask(task)
            Toast.makeText(context, "Task Added", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addTaskFragment_to_taskListFragment)
        } else {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpDateAndTimePicker() {
        val calendar = Calendar.getInstance()

        add_date.setOnClickListener {
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        add_time.setOnClickListener {
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            TimePickerDialog(context, this, hour, minute, true).show()
        }
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        cleanTime = Calculations.cleanTime(p1, p2)
        add_time_selected.text = "Time: $cleanTime"
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        cleanDate = Calculations.cleanDate(p3, p2, p1)
        add_date_selected.text = "Date: $cleanDate"
    }
}