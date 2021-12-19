package com.abn.test.ui.gitrepo

import androidx.lifecycle.*
import androidx.paging.*
import com.abn.test.data.local.model.mapToDetailItem
import com.abn.test.data.local.model.mapToListItem
import com.abn.test.repositories.GitRepoRepository
import com.abn.test.ui.gitrepo.model.GitRepoDetailItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class GitRepoViewModel @Inject constructor(private val gitRepoRepository: GitRepoRepository) :
    ViewModel() {

    fun getRepositories() = gitRepoRepository.getAllRepos().map {
        it.map { repoList ->
            repoList.mapToListItem()
        }
    }.cachedIn(viewModelScope)


    private val _repo = MutableLiveData<GitRepoDetailItem>()
    val repo: LiveData<GitRepoDetailItem> = _repo

    fun getRepoDetails(id: Long) {
        viewModelScope.launch {
            val repo = gitRepoRepository.getRepoById(id)
            _repo.value = repo.mapToDetailItem()
        }
    }
}