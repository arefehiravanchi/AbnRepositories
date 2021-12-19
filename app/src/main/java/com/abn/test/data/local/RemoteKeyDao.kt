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
    suspend fun insert(remoteKey: RemoteKey)

    @Query("Select * From ${Constants.TABLE_REMOTE_KEY} WHERE keyName= :name")
    suspend fun getRemoteKey(name: String): RemoteKey?

    @Query("DELETE FROM ${Constants.TABLE_REMOTE_KEY}")
    suspend fun clearRemoteKeys()
}