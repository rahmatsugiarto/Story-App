package com.rs.storyapp.ui.liststory

import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.rs.storyapp.common.util.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
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
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }


//    @Test
//    fun loadHeadlineNews_Success() {
//        onView(withId(R.id.rv_story)).check(matches(isDisplayed()))
//        onView(withId(R.id.rv_story)).perform(
//            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
//                10
//            )
//        )
//    }


}