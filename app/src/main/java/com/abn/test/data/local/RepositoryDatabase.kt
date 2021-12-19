package com.abn.test.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.abn.test.data.local.model.GitRepo
import com.abn.test.data.local.model.RemoteKey

@Database(entities = [GitRepo::class, RemoteKey::class], version = 1, exportSchema = false)
abstract class RepositoryDatabase : RoomDatabase() {

    abstract fun repositoryDao(): RepositoryDao
    abstract fun remoteKeyDao(): RemoteKeyDao

}