package com.abn.test.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import androidx.paging.*
import com.abn.test.MainCoroutineRule
import com.abn.test.data.local.model.GitRepo
import com.abn.test.data.local.model.mapToDetailItem
import com.abn.test.data.local.model.mapToListItem
import com.abn.test.data.remote.response.RepoResponse
import com.abn.test.data.remote.response.mapToDbModel
import com.abn.test.repositories.GitRepoRepository
import com.abn.test.ui.gitrepo.GitRepoViewModel
import com.abn.test.ui.gitrepo.model.GitRepoDetailItem
import com.abn.test.ui.gitrepo.model.GitRepoListItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.lang.reflect.Type

@ExperimentalCoroutinesApi
class GitRepoViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var gitRepoRepository: GitRepoRepository

    private lateinit var mockRepoLisItems: List<GitRepoListItem>
    private lateinit var mockListRepo: List<GitRepo>

    private lateinit var gitRepoViewModel: GitRepoViewModel

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        val listType: Type = object : TypeToken<List<RepoResponse>>() {}.type
        val responseList = Gson().fromJson<List<RepoResponse>>(
            ClassLoader.getSystemResource("response").readText(), listType
        )
        mockListRepo = responseList.map { it.mapToDbModel() }
        mockRepoLisItems = mockListRepo.map { it.mapToListItem() }

        gitRepoViewModel = GitRepoViewModel(gitRepoRepository)
    }


    //There is a bug here: https://github.com/Kotlin/kotlinx.coroutines/issues/1204
    @Test
    fun checkRepositoryValue() = runBlockingTest{
        Mockito.`when`(gitRepoRepository.getAllRepos())
            .thenReturn(flow{ PagingData.from(mockListRepo) })

        val expected =  mockRepoLisItems
        val actual = gitRepoViewModel.repoFlow.take(1).toList().first().collectDataForTest()
        Assert.assertEquals(expected,actual)
    }

    @Mock
    lateinit var observer: Observer<GitRepoDetailItem>

    @Test
    fun getRepoDetailsTest() = runBlockingTest {
        val fakeItem = FakeRepoItem.getFakeRepoItem()
        Mockito.`when`(gitRepoRepository.getRepoById(fakeItem.id))
            .thenReturn(fakeItem)

        gitRepoViewModel.singleRepo.observeForever(observer)
        gitRepoViewModel.getRepoDetails(fakeItem.id)
        Mockito.verify(observer).onChanged(fakeItem.mapToDetailItem())

    }


    private suspend fun <T : Any> PagingData<T>.collectDataForTest(): List<T> {
        val dcb = object : DifferCallback {
            override fun onChanged(position: Int, count: Int) {}
            override fun onInserted(position: Int, count: Int) {}
            override fun onRemoved(position: Int, count: Int) {}
        }
        val items = mutableListOf<T>()
        val dif = object : PagingDataDiffer<T>(dcb, TestCoroutineDispatcher()) {
            override suspend fun presentNewList(
                previousList: NullPaddedList<T>,
                newList: NullPaddedList<T>,
                lastAccessedIndex: Int,
                onListPresentable: () -> Unit
            ): Int? {
                for (idx in 0 until newList.size)
                    items.add(newList.getFromStorage(idx))
                onListPresentable()
                return null
            }
        }
        dif.collectFrom(this)
        return items
    }
}

