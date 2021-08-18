package com.amar.mimoapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amar.mimoapp.custom.DataTypeConverter
import com.amar.mimoapp.model.CompletedLesson
import com.amar.mimoapp.model.Lesson

@Database(entities = [Lesson::class, CompletedLesson::class], version = 14)
@TypeConverters(*[DataTypeConverter::class])
abstract class LessonDatabase : RoomDatabase() {

    abstract fun completedLessonDao(): CompletedLessonDAO

    abstract fun lessonDao(): LessonDAO

}