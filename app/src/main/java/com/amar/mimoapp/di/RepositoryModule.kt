package com.amar.mimoapp.di
//
//import com.amar.mimoapp.db.LessonDAO
//import com.amar.mimoapp.db.LessonRepository
//import com.amar.mimoapp.network.ApiService
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object RepositoryModule {
//
//    @Singleton
//    @Provides
//    fun provideLessonRepository(
//        lessonDao: LessonDAO,
//        retrofit: ApiService
//    ): LessonRepository{
//        return LessonRepository(lessonDao, retrofit)
//    }
//}