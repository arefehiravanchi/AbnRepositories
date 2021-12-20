package com.abn.test.data.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.abn.test.data.getOrAwaitValue
import com.abn.test.data.local.model.GitRepo
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*

@ExperimentalCoroutinesApi
@SmallTest
class RepositoryDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: RepositoryDatabase
    private lateinit var dao: RepositoryDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            RepositoryDatabase::class.java
        ).build()
        dao = db.repositoryDao()
    }


    @Test
    fun insertRepoItemsTest() = runBlockingTest {
        val fakeItems = FakeRepoItem.getFakeRepoItems()
        dao.insertAll(fakeItems)
        val response = dao.observeAll().getOrAwaitValue()

        Assert.assertTrue(response.any { it.id == fakeItems[0].id })
        assertThat(response.size).isEqualTo(fakeItems.size)

    }

    @Test
    fun getRepoByIdTest() = runBlockingTest {
        val sampleItems = FakeRepoItem.getFakeRepoItems()
        dao.insertAll(sampleItems)

        val repo = dao.getRepoById(sampleItems[0].id)
        Assert.assertNotNull(repo)
        assertThat(repo.id).isEqualTo(sampleItems[0].id)

    }

    @Test
    fun clearRepoTest() = runBlockingTest {
        val sampleItem = FakeRepoItem.getFakeRepoItems()
        val sampleList = arrayListOf<GitRepo>()
        sampleList.add(sampleItem[0])
        dao.insertAll(sampleList)
        dao.clearRepos()
        val response = dao.observeAll().getOrAwaitValue()

        assertThat(response).isEmpty()
    }


    @After
    fun tearDown() {
        db.close()
    }


}