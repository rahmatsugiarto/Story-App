package com.rs.storyapp

import com.rs.storyapp.data.local.database.StoryEntity

/**
 * Created by Rahmat Sugiarto on 21/10/2022
 */
object DataDummy {
    fun generateDummyStoryResponse(): List<StoryEntity> {
        val items: MutableList<StoryEntity> = arrayListOf()

        for (i in 0..100) {
            val story = StoryEntity(
                createdAt = "createdAt $i",
                description = "description $i",
                id = "id $i",
                lat = i.toDouble(),
                lon = i.toDouble(),
                name = "name $i",
                photoUrl = "photoUrl $i",
            )
            items.add(story)
        }
        return items
    }
}