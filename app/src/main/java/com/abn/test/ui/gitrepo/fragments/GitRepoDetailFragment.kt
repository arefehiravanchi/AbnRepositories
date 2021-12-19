package com.abn.test.ui.gitrepo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.abn.test.R
import com.abn.test.databinding.FragmentRepoDetailBinding
import com.abn.test.di.GlideApp
import com.abn.test.ui.gitrepo.model.GitRepoDetailItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import android.content.Intent
import android.net.Uri
import com.abn.test.ui.gitrepo.GitRepoViewModel
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class GitRepoDetailFragment : Fragment(R.layout.fragment_repo_detail) {

    private lateinit var binding: FragmentRepoDetailBinding
    private val args by navArgs<GitRepoDetailFragmentArgs>()

    private val gitRepoViewModel: GitRepoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepoDetailBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gitRepoViewModel.getRepoDetails(args.repoId)
        observe()
    }

    private fun observe() {
        gitRepoViewModel.repo.observe(viewLifecycleOwner, { repo -> initView(repo) })
    }

    private fun initView(repo: GitRepoDetailItem) {
        binding.repoDetailName.text = repo.name
        binding.repoDetailFullname.text = repo.fullName

        binding.repoDetailPublic.setImageResource(R.drawable.unlock)
        if (repo.isPrivate) {
            binding.repoDetailPublic.setImageResource(R.drawable.padlock)
        }

        GlideApp.with(requireContext())
            .load(repo.ownerAvatar)
            .into(binding.repoDetailIv)

        binding.repoDetailDescription.text = repo.description
        binding.repoDetailVisibility.text = repo.visibility

        binding.repoDetailBtn.setOnClickListener {
            val webpage: Uri = Uri.parse(repo.link)
            val i = Intent(Intent.ACTION_VIEW, webpage)
            val chooser = Intent.createChooser(i, getString(R.string.browsers))
            if (i.resolveActivity(requireContext().packageManager) != null) {
                startActivity(chooser)
            }
        }
    }

}
