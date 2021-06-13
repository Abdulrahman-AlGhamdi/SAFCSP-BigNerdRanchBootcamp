package com.ss.todolist.Repository

import androidx.lifecycle.LiveData
import com.ss.todolist.Database.TaskDao
import com.ss.todolist.Model.Task

class TaskRepository(private val taskDao: TaskDao) {
    val getAllTasksById: LiveData<List<Task>> = taskDao.getAllTasksById()
    val getAllTaskByCreatedTime: LiveData<List<Task>> = taskDao.getAllTasksByCreatedTime()
    val getAllTasksByCompleted: LiveData<List<Task>> = taskDao.getAllTasksByCompleted()
    val getAllTasksByTimePassed: LiveData<List<Task>> = taskDao.getAllTasksByTimePassed()
    val getAllTasksFilteredByCompleted: LiveData<List<Task>> = taskDao.getAllTasksFilteredByCompleted(isCompleted = true)
    val getAllTasksFilteredByNotCompleted: LiveData<List<Task>> = taskDao.getAllTasksFilteredByCompleted(isCompleted = false)

    suspend fun addTask(task: Task) {
        taskDao.addTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }
}