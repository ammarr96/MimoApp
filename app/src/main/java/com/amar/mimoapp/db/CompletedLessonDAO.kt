package com.amar.mimoapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amar.mimoapp.model.CompletedLesson
import com.amar.mimoapp.model.Lesson

@Dao
interface CompletedLessonDAO {

    @Query("SELECT * from completed_lessons")
    fun getAllCompletedLessons(): LiveData<List<CompletedLesson>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCompletedLessons(list: List<CompletedLesson>)

    @Query("DELETE FROM completed_lessons")
    fun deleteAllCompletedLessons()

}