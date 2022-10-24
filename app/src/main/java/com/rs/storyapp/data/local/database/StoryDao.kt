package com.rs.storyapp.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by Rahmat Sugiarto on 17/10/2022
 */
@Dao
interface StoryDao {
    @Query("SELECT * FROM db_story")
    fun getAllStories(): PagingSource<Int, StoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(stories: List<StoryEntity>)

    @Query("DELETE FROM db_story")
    fun deleteAll()
}