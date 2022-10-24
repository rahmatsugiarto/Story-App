package com.rs.storyapp.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Rahmat Sugiarto on 17/10/2022
 */
@Entity(tableName = "remote_key")
data class RemoteKeyEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "prev_key")
    val prevKey: Int?,

    @ColumnInfo(name = "next_key")
    val nextKey: Int?
)
