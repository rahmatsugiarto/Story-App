package com.rs.storyapp.utils

import com.rs.storyapp.data.local.database.StoryEntity
import com.rs.storyapp.model.request.RequestLogin
import com.rs.storyapp.model.request.RequestSignUp
import com.rs.storyapp.model.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Created by Rahmat Sugiarto on 21/10/2022
 */
object DataDummy {
    fun generateDummyListStoryEntity(): List<StoryEntity> {
        val items: MutableList<StoryEntity> = arrayListOf()

        for (i in 0..100) {
            val story = StoryEntity(
                createdAt = "2022-10-21T08:22:39.243Z",
                description = "galpot",
                id = "story-n2o969c3NJARQEp3",
                lat = i.toDouble(),
                lon = i.toDouble(),
                name = "rev",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1666339533827_MAvCeeoa.jpg",
            )
            items.add(story)
        }
        return items
    }

    fun generateDummyStoryResponse(): StoryResponse {
        val items: MutableList<Story> = arrayListOf()

        for (i in 0..100) {
            val story = Story(
                createdAt = "2022-10-21T08:22:39.243Z",
                description = "galpot",
                id = "story-n2o969c3NJARQEp3",
                lat = i.toDouble(),
                lon = i.toDouble(),
                name = "rev",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1666339533827_MAvCeeoa.jpg",
            )
            items.add(story)
        }

        return StoryResponse(false, items, "Stories fetched successfully")
    }

    fun generateDummyLoginResponse(): LoginResponse {
        val loginResult = LoginResult(
            userId = "user-0DySULI_mKrYwe1R",
            name = "rahmatdev",
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTBEeVNVTElfbUtyWXdlMVIiLCJpYXQiOjE2NjYzNDA2ODB9.4qPa0XWqlAKc0Yte2QJ9v9smSxsWAJi97rp7HytNAK8"
        )

        return LoginResponse(
            loginResult = loginResult,
            error = false,
            message = "success"
        )
    }

    fun generateDummyRequestLogin(): RequestLogin {
        return RequestLogin("mail@mail.com", "password")
    }

    fun generateDummyRequestSignUp(): RequestSignUp {
        return RequestSignUp("Name", "mail@mail.com", "password")
    }

    fun generateDummyMessageResponse(): MessageResponse {
        return MessageResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyMultipartFile(): MultipartBody.Part {
        val dummyText = "text"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

    fun generateDummyDescRequestBody(): RequestBody {
        val dummyText = "text"
        return dummyText.toRequestBody()
    }
}