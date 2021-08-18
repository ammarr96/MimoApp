package com.amar.mimoapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.amar.mimoapp.model.Lesson

@Dao
interface LessonDAO {

    @Query("SELECT * from lessons")
    suspend fun getAllLessons(): List<Lesson>

    @Query("SELECT * from lessons WHERE finished = 0")
    suspend fun getUnfinishedLessons(): List<Lesson>

    @Query("SELECT * from lessons WHERE finished = 1")
    suspend fun getFinishedLessons(): List<Lesson>

    @Query("UPDATE lessons SET finished = 1 WHERE id = :lessonId")
    suspend fun makeLessonCompleted(lessonId: Int) : Int

    @Query("UPDATE lessons SET finished = 0")
    suspend fun resetAllLessons() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(list: List<Lesson>)

    @Query("DELETE FROM lessons")
    suspend fun deleteAllLessons()

}