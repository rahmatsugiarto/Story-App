package com.rs.storyapp.data.local.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Created by Rahmat Sugiarto on 17/10/2022
 */

@Parcelize
@Entity(tableName = "db_story")
data class StoryEntity(

    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "photo_url")
    val photoUrl: String,

    @ColumnInfo(name = "created_at")
    val createdAt: String,

    @ColumnInfo(name = "lat")
    val lat: Double?,

    @ColumnInfo(name = "lon")
    val lon: Double?
) : Parcelable