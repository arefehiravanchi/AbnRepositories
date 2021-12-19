package com.abn.test.ui.gitrepo.model

data class GitRepoDetailItem(
    val id:Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val ownerAvatar: String,
    val visibility: String,
    val isPrivate: Boolean,
    val link:String
)