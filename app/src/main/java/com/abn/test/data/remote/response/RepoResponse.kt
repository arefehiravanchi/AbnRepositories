package com.abn.test.data.remote.response

import com.abn.test.data.local.model.GitRepo
import com.google.gson.annotations.SerializedName

data class RepoResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("private") val private: Boolean,
    @SerializedName("description") val description: String?,
    @SerializedName("visibility") val visibility: String,
    @SerializedName("html_url") val htmlUrl: String,
    @SerializedName("owner") val owner: Owner
)


data class Owner(@SerializedName("avatar_url") val avatarUrl: String)

fun RepoResponse.mapToDbModel(): GitRepo {
    return GitRepo(
        id,
        name,
        fullName,
        private,
        description,
        visibility,
        htmlUrl,
        owner.avatarUrl
    )
}