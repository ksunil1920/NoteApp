package com.sky.notes.di

import com.sky.notes.api.AuthInterceptor
import com.sky.notes.api.NoteApi
import com.sky.notes.api.UserAPI
import com.sky.notes.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NoteModule {


    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)

    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        okHttpClientBuilder:
        OkHttpClient.Builder,
    ): OkHttpClient {
        return okHttpClientBuilder.addInterceptor(authInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(interceptor)
    }

    @Singleton
    @Provides
    fun provideUserApi(retrofit: Retrofit.Builder, okHttpClient: OkHttpClient.Builder): UserAPI {
        return retrofit.client(okHttpClient.build()).build().create(UserAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideNoteRetrofit(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient,
    ): NoteApi {
        return retrofitBuilder.client(okHttpClient).build().create(NoteApi::class.java)
    }
}