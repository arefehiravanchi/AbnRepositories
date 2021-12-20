package com.abn.test.repositories.page

import androidx.paging.*
import com.abn.test.data.local.RemoteKeyDao
import com.abn.test.data.local.RepositoryDao
import com.abn.test.data.local.model.GitRepo
import com.abn.test.data.local.model.RemoteKey
import com.abn.test.repositories.GitRepoRepository
import com.abn.test.util.Constants
import com.abn.test.util.Result
import timber.log.Timber


@ExperimentalPagingApi
class GitRepoRemoteMediator(
    private val gitRepository: GitRepoRepository,
    private val repositoryDao: RepositoryDao,
    private val remoteKeyDao: RemoteKeyDao,
) : RemoteMediator<Int, GitRepo>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GitRepo>
    ): MediatorResult {
        return try {
            val page: Int = when (loadType) {
                LoadType.REFRESH -> Constants.FIRST_PAGE_OFFSET
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKeys = remoteKeyDao.getRemoteKey(Constants.KEY_ABN_REPO)
                    if (remoteKeys?.nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    }
                    remoteKeys.nextKey
                }
            }
            val response = gitRepository.fetchGitRepo(page)
            if (response.isSuccess()) {
                val repoList = (response as Result.Success<List<GitRepo>>).data
                val endOfPaginationReached = repoList.isEmpty()

                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.clearRemoteKeys()
                    repositoryDao.clearRepos()
                }

                val previousKey = if (page == Constants.FIRST_PAGE_OFFSET) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                repositoryDao.insertAll(repoList)
                remoteKeyDao.insert(RemoteKey(Constants.KEY_ABN_REPO, previousKey, nextKey))
                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } else {
                MediatorResult.Error((response as Result.Error).exception)
            }

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

}