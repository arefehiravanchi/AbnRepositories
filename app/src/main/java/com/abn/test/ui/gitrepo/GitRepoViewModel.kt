package com.abn.test.ui.gitrepo

import androidx.lifecycle.*
import androidx.paging.*
import com.abn.test.data.local.model.mapToDetailItem
import com.abn.test.data.local.model.mapToListItem
import com.abn.test.repositories.GitRepoRepository
import com.abn.test.ui.gitrepo.model.GitRepoDetailItem
import com.abn.test.ui.gitrepo.model.GitRepoListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class GitRepoViewModel @Inject constructor(private val gitRepoRepository: GitRepoRepository) :
    ViewModel() {

    init {
        getRepositories()
    }

    private var _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private lateinit var _repoFlow: Flow<PagingData<GitRepoListItem>>
    val repoFlow: Flow<PagingData<GitRepoListItem>>
        get() = _repoFlow

    private fun getRepositories() = viewModelScope.launch {
        try {
            _repoFlow = gitRepoRepository.getAllRepos().map {
                it.map { repoList ->
                    repoList.mapToListItem()
                }
            }.cachedIn(viewModelScope)
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

    private val _singleRepo = MutableLiveData<GitRepoDetailItem>()
    val singleRepo: LiveData<GitRepoDetailItem> = _singleRepo

    fun getRepoDetails(id: Long) {
        viewModelScope.launch {
            val repo = gitRepoRepository.getRepoById(id)
            _singleRepo.value = repo.mapToDetailItem()
        }
    }
}