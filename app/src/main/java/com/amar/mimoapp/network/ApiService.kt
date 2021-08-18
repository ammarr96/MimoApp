package com.amar.mimoapp.network

import com.amar.mimoapp.model.Lesson
import com.amar.mimoapp.model.LessonResponseObject
import retrofit2.http.GET

interface ApiService {

    @GET("lessons")
    suspend fun getLessons(): LessonResponseObject
}