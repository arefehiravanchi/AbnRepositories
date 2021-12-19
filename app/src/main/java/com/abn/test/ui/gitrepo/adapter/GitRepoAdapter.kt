package com.abn.test.ui.gitrepo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.abn.test.R
import com.abn.test.di.GlideApp
import com.abn.test.ui.gitrepo.model.GitRepoListItem

class GitRepoAdapter(val onItemClick: (Long) -> Unit) :
    PagingDataAdapter<GitRepoListItem, GitRepoAdapter.RepositoryViewHolder>(diffCallback) {
    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        return RepositoryViewHolder(parent)
    }

    inner class RepositoryViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_gitrepo, parent, false)
    ) {
        private val name = itemView.findViewById<TextView>(R.id.repo_name)
        private val avatar = itemView.findViewById<ImageView>(R.id.repo_iv)
        private val visibility = itemView.findViewById<TextView>(R.id.repo_visibility)
        private val publicity = itemView.findViewById<ImageView>(R.id.repo_lock)
        private val container = itemView.findViewById<ConstraintLayout>(R.id.container)
        fun bindTo(item: GitRepoListItem?) {

            item?.let { repo ->
                name.text = repo.name
                visibility.text = item.visibility

                if (item.isPrivate) {
                    publicity.setImageResource(R.drawable.padlock)
                } else {
                    publicity.setImageResource(R.drawable.unlock)
                }

                GlideApp.with(itemView)
                    .load(item.ownerAvatar)
                    .into(avatar)

                container.setOnClickListener {
                    onItemClick.invoke(item.id)
                }
            }
        }

    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<GitRepoListItem>() {
            override fun areItemsTheSame(
                oldItem: GitRepoListItem,
                newItem: GitRepoListItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: GitRepoListItem,
                newItem: GitRepoListItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

