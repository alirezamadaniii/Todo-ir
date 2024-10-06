package com.example.todoir.peresentaion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.todoir.data.model.Priority
import com.example.todoir.data.model.Task
import com.example.todoir.domain.usecase.AddTaskUseCase
import com.example.todoir.domain.usecase.CompletedTaskUseCase
import com.example.todoir.domain.usecase.DeleteTaskUseCase
import com.example.todoir.domain.usecase.GetCategoryTaskUseCase
import com.example.todoir.domain.usecase.GetCompletedTaskUseCase
import com.example.todoir.domain.usecase.GetPriorityTaskUseCase
import com.example.todoir.domain.usecase.GetTaskAfterFilterUseCase
import com.example.todoir.domain.usecase.GetTaskUseCase
import com.example.todoir.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val completedTaskUseCase: CompletedTaskUseCase,
    private val getTaskAfterFilterUseCase: GetTaskAfterFilterUseCase,
    private val getPriorityTaskUseCase: GetPriorityTaskUseCase,
    private val getCategoryTaskUseCase: GetCategoryTaskUseCase,
    private val getCompletedTaskUseCase: GetCompletedTaskUseCase
) :ViewModel(){
    fun addTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        addTaskUseCase.execute(task)
    }

    fun getTask() = liveData {
        getTaskUseCase.execute().collect(){
            emit(it)
        }
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        deleteTaskUseCase.execute(task)

    }

    fun updateTask(task: Task) = viewModelScope.launch {
        updateTaskUseCase.execute(task)
    }

    fun completedTask(id:Int,isCompleted: Boolean) = viewModelScope.launch {
        completedTaskUseCase.execute(id, isCompleted)
    }

    fun getTaskAfterFilter(date:String) = liveData {
        getTaskAfterFilterUseCase.execute(date).collect(){
            emit(it)
        }
    }

    fun getPriorityTask(priority: Int) = liveData {
        getPriorityTaskUseCase.execute(priority).collect(){
            emit(it)
        }
    }


    fun getCategoryTask(category:String) = liveData {
        getCategoryTaskUseCase.execute(category).collect(){
            emit(it)
        }
    }


    fun getCompletedTask(isCompleted:Boolean) = liveData {
        getCompletedTaskUseCase.execute(isCompleted).collect(){
            emit(it)
        }
    }

}