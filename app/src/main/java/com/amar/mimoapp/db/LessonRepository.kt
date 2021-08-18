package com.amar.mimoapp.db

import com.amar.mimoapp.application.MyApplication
import com.amar.mimoapp.model.Lesson
import com.amar.mimoapp.network.ApiService
import com.amar.mimoapp.network.RetrofitFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LessonRepository {

    private val apiService: ApiService
    private val lessonDatabase: LessonDatabase

    init {
        apiService = RetrofitFactory().makeApiService()!!
        lessonDatabase = MyApplication.database!!
    }

    suspend fun getAllLessons(): Flow<DataState<List<Lesson>>> = flow {
        emit(DataState.Loading)
        try{
            val networkLessons = apiService.getLessons().lessonList

            val cachedFinishedLessons = lessonDatabase.lessonDao().getFinishedLessons()
            for (lesson in cachedFinishedLessons) {
                networkLessons.find { it.id == lesson.id  }?.finished = true
            }
            lessonDatabase.lessonDao().deleteAllLessons()
            lessonDatabase.lessonDao().insertLessons(networkLessons)
            val cachedLessons = lessonDatabase.lessonDao().getAllLessons()
            emit(DataState.Success(cachedLessons))
        }catch (e: Exception){
            emit(DataState.Error(e))
            System.out.println(e.message)
            e.printStackTrace()
        }
    }

    suspend fun getUnfinishedLessons(): Flow<DataState<List<Lesson>>> = flow {
        emit(DataState.Loading)
        try{
            val cachedLessons = lessonDatabase.lessonDao().getUnfinishedLessons()
            emit(DataState.Success(cachedLessons))
        }catch (e: Exception){
            emit(DataState.Error(e))
            System.out.println(e.message)
            e.printStackTrace()
        }
    }

    suspend fun makeLessonCompleted(lesson: Lesson): Flow<DataState<Int>> = flow {
        emit(DataState.Loading)
        try{
            val rowUpdated = lessonDatabase.lessonDao().makeLessonCompleted(lesson.id)
            emit(DataState.Success(rowUpdated))
        }catch (e: Exception){
            emit(DataState.Error(e))
            System.out.println(e.message)
            e.printStackTrace()
        }
    }

    suspend fun resetAllLessons(): Flow<DataState<Int>> = flow {
        emit(DataState.Loading)
        try{
            val rowUpdated = lessonDatabase.lessonDao().resetAllLessons()
            emit(DataState.Success(rowUpdated))
        }catch (e: Exception){
            emit(DataState.Error(e))
            System.out.println(e.message)
            e.printStackTrace()
        }
    }


}