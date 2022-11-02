package com.rs.storyapp.ui.liststory

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.rs.storyapp.R
import com.rs.storyapp.common.util.EspressoIdlingResource
import com.rs.storyapp.data.remote.ApiConfig.BASE_URL
import com.rs.storyapp.utils.JsonConverter
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Rahmat Sugiarto on 23/10/2022
 */
@RunWith(AndroidJUnit4::class)
@MediumTest
class ListStoryActivityMediumTest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun launchLisStory_Success() {
        launchActivity<ListStoryActivity>()
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response.json"))

        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.topAppBar))
            .check(ViewAssertions.matches(isDisplayed()))

        onView(withId(R.id.rv_story))
            .check(ViewAssertions.matches(isDisplayed()))

        onView(withId(R.id.rv_story)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                10
            )
        )
    }
}