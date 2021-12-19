package com.abn.test.ui.gitrepo.model

data class GitRepoListItem(
    val id:Long,
    val name: String,
    val ownerAvatar: String,
    val visibility: String,
    val isPrivate: Boolean,
)