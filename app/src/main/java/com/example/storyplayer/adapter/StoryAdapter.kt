package com.example.storyplayer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyplayer.R
import com.example.storyplayer.model.User


class StoryAdapter (
    private val users: ArrayList<User>,
    private val context: Context,
    private val itemClick: StoryDetailClickListener
    ): RecyclerView.Adapter<StoryAdapter.ViewHolder>(){

    interface StoryDetailClickListener{
        fun cancelListener()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_story, parent, false)
        )

    }
    override fun onBindViewHolder(holder: StoryAdapter.ViewHolder, position: Int) {
        val item = users[position]
        Glide.with(context).load(item.stories[0].url).into(holder.storyImage)
        holder.storyImage.visibility = View.VISIBLE
        //TODO: make video visible
        holder.bindData(position, itemClick)
    }

    override fun getItemCount(): Int {
        return users.count()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val storyImage = itemView.findViewById<ImageView>(R.id.story_image)
        val cancelButton = itemView.findViewById<ImageView>(R.id.cancel_button)

        fun bindData(position: Int, clickListener: StoryDetailClickListener){
            cancelButton.setOnClickListener{
                itemClick.cancelListener()
            }
        }
    }


}