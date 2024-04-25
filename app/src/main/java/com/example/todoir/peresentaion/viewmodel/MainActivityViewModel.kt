package com.example.todoir.peresentaion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.todoir.data.model.Task
import com.example.todoir.domain.usecase.AddTaskUseCase
import com.example.todoir.domain.usecase.GetTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val getTaskUseCase: GetTaskUseCase
) :ViewModel(){
    fun addTask(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        addTaskUseCase.execute(task)
    }

    fun getTask() = liveData {
        getTaskUseCase.execute().collect(){
            emit(it)
        }
    }

}