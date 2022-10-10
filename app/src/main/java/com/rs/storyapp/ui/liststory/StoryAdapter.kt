package com.rs.storyapp.ui.liststory

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import androidx.core.util.Pair
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rs.storyapp.R
import com.rs.storyapp.common.util.DiffUtilStories
import com.rs.storyapp.databinding.RowStoryBinding
import com.rs.storyapp.model.response.Story
import com.rs.storyapp.ui.detailstory.DetailStoryActivity

/**
 * Created by Rahmat Sugiarto on 30/09/2022
 */
class StoryAdapter : RecyclerView.Adapter<StoryAdapter.MyViewHolder>() {
    private var stories = emptyList<Story>()

    class MyViewHolder(private val binding: RowStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: Story) {
            binding.tvItemName.text = story.name
            binding.ivItemPhoto.load(story.photoUrl) {
                crossfade(true)
                placeholder(R.drawable.icon_story_app)
            }

            binding.constraintListStory.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivItemPhoto, "detail_photo"),
                    )
                Intent(binding.root.context, DetailStoryActivity::class.java).also {
                    it.putExtra(DetailStoryActivity.DETAIL_STORY, story)
                    binding.root.context.startActivity(it, optionsCompat.toBundle())
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowStoryBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentStory = stories[position]
        holder.bind(currentStory)
    }

    override fun getItemCount(): Int = stories.size

    fun setData(newData: List<Story>) {
        val recipesDiffUtil = DiffUtilStories(stories, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        stories = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

}