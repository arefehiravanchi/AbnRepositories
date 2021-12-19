package com.abn.test.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abn.test.ui.gitrepo.model.GitRepoDetailItem
import com.abn.test.ui.gitrepo.model.GitRepoListItem
import com.abn.test.util.Constants

@Entity(tableName = Constants.TABLE_REPOSITORY)
data class GitRepo(
    @PrimaryKey var id: Long,
    val name: String,
    val  fullName: String,
    val isPrivate: Boolean,
    val description: String?,
    val visibility: String,
    val htmlUrl: String,
    val ownerAvatar: String)


fun GitRepo.mapToListItem(): GitRepoListItem {
    return GitRepoListItem(id,name,ownerAvatar,visibility,isPrivate)
}

fun GitRepo.mapToDetailItem(): GitRepoDetailItem {
    return GitRepoDetailItem(id,name,fullName,description,ownerAvatar,visibility,isPrivate,htmlUrl)
}
