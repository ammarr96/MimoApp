package com.amar.mimoapp.di

//import android.content.Context
//import androidx.room.Room
//import com.amar.mimoapp.db.LessonDAO
//import com.amar.mimoapp.db.LessonDatabase
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@InstallIn(SingletonComponent::class)
//@Module
//object DatabaseModule {
//
//    @Provides
//    @Singleton
//    fun provideAppDatabase(@ApplicationContext appContext: Context): LessonDatabase {
//        return Room.databaseBuilder(
//            appContext,
//            LessonDatabase::class.java,
//            "lessons.db"
//        ).fallbackToDestructiveMigration()
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideMovieDao(lessonDatabase: LessonDatabase): LessonDAO {
//        return lessonDatabase.lessonDao()
//    }
//}