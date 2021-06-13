package com.ss.todolist.Fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ss.todolist.Adapter.TaskListAdapter
import com.ss.todolist.Model.Task
import com.ss.todolist.R
import com.ss.todolist.ViewModels.TaskViewModel
import kotlinx.android.synthetic.main.fragment_task_list.*

class TaskListFragment : Fragment(R.layout.fragment_task_list) {

    private lateinit var taskList: List<Task>
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var adapter: TaskListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TaskListAdapter()
        tasks_list.adapter = adapter
        tasks_list.layoutManager = LinearLayoutManager(context)

        viewModels()

        list_navigation_bar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.list_add_icon -> findNavController().navigate(R.id.action_taskListFragment_to_addTaskFragment)
                R.id.list_delete_icon -> taskViewModel.deleteAllTasks()
                R.id.list_navigation_icon -> drawer.open()
            }
            true
        }
    }

    private fun viewModels() {
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        sortAndFilterTask(taskViewModel.getAllTasksById)

        navigation_view.setNavigationItemSelectedListener {
            when (it.itemId){
                R.id.id_sort -> sortAndFilterTask(taskViewModel.getAllTasksById)
                R.id.created_time -> sortAndFilterTask(taskViewModel.getAllTaskByCreatedTime)
                R.id.completed_sort -> sortAndFilterTask(taskViewModel.getAllTasksByCompleted)
                R.id.time_passed_sort -> sortAndFilterTask(taskViewModel.getAllTasksByTimePassed)
                R.id.completed_filter -> sortAndFilterTask(taskViewModel.getAllTasksFilteredByCompleted)
                R.id.not_completed_filter -> sortAndFilterTask(taskViewModel.getAllTasksFilteredByNotCompleted)
            }
            true
        }
    }

    private fun sortAndFilterTask(sortAndFilter: LiveData<List<Task>>) {

        sortAndFilter.observe(viewLifecycleOwner, {
            adapter.setData(it)
            taskList = it

            if (it.isEmpty()) {
                tasks_list.visibility = View.GONE
                empty_list.visibility = View.VISIBLE
            } else {
                tasks_list.visibility = View.VISIBLE
                empty_list.visibility = View.GONE
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.list_delete_icon -> taskViewModel.deleteAllTasks()
        }
        return super.onOptionsItemSelected(item)
    }
}