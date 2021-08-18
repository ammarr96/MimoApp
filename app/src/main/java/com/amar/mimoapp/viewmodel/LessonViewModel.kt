package com.amar.mimoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amar.mimoapp.db.DataState
import com.amar.mimoapp.db.LessonRepository
import com.amar.mimoapp.model.Lesson
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LessonViewModel: ViewModel(){


    val mainRepository: LessonRepository

    init {
        mainRepository = LessonRepository()
    }

    private val _dataState: MutableLiveData<DataState<List<Lesson>>> = MutableLiveData()

    val dataState: LiveData<DataState<List<Lesson>>>
        get() = _dataState

    private val _dataStateLesson: MutableLiveData<DataState<Int>> = MutableLiveData()

    val dataStateLesson: LiveData<DataState<Int>>
        get() = _dataStateLesson

    fun getUnFinishedLessons() {
        viewModelScope.launch {
            mainRepository.getUnfinishedLessons()
                .onEach {dataState ->
                    _dataState.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }

    fun makeLessonCompleted(lesson: Lesson) {
        viewModelScope.launch {
            mainRepository.makeLessonCompleted(lesson)
                .onEach {dataStateLesson ->
                    _dataStateLesson.value = dataStateLesson
                }
                .launchIn(viewModelScope)
        }
    }

}