 package com.rs.storyapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.rs.storyapp.adapter.StoryAdapter
import com.rs.storyapp.data.local.database.StoryEntity
import com.rs.storyapp.data.repository.AuthRepository
import com.rs.storyapp.data.repository.StoryRepository
import com.rs.storyapp.utils.DataDummy
import com.rs.storyapp.utils.MainDispatcherRule
import com.rs.storyapp.utils.StoryPagingSource
import com.rs.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Created by Rahmat Sugiarto on 21/10/2022
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListStoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var listStoryViewModel: ListStoryViewModel
    private val dummyToken = "token"

    @Before
    fun setUp() {
        listStoryViewModel = ListStoryViewModel(authRepository, storyRepository)
    }

    @Test
    fun `when Get Story Should Not Null and Return Success`() = runTest {
        val dummyStory = DataDummy.generateDummyListStoryEntity()
        val data: PagingData<StoryEntity> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<StoryEntity>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getStories(dummyToken)).thenReturn(expectedStory)

        val actualStory: PagingData<StoryEntity> =
            listStoryViewModel.getStories(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory, differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0].name, differ.snapshot()[0]?.name)
    }

    @Test
    fun `when resetUserData ensures that the function is running`() = runTest {
        listStoryViewModel.resetUserData()
        Mockito.verify(authRepository).deleteUser()
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}