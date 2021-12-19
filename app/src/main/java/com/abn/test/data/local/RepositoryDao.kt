package com.abn.test.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abn.test.data.local.model.GitRepo
import com.abn.test.util.Constants
import java.util.*

@Dao
interface RepositoryDao {

    @Query("Select * From ${Constants.TABLE_REPOSITORY}")
    fun getAll(): PagingSource<Int,GitRepo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repositories: List<GitRepo>)

    @Query("Select * From ${Constants.TABLE_REPOSITORY}")
    fun observeAll(): LiveData<List<GitRepo>>

    @Query("SELECT * FROM ${Constants.TABLE_REPOSITORY} WHERE id= :id")
    suspend fun getRepoById(id:Long) : GitRepo

    @Query("DELETE FROM ${Constants.TABLE_REPOSITORY}")
    suspend fun clearRepos()




}