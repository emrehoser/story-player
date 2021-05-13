package com.example.storyplayer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyplayer.R
import com.example.storyplayer.model.User
import de.hdodenhof.circleimageview.CircleImageView

class StoryRecyclerAdapter(
    val users: ArrayList<User>,
    private val context: Context,
    private val itemClick: StoryClickListener
): RecyclerView.Adapter<StoryRecyclerAdapter.ViewHolder>() {

    interface StoryClickListener{
        fun storyClicked(index: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_story, parent, false))

    }

    override fun getItemCount(): Int {
        return users.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = users[position]
        holder.username.text = item.username
        Glide.with(context).load(item.profilePicture).into(holder.profilePhoto)
        holder.bindData(position, itemClick)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val isSeen = itemView.findViewById<ImageView>(R.id.is_seen_circle)
        val profilePhoto = itemView.findViewById<CircleImageView>(R.id.row_story_profile)
        val username = itemView.findViewById<TextView>(R.id.username)

        fun bindData(position: Int, clickListener: StoryClickListener){
            itemView.setOnClickListener {
                clickListener.storyClicked(position)
                isSeen.setImageResource(R.drawable.seen_story_circle)
            }
        }
    }
}


