package com.abn.test.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abn.test.data.local.model.RemoteKey
import com.abn.test.util.Constants

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<RemoteKey>)

    @Query("Select * From ${Constants.TABLE_REMOTE_KEY} WHERE repoId= :id")
    suspend fun getRemoteKey(id: Long): RemoteKey?

    @Query("DELETE FROM ${Constants.TABLE_REMOTE_KEY}")
    suspend fun clearRemoteKeys()
}