package com.example.storyplayer.page.fragment.story

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.storyplayer.R
import com.example.storyplayer.adapter.StoryViewPagerAdapter
import com.example.storyplayer.listener.FragmentChangeListener
import com.example.storyplayer.model.StoryResponse
import kotlin.math.abs
import kotlin.properties.Delegates

class StoryContainerFragment: Fragment(), StoryViewPagerAdapter.StoryDetailClickListener {

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

        val callback: OnPageChangeCallback = object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        }

        viewpager = root.findViewById(R.id.view_pager)
        viewpager.offscreenPageLimit = users.users.size
        //viewpager.offscreenPageLimit = 1
        adapter2 = StoryViewPagerAdapter(users.users,this,this)
        viewpager.adapter = adapter2
        viewpager.registerOnPageChangeCallback(callback)
        viewpager.setCurrentItem(currentUser,false)
        viewpager.unregisterOnPageChangeCallback(callback)
        /*
        viewpager.doOnAttach {
            viewpager.setCurrentItem(currentUser,false)
        }
         */
        /*
        Handler().postDelayed({
            viewpager.setCurrentItem(currentUser, false)
        }, 50)

         */
        /*
        viewpager.post {
            viewpager.setCurrentItem(currentUser, false)
        }
         */

        viewpager.setPageTransformer(ViewPager2.PageTransformer(){ view: View, fl: Float ->
            view.cameraDistance = 20000F
            if(fl < -1){
                view.alpha = 0F
            }
            else if(fl <= 0){
                view.alpha = 1F
                view.pivotX = view.width.toFloat()
                view.rotationY  = -90* abs(fl)
            }
            else if(fl <= 1){
                view.alpha = 1F
                view.pivotX = 0F
                view.rotationY  = 90* abs(fl)
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

    override fun nextUserStory() {
        if (viewpager.currentItem != users.users.size-1){
            viewpager.currentItem = viewpager.currentItem+1
        }
        else {
            fragmentChangeListener.destroyCurrentFragment()
        }
    }

    override fun previousUserStory() {
        if (viewpager.currentItem != 0){
            viewpager.currentItem = viewpager.currentItem-1
        }
    }
}