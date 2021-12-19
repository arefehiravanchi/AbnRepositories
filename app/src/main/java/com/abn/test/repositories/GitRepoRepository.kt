package com.abn.test.repositories

import androidx.paging.PagingData
import com.abn.test.data.local.model.GitRepo
import com.abn.test.util.Result
import kotlinx.coroutines.flow.Flow

interface GitRepoRepository {

    suspend fun fetchGitRepo(page: Int): Result<List<GitRepo>>

    fun getAllRepos(): Flow<PagingData<GitRepo>>

    suspend fun getRepoById(id: Long): GitRepo

}