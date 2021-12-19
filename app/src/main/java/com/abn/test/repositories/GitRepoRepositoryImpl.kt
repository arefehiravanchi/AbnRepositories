package com.abn.test.repositories

import androidx.paging.*
import com.abn.test.data.local.RemoteKeyDao
import com.abn.test.data.local.RepositoryDao
import com.abn.test.data.local.model.GitRepo
import com.abn.test.data.remote.ApiService
import com.abn.test.data.remote.response.mapToDbModel
import com.abn.test.repositories.page.GitRepoRemoteMediator
import com.abn.test.util.Constants
import com.abn.test.util.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@ExperimentalPagingApi
@Singleton
class GitRepoRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val repositoryDao: RepositoryDao,
    private val remoteKeyDao: RemoteKeyDao,
) : GitRepoRepository {

    override fun getAllRepos(): Flow<PagingData<GitRepo>> {
        return Pager(
            config = PagingConfig(Constants.PAGE_SIZE, enablePlaceholders = true),
            remoteMediator = GitRepoRemoteMediator(this, repositoryDao, remoteKeyDao),
            pagingSourceFactory = {
                repositoryDao.getAll()
            }
        ).flow
    }

    override suspend fun getRepoById(id: Long): GitRepo = repositoryDao.getRepoById(id)


    override suspend fun fetchGitRepo(page: Int): Result<List<GitRepo>> =
        try {
            val response = apiService.getRepositories(page)
            if (response.isSuccessful) {
                response.body()?.let { serverList ->
                    val repoList = serverList.map { it.mapToDbModel() }
                    Result.Success(repoList)
                } ?: Result.Error(Exception("Empty Result"))
            } else {
                Result.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }

}