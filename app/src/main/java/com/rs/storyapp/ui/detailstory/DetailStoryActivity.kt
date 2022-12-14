package com.rs.storyapp.ui.detailstory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.rs.storyapp.R
import com.rs.storyapp.data.local.database.StoryEntity
import com.rs.storyapp.databinding.ActivityDetailStoryBinding


class DetailStoryActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailStoryBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val dataStory: StoryEntity? = intent.getParcelableExtra(DETAIL_STORY)

        binding.apply {
            tvDetailName.text = dataStory?.name
            ivDetailPhoto.load(dataStory?.photoUrl) {
                crossfade(true)
                placeholder(R.drawable.icon_story_app)
            }
            tvDetailDescription.text = dataStory?.description
        }
    }

    companion object {
        const val DETAIL_STORY = "detail_story"
    }
}