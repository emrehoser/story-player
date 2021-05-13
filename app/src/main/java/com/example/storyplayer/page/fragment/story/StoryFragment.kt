package com.example.storyplayer.page.fragment.story

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.marginBottom
import androidx.core.view.marginRight
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.storyplayer.R
import com.example.storyplayer.adapter.StoryAdapter
import com.example.storyplayer.adapter.StoryViewPagerAdapter
import com.example.storyplayer.listener.FragmentChangeListener
import com.example.storyplayer.model.User
import de.hdodenhof.circleimageview.CircleImageView

class StoryFragment(
    private var user: User,
    private val itemClick: StoryViewPagerAdapter.StoryDetailClickListener
): Fragment() {

    private lateinit var fragmentChangeListener: FragmentChangeListener
    private lateinit var storyImage: ImageView
    private lateinit var storyVideo: VideoView
    private lateinit var progressBarLayout: LinearLayout
    private lateinit var profilePhoto: CircleImageView
    private lateinit var username: TextView
    private lateinit var storyTime: TextView
    private lateinit var cancelButton: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initProgressBar()
        setListener()
        setFragmentChangeListener(activity as FragmentChangeListener)
        loadData()

    }

    private fun initViews(root: View) {
        storyImage = root.findViewById(R.id.story_image)
        storyVideo = root.findViewById(R.id.story_video)
        progressBarLayout = root.findViewById(R.id.progress_bar_layout)
        profilePhoto = root.findViewById(R.id.story_profile_photo)
        username = root.findViewById(R.id.story_username)
        storyTime = root.findViewById(R.id.story_time)
        cancelButton = root.findViewById(R.id.cancel_button)
    }

    private fun initProgressBar(){

        val progressBar = ProgressBar(requireContext(),null,R.attr.progressBarStyle)

        val params : LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        progressBar.progress = 50
        progressBar.layoutParams = params
        progressBarLayout.addView(progressBar)

    }

    private fun setListener() {
        cancelButton.setOnClickListener{
            itemClick.cancelListener()
        }
    }

    private fun loadData(){
        if(user.stories[0].contentType==0){
            storyImage.visibility = View.VISIBLE
            Glide.with(requireContext()).load(user.stories[0].url).into(storyImage)
        }
        //Glide.with(requireContext()).load(user.stories[0].url).into(storyImage)
        Glide.with(requireContext()).load(user.profilePicture).into(profilePhoto)
        username.text = user.username
        storyTime.text = user.stories[0].story_time
    }

    private fun setFragmentChangeListener(fragmentChangeListener: FragmentChangeListener) {
        this.fragmentChangeListener = fragmentChangeListener
    }
}