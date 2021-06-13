package com.ss.todolist.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.ss.todolist.Model.Task

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task_table ORDER BY id DESC")
    fun getAllTasksById(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table ORDER BY createdTime")
    fun getAllTasksByCreatedTime(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table ORDER BY isCompleted DESC")
    fun getAllTasksByCompleted(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table ORDER BY timePassed DESC")
    fun getAllTasksByTimePassed(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE isCompleted = :isCompleted")
    fun getAllTasksFilteredByCompleted(isCompleted: Boolean): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE isCompleted = :isNotCompleted")
    fun getAllTasksFilteredByNotCompleted(isNotCompleted: Boolean): LiveData<List<Task>>

    @Query("DELETE FROM task_table")
    suspend fun deleteAllTasks()
}