package com.example.todoir.peresentaion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.todoir.data.model.Category
import com.example.todoir.domain.usecase.AddCategoryUseCase
import com.example.todoir.domain.usecase.GetCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCategoryViewModel @Inject constructor(
    private val addCategoryUseCase: AddCategoryUseCase,
    private val getCategoryUseCase: GetCategoryUseCase
):ViewModel() {

    fun addCategory(category: Category) =viewModelScope.launch(Dispatchers.IO) {
        addCategoryUseCase.execute(category)
    }

    fun getCategory() = liveData {
        getCategoryUseCase.execute().collect{
            emit(it)
        }
    }

}