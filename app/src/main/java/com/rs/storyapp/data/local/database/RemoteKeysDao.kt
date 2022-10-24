package com.rs.storyapp.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by Rahmat Sugiarto on 17/10/2022
 */
@Dao
interface RemoteKeysDao {
    @Query("SELECT * FROM remote_key WHERE id = :id")
    suspend fun getRemoteKeysId(id: String): RemoteKeyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeyEntity: List<RemoteKeyEntity>)

    @Query("DELETE FROM remote_key")
    suspend fun deleteRemoteKeys()
}