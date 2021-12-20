package com.abn.test.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.abn.test.util.Constants

@Entity(tableName = Constants.TABLE_REMOTE_KEY)
data class RemoteKey(
    @PrimaryKey var repoId: Long,
    val prevKey: Int?,
    val nextKey: Int?
    )