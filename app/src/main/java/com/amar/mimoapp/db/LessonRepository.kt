package com.amar.mimoapp.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amar.mimoapp.application.MyApplication
import com.amar.mimoapp.model.CompletedLesson
import com.amar.mimoapp.model.Lesson
import com.amar.mimoapp.network.ApiService
import com.amar.mimoapp.network.RetrofitFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LessonRepository {
//@Inject
//constructor(private val lessonDAO: LessonDAO,
//            private val apiService: ApiService) {


    //lateinit var employeeList : MutableLiveData<List<Lesson>>
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


//    lateinit var lessonList : MutableLiveData<List<Lesson>>
//    private val apiService: ApiService
//
//    init {
//        apiService = RetrofitFactory().makeApiService()!!
//        lessonList = MutableLiveData()
//    }
//
//    fun getLessons() : LiveData<List<Lesson>> {
//
//        val completedLessons = MyApplication.database!!.completedLessonDao().getAllCompletedLessons()
//        lessonList.value = MyApplication.database!!.lessonDao().getAllLessons().value
//
//        return MyApplication.database!!.lessonDao().getAllLessons()
//
////        System.out.println("LUBA")
////
////        for (i in 0..(lessonList.value?.size ?: 0)) {
////
////            System.out.println(lessonList.value?.get(i)?.id)
////
////            val found = completedLessons.value?.firstOrNull { it.id == lessonList.value?.get(i)?.id ?: 0 } != null
////
////            lessonList.value?.get(i)?.finished = found
////        }
////
////        return lessonList;
//
//    }
//
//    fun getLessonsFromApi() {
//
//        val call = apiService.getLessons()
//
//        System.out.println("SUCCESS3")
//
//        call.enqueue(object : Callback<List<Lesson>?> {
//            override fun onResponse(call: Call<List<Lesson>?>, response: Response<List<Lesson>?>) {
//
//                System.out.println("SUCCESS2")
//                when(response.code())
//                {
//                    200 ->{
//                        Thread(Runnable {
//
//                            System.out.println("SUCCESS1")
//
//
//                            MyApplication.database!!.lessonDao().deleteAllLessons()
//                            MyApplication.database!!.lessonDao().insertLessons(response.body()!!)
//
//                            System.out.println("SUCCESS")
//
//                           // getLessons()
//
//                        }).start()
//                    }
//                }
//
//            }
//
//            override fun onFailure(call: Call<List<Lesson>?>, t: Throwable) {
//
//            }
//
//        })
//
//    }
}