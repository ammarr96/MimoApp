package com.amar.mimoapp.di

//import com.amar.mimoapp.BuildConfig
//import com.amar.mimoapp.network.ApiService
//import com.amar.mimoapp.util.Constants
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object NetworkModule {
//
//    private const val baseUrl = Constants.BASE_URL
//
//    @Singleton
//    @Provides
//    fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
//        val interceptor = HttpLoggingInterceptor()
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//        return interceptor;
//    }
//
////    @Singleton
////    @Provides
////    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor
////    ): OkHttpClient {
////        return OkHttpClient.Builder()
////            .addInterceptor(loggingInterceptor)
////            .build()
////    }
//
//    @Singleton
//    @Provides
//    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(baseUrl)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient)
//            .build()
//    }
//
//    @Singleton
//    @Provides
//    fun provideApiService(retrofit: Retrofit.Builder): ApiService {
//        return retrofit
//            .build()
//            .create(ApiService::class.java)
//    }
//}