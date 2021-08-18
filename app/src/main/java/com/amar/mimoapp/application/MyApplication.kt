package com.amar.mimoapp.application

import android.app.Application
import androidx.room.Room
import com.amar.mimoapp.db.LessonDatabase

//@HiltAndroidApp
class MyApplication : Application() {

    companion object {
        var database: LessonDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        database =  Room.databaseBuilder(applicationContext, LessonDatabase::class.java, "lesson.db").fallbackToDestructiveMigration().build()
    }
}