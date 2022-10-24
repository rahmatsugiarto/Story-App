package com.rs.storyapp.utils

import androidx.paging.PagingSource
import com.rs.storyapp.data.local.database.StoryDao
import com.rs.storyapp.data.local.database.StoryEntity

/**
 * Created by Rahmat Sugiarto on 21/10/2022
 */
class FakeStoryDao : StoryDao {
    private var storyData = mutableListOf<StoryEntity>()

    override fun getAllStories(): PagingSource<Int, StoryEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun insertStory(stories: List<StoryEntity>) {
        stories.map { value ->
            storyData.add(value)
        }

    }

    override fun deleteAll() {
        storyData.clear()
    }
}