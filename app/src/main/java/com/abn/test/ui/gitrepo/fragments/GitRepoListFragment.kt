package com.abn.test.ui.gitrepo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abn.test.R
import com.abn.test.databinding.FragmentRepoListBinding
import com.abn.test.ui.gitrepo.adapter.GitRepoAdapter
import com.abn.test.ui.gitrepo.GitRepoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@AndroidEntryPoint
class GitRepoListFragment : Fragment(R.layout.fragment_repo_list) {

    private val gitRepoViewModel: GitRepoViewModel by viewModels()

    private lateinit var binding: FragmentRepoListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepoListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repoAdapter = GitRepoAdapter { id ->
            val action = GitRepoListFragmentDirections.openRepoDetail(id)
            findNavController().navigate(action)
        }

        binding.repoList.apply {
            adapter = repoAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }

        lifecycleScope.launch {
            gitRepoViewModel.getRepositories().collectLatest {
                repoAdapter.submitData(it)
            }
        }

    }

}