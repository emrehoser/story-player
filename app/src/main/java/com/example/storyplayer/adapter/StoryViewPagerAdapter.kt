package com.example.storyplayer.adapter

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.storyplayer.model.User
import com.example.storyplayer.page.fragment.story.StoryFragment

class StoryViewPagerAdapter(
    private val users: ArrayList<User>,
    fragment: Fragment,
    private val itemClick: StoryDetailClickListener
    ): FragmentStateAdapter(fragment){

    interface StoryDetailClickListener{
        fun cancelListener()
        fun nextUserStory()
        fun previousUserStory()
    }

    override fun getItemCount(): Int {
        return users.count()
    }


    override fun createFragment(position: Int): Fragment {
        return StoryFragment(users[position], itemClick)
    }


}