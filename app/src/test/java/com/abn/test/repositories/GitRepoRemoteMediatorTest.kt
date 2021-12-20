package com.abn.test.repositories

import androidx.paging.*
import com.abn.test.data.local.RemoteKeyDao
import com.abn.test.data.local.RepositoryDao
import com.abn.test.data.local.model.GitRepo
import com.abn.test.data.local.model.RemoteKey
import com.abn.test.data.remote.response.RepoResponse
import com.abn.test.data.remote.response.mapToDbModel
import com.abn.test.repositories.page.GitRepoRemoteMediator
import com.abn.test.util.Constants
import com.abn.test.util.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.Exception
import java.lang.reflect.Type
import androidx.paging.RemoteMediator

import androidx.paging.LoadType
import org.junit.Assert.assertFalse


@ExperimentalPagingApi
class GitRepoRemoteMediatorTest {

    @Mock
    lateinit var gitRepository: GitRepoRepository

    @Mock
    lateinit var gitRepositoryDao: RepositoryDao

    @Mock
    lateinit var remoteKeyDao: RemoteKeyDao

    private lateinit var mockedRemoteMediator: GitRepoRemoteMediator
    private lateinit var mockedPagedStatus: PagingState<Int, GitRepo>
    private lateinit var mockedRepoList: List<RepoResponse>

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        mockedRemoteMediator = GitRepoRemoteMediator(
            gitRepository, gitRepositoryDao, remoteKeyDao
        )

        mockedPagedStatus = PagingState(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val listType: Type = object : TypeToken<List<RepoResponse>>() {}.type
        mockedRepoList = Gson().fromJson(
            ClassLoader.getSystemResource("response").readText(), listType
        )
    }


    @Test
    fun loadData_SuccessResultInTheFirstOrRefreshLoad() = runBlocking {
        Mockito.`when`(gitRepository.fetchGitRepo(Constants.FIRST_PAGE_OFFSET))
            .thenReturn(Result.Success(data = mockedRepoList.map { it.mapToDbModel() }))

        val result = mockedRemoteMediator.load(LoadType.REFRESH, mockedPagedStatus)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }


    @Test
    fun loadData_SuccessInEndOfPaginationWhenNoMoreData() = runBlocking {
        Mockito.`when`(gitRepository.fetchGitRepo(Constants.FIRST_PAGE_OFFSET))
            .thenReturn(Result.Success(listOf()))

        val result = mockedRemoteMediator.load(LoadType.REFRESH, mockedPagedStatus)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertTrue((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)

    }

    @Test
    fun loadData_ErrorResultWhenErrorOccurs() = runBlocking {
        Mockito.`when`(gitRepository.fetchGitRepo(Constants.FIRST_PAGE_OFFSET))
            .thenReturn(Result.Error(Exception()))

        val result = mockedRemoteMediator.load(LoadType.REFRESH, mockedPagedStatus)
        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @Test
    fun loadData_SuccessInTheNextPageLoad() = runBlocking {
        val nextPage = 2
        val sampleId = 276828556L
        Mockito.`when`(remoteKeyDao.getRemoteKey(sampleId))
            .thenReturn(RemoteKey(sampleId, null, nextPage))

        Mockito.`when`(gitRepository.fetchGitRepo(nextPage))
            .thenReturn(Result.Success(data = mockedRepoList.map { it.mapToDbModel() }))

        val result = mockedRemoteMediator.load(LoadType.APPEND, mockedPagedStatus)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
    }

}