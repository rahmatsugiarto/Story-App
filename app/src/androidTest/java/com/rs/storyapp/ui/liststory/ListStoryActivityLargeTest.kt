package com.rs.storyapp.ui.liststory

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.rs.storyapp.R
import com.rs.storyapp.common.util.EspressoIdlingResource
import com.rs.storyapp.data.remote.ApiConfig.BASE_URL
import com.rs.storyapp.ui.addstory.AddStoryActivity
import com.rs.storyapp.ui.detailstory.DetailStoryActivity
import com.rs.storyapp.ui.maps.MapsActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Created by Rahmat Sugiarto on 23/10/2022
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class ListStoryActivityLargeTest {

    @get:Rule
    val activity = ActivityScenarioRule(ListStoryActivity::class.java)

    @Before
    fun setUp() {
        BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }


    @Test
    fun launchListStoryActivity_Success() {
        onView(withId(R.id.topAppBar))
            .check(matches(isDisplayed()))

        onView(withId(R.id.fab_maps))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rv_story)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_story)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                10
            )
        )
    }

    @Test
    fun launchDetailStoryActivity_Success() {
        Intents.init()
        onView(withId(R.id.rv_story)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        Intents.intended(hasComponent(DetailStoryActivity::class.java.name))
        onView(withId(R.id.topAppBar)).check(matches(isDisplayed()))
        onView(withId(R.id.profile_image)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_name)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_detail_photo)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_description)).check(matches(isDisplayed()))
        ViewActions.pressBack()
        Intents.release()
    }

    @Test
    fun launchMapsActivity_Success() {
        Intents.init()
        onView(withId(R.id.fab_maps)).perform(ViewActions.click())
        Intents.intended(hasComponent(MapsActivity::class.java.name))
        onView(withId(R.id.map)).check(matches(isDisplayed()))
        Intents.release()
    }

    @Test
    fun launchAddStoryActivity_Success() {
        Intents.init()
        onView(withId(R.id.addStory)).perform(ViewActions.click())
        Intents.intended(hasComponent(AddStoryActivity::class.java.name))
        onView(withId(R.id.topAppBar)).check(matches(isDisplayed()))

        onView(withId(R.id.previewImageView)).check(matches(isDisplayed()))
        onView(withId(R.id.ed_add_description)).check(matches(isDisplayed()))
        onView(withId(R.id.sw_location)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_gallery)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_camera)).check(matches(isDisplayed()))
        onView(withId(R.id.button_add)).check(matches(isDisplayed()))
        ViewActions.pressBack()
        Intents.release()
    }


}