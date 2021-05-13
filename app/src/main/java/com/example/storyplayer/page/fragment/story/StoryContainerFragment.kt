package com.example.storyplayer.page.fragment.story

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.storyplayer.R
import com.example.storyplayer.adapter.StoryAdapter
import com.example.storyplayer.adapter.StoryRecyclerAdapter
import com.example.storyplayer.adapter.StoryViewPagerAdapter
import com.example.storyplayer.listener.FragmentChangeListener
import com.example.storyplayer.model.StoryResponse
import com.example.storyplayer.model.User
import kotlin.properties.Delegates

class StoryContainerFragment: Fragment(), StoryViewPagerAdapter.StoryDetailClickListener {

    private lateinit var adapter: StoryAdapter
    private lateinit var adapter2: StoryViewPagerAdapter
    private lateinit var viewpager: ViewPager2
    private lateinit var fragmentChangeListener: FragmentChangeListener
    private lateinit var users: StoryResponse
    private var currentUser by Delegates.notNull<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setFragmentChangeListener(activity as FragmentChangeListener)

    }
    private fun setFragmentChangeListener(fragmentChangeListener: FragmentChangeListener) {
        this.fragmentChangeListener = fragmentChangeListener
    }
    private fun initViews(root: View) {
        users = requireArguments().getSerializable("users") as StoryResponse
        currentUser = requireArguments().getInt("current_user")

        viewpager = root.findViewById(R.id.view_pager)
        viewpager.post {
            viewpager.setCurrentItem(currentUser, true)
        }
        //adapter = StoryAdapter(users.users, requireContext(), this)
        //viewpager.adapter = adapter
        adapter2 = StoryViewPagerAdapter(users.users,this,this)
        viewpager.adapter = adapter2
        viewpager.setPageTransformer(ViewPager2.PageTransformer(){ view: View, fl: Float ->
            view.cameraDistance = 20000F
            if(fl < -1){
                view.alpha = 0F
            }
            else if(fl <= 0){
                view.alpha = 1F
                view.pivotX = view.width.toFloat()
                view.rotationY  = -90*Math.abs(fl)
            }
            else if(fl <= 1){
                view.alpha = 1F
                view.pivotX = 0F
                view.rotationY  = 90*Math.abs(fl)
            }
            else
            {
                view.alpha = 0F
            }

        })
    }

    override fun cancelListener() {
        fragmentChangeListener.destroyCurrentFragment()
    }
}