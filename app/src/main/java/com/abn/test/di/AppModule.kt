package com.abn.test.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import com.abn.test.BuildConfig
import com.abn.test.data.local.RemoteKeyDao
import com.abn.test.data.local.RepositoryDao
import com.abn.test.data.local.RepositoryDatabase
import com.abn.test.data.remote.ApiService
import com.abn.test.repositories.GitRepoRepository
import com.abn.test.repositories.GitRepoRepositoryImpl
import com.abn.test.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApiService(httpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build().create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.retryOnConnectionFailure(false)
        builder.interceptors().add(HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        })
        return builder.build()
    }

    @Singleton
    @Provides
    fun repositoryDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, RepositoryDatabase::class.java, Constants.DATABASE_NAME)
            .build()


    @Singleton
    @Provides
    fun provideRepositoryDao(dataBase: RepositoryDatabase) = dataBase.repositoryDao()

    @Singleton
    @Provides
    fun provideRemoteKeyDao(dataBase: RepositoryDatabase) = dataBase.remoteKeyDao()

    @ExperimentalPagingApi
    @Singleton
    @Provides
    fun provideMainRepository(
        apiService: ApiService,
        repositoryDao: RepositoryDao,
        remoteKeyDao: RemoteKeyDao
    ): GitRepoRepository = GitRepoRepositoryImpl(apiService, repositoryDao, remoteKeyDao)

}