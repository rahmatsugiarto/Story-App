package com.rs.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rs.storyapp.R
import com.rs.storyapp.data.local.database.StoryEntity
import com.rs.storyapp.databinding.RowStoryBinding
import com.rs.storyapp.ui.detailstory.DetailStoryActivity

/**
 * Created by Rahmat Sugiarto on 30/09/2022
 */
class StoryAdapter : PagingDataAdapter<StoryEntity, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(private val binding: RowStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: StoryEntity) {
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
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}