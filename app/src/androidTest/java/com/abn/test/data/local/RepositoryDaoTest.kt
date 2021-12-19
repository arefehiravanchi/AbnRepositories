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
        val sampleList = arrayListOf<GitRepo>()
        sampleList.add(FakeRepoItem.getFakeRepoItem())
        for (i in 0..5) {
            val item = FakeRepoItem.getFakeRepoItem()
            //to change primary key values
            item.id = i.toLong()
            sampleList.add(item)
        }

        dao.insertAll(sampleList)
        val response = dao.observeAll().getOrAwaitValue()

        assertThat(response).contains(FakeRepoItem.getFakeRepoItem())
        assertThat(response.size).isEqualTo(sampleList.size)

    }

    @Test
    fun getRepoByIdTest() = runBlockingTest {
        val sampleItem = FakeRepoItem.getFakeRepoItem()
        val sampleList = arrayListOf<GitRepo>()
        sampleList.add(sampleItem)
        dao.insertAll(sampleList)

        val repo = dao.getRepoById(sampleItem.id)
        Assert.assertNotNull(repo)
        assertThat(repo).isEqualTo(sampleItem)

    }

    @Test
    fun clearRepoTest() = runBlockingTest {
        val sampleItem = FakeRepoItem.getFakeRepoItem()
        val sampleList = arrayListOf<GitRepo>()
        sampleList.add(sampleItem)
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