package com.ss.todolist.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ss.todolist.Database.TaskDatabase
import com.ss.todolist.Model.Task
import com.ss.todolist.Repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TaskRepository
    val getAllTasksById: LiveData<List<Task>>
    val getAllTaskByCreatedTime: LiveData<List<Task>>
    val getAllTasksByCompleted: LiveData<List<Task>>
    val getAllTasksByTimePassed: LiveData<List<Task>>
    val getAllTasksFilteredByCompleted: LiveData<List<Task>>
    val getAllTasksFilteredByNotCompleted: LiveData<List<Task>>

    init {
        val habitDao= TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(habitDao)

        getAllTasksById = repository.getAllTasksById
        getAllTaskByCreatedTime = repository.getAllTaskByCreatedTime
        getAllTasksByCompleted = repository.getAllTasksByCompleted
        getAllTasksByTimePassed = repository.getAllTasksByTimePassed
        getAllTasksFilteredByCompleted = repository.getAllTasksFilteredByCompleted
        getAllTasksFilteredByNotCompleted = repository.getAllTasksFilteredByNotCompleted
    }

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTask(task)
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTasks()
        }
    }
}