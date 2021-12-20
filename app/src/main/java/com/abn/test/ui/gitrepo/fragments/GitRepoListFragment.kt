package com.abn.test.ui.gitrepo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.abn.test.R
import com.abn.test.databinding.FragmentRepoListBinding
import com.abn.test.ui.gitrepo.adapter.GitRepoAdapter
import com.abn.test.ui.gitrepo.GitRepoViewModel
import com.abn.test.util.PagingLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@AndroidEntryPoint
class GitRepoListFragment : Fragment(R.layout.fragment_repo_list) {

    private val gitRepoViewModel: GitRepoViewModel by viewModels()

    private lateinit var repoAdapter: GitRepoAdapter
    private val footer = PagingLoadStateAdapter()
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
        initView()
        observe()
    }

    private fun initView() {
        repoAdapter = GitRepoAdapter { id ->
            val action = GitRepoListFragmentDirections.openRepoDetail(id)
            findNavController().navigate(action)
        }
        binding.repoList.adapter = repoAdapter.withLoadStateFooter(footer)
    }

    private fun observe() {
        with(gitRepoViewModel) {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                repoFlow.collectLatest {
                    repoAdapter.submitData(it)
                }
            }

            error.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            repoAdapter.loadStateFlow.collectLatest { loadState ->
                updateUi(loadState)
            }
        }

    }

    private fun updateUi(loadState: CombinedLoadStates) {
        val isListEmpty = loadState.refresh is LoadState.NotLoading && repoAdapter.itemCount == 0
        binding.repoList.isVisible = !isListEmpty
        binding.repoPb.isVisible = loadState.mediator?.refresh is LoadState.Loading

        val errorMessage = loadState.refresh as? LoadState.Error
            ?: loadState.append as? LoadState.Error
        errorMessage?.let {
            Toast.makeText(requireContext(), "Error! ${it.error}", Toast.LENGTH_LONG).show()
        }
    }
}