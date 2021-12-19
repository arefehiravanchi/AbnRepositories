package com.abn.test.repositories

import androidx.paging.*
import com.abn.test.data.local.RemoteKeyDao
import com.abn.test.data.local.RepositoryDao
import com.abn.test.data.local.model.GitRepo
import com.abn.test.data.remote.ApiService
import com.abn.test.data.remote.response.RepoResponse
import com.abn.test.data.remote.response.mapToDbModel
import com.abn.test.util.Constants
import com.abn.test.util.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response
import java.lang.reflect.Type


@ExperimentalPagingApi
class GitRepositoryImplTest {

    private lateinit var gitRepository: GitRepoRepositoryImpl

    @Mock
    lateinit var apiService: ApiService

    @Mock
    lateinit var repositoryDao: RepositoryDao

    @Mock
    lateinit var remoteKeyDao: RemoteKeyDao

    private lateinit var mockedRepoList: List<RepoResponse>


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        gitRepository = GitRepoRepositoryImpl(apiService, repositoryDao, remoteKeyDao)

        val listType: Type = object : TypeToken<List<RepoResponse>>() {}.type
        mockedRepoList = Gson().fromJson(
            ClassLoader.getSystemResource("response").readText(), listType
        )
    }


    @Test
    fun fetchGitRepo_Success() = runBlocking {
        Mockito.`when`(apiService.getRepositories(Constants.FIRST_PAGE_OFFSET, Constants.PAGE_SIZE))
            .thenReturn(Response.success(mockedRepoList))

        val response = gitRepository.fetchGitRepo(Constants.FIRST_PAGE_OFFSET)
        Assert.assertTrue(response is Result.Success)
        Assert.assertTrue(response.isSuccess())
        Assert.assertTrue((response as Result.Success).data.isNotEmpty())
        Assert.assertEquals(response.data,mockedRepoList.map { it.mapToDbModel() })
    }

    @Test
    fun fetchGitRepo_Error() = runBlocking {
        Mockito.`when`(apiService.getRepositories(Constants.FIRST_PAGE_OFFSET, Constants.PAGE_SIZE))
            .thenReturn(Response.error(404, ResponseBody.create(MediaType.parse("application/json"), "Error")))

        val response = gitRepository.fetchGitRepo(Constants.FIRST_PAGE_OFFSET)
        Assert.assertTrue(response is Result.Error)
        Assert.assertFalse(response.isSuccess())
    }

}