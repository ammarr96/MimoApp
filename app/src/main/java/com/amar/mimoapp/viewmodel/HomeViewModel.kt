package com.amar.mimoapp.viewmodel

import androidx.lifecycle.*
import com.amar.mimoapp.db.DataState
import com.amar.mimoapp.db.LessonRepository
import com.amar.mimoapp.model.Lesson
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
//@Inject
//constructor(
//    private val mainRepository: LessonRepository
//): ViewModel() {


    val mainRepository: LessonRepository
    //val employeeLiveData: LiveData<List<Lesson>>

    init {

        mainRepository = LessonRepository()
        //this.employeeLiveData = mainRepository.getEmployees()

    }

    private val _dataState: MutableLiveData<DataState<List<Lesson>>> = MutableLiveData()

    val dataState: LiveData<DataState<List<Lesson>>>
        get() = _dataState

    private val _dataStateUnfinished: MutableLiveData<DataState<List<Lesson>>> = MutableLiveData()

    val dataStateUnfinished: LiveData<DataState<List<Lesson>>>
        get() = _dataStateUnfinished

    private val _dataStateReset: MutableLiveData<DataState<Int>> = MutableLiveData()

    val dataStateReset: LiveData<DataState<Int>>
        get() = _dataStateReset

    fun getAllLessons() {
        viewModelScope.launch {
            mainRepository.getAllLessons()
                .onEach {dataState ->
                    _dataState.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }

    fun getUnfinishedLessons() {
        viewModelScope.launch {
            mainRepository.getUnfinishedLessons()
                .onEach {dataState ->
                    _dataStateUnfinished.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }

    fun resetAllLessons() {
        viewModelScope.launch {
            mainRepository.resetAllLessons()
                .onEach {dataState ->
                    _dataStateReset.value = dataState
                }
                .launchIn(viewModelScope)
        }
    }


}