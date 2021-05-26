package com.example.storyplayer.page.fragment.story

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.storyplayer.R
import com.example.storyplayer.adapter.StoryViewPagerAdapter
import com.example.storyplayer.listener.CustomTouchListener
import com.example.storyplayer.listener.FragmentChangeListener
import com.example.storyplayer.model.User
import de.hdodenhof.circleimageview.CircleImageView


class StoryFragment(
    private var user: User,
    private val itemClick: StoryViewPagerAdapter.StoryDetailClickListener
): Fragment() {

    private lateinit var fragmentChangeListener: FragmentChangeListener
    private lateinit var storyFullLayout: ConstraintLayout
    private lateinit var storyInfoLayout: ConstraintLayout
    private lateinit var storyImage: ImageView
    private lateinit var storyVideo: VideoView
    private lateinit var progressBarLayout: LinearLayout
    private lateinit var profilePhoto: CircleImageView
    private lateinit var username: TextView
    private lateinit var storyTime: TextView
    private lateinit var cancelButton: ImageView
    private lateinit var next: View
    private  lateinit var previous: View


    private var timer:CountDownTimer? = null
    private var progressBarList = arrayListOf<ProgressBar>()
    private var currentStory = 0
    private var pressTime = 0L
    private var limit = 500L
    private var progress = 0
    private var duration = 0L
    private var remainingTime = 0L
    private var onHold = false
    private var nextClicked = 0L
    private var previousClicked = 0L




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
    }

    override fun onPause() {
        super.onPause()
        resetData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
        setContent()
        storyInfoLayout.visibility = View.VISIBLE
        storyVideo.start()
    }

    private fun setContent(){
        if(user.stories[currentStory].contentType == 0){
            Glide.with(requireContext()).load(user.stories[currentStory].url)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        duration = 5000L
                        if(onHold){
                            setTimer(duration,remainingTime)
                        }else{
                            setTimer(duration,duration)
                        }
                        return false
                    }
                }).into(storyImage)
        } else{
            storyVideo.setVideoURI(Uri.parse(user.stories[currentStory].url))
            storyVideo.setOnPreparedListener {
                if(onHold){
                    setTimer(it.duration.toLong(),remainingTime)
                }else{
                    setTimer(it.duration.toLong(),it.duration.toLong())
                }
            }
        }

    }

    private fun resetData() {
        timer?.cancel()
        storyVideo.stopPlayback()
        progress = 0
        storyVideo.visibility = View.GONE
        storyImage.visibility = View.GONE

    }


    private fun initViews(root: View) {
        storyFullLayout = root.findViewById(R.id.story_full_layout)
        storyInfoLayout = root.findViewById(R.id.story_info_Layout)
        storyImage = root.findViewById(R.id.story_image)
        storyVideo = root.findViewById(R.id.story_video)
        progressBarLayout = root.findViewById(R.id.progress_bar_layout)
        profilePhoto = root.findViewById(R.id.story_profile_photo)
        username = root.findViewById(R.id.story_username)
        storyTime = root.findViewById(R.id.story_time)
        cancelButton = root.findViewById(R.id.cancel_button)
        next = root.findViewById(R.id.next)
        previous = root.findViewById(R.id.previous)

    }

    private fun initProgressBar(){

        val params : LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        params.weight = 1F
        params.setMargins(3,0,3,0)

        for(i in 0 until user.stories.size){
            val progressBar = ProgressBar(requireContext(),
                null,
                R.attr.progressBarStyle
            )
            progressBar.progressDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.progress_bar)
            progressBar.isIndeterminate = false
            progressBar.max = 100
            progressBar.layoutParams = params
            progressBarList.add(progressBar)
            progressBarLayout.addView(progressBar)
        }

    }

    private fun setTimer(maxDuration: Long,remaining: Long) {

        progressBarList[currentStory].progress = progress

        timer?.cancel()

        timer = object: CountDownTimer(remaining, 50) {
            override fun onTick(millisUntilFinished: Long){
                Log.i("tag", progress.toString())
                progressBarList[currentStory].progress = (progress * 100/(maxDuration/50)).toInt()
                progress++
                //progressBarList[currentStory].progress = ((1-(millisUntilFinished.toFloat()/maxDuration)) * 100).toInt()
                remainingTime = millisUntilFinished
            }

            override fun onFinish(){
                progressBarList[currentStory].progress = 100
                //Log.i("Tag","finish")

                if(currentStory >= user.stories.size - 1){
                    storyVideo.stopPlayback()
                    //Log.i("tag", "emre")
                    itemClick.nextUserStory()
                }else {
                    storyVideo.stopPlayback()
                    currentStory++
                    loadData()
                    nextStory()
                }

            }
        }
        (timer as CountDownTimer).start()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setListener() {
        val touchListener = object : CustomTouchListener(activity!!) {

            override fun onClick(view: View) {
                when (view) {
                    next -> {
                        if(currentStory >= user.stories.size - 1){
                            nextClicked = System.currentTimeMillis()
                            if((System.currentTimeMillis()-previousClicked)<=500){
                                Log.i("clicked", "hızlı tıklama")
                            }else{
                                progressBarList[currentStory].progress = 0

                                timer?.cancel()

                                storyVideo.stopPlayback()
                                //Log.i("tag", "emre tıklama")
                                itemClick.nextUserStory()
                            }
                        }else {
                            timer?.cancel()
                            storyVideo.stopPlayback()
                            progressBarList[currentStory].progress = 100
                            currentStory++
                            loadData()
                            nextStory()
                        }

                    }
                    previous -> {
                        if(currentStory <= 0){
                            previousClicked = System.currentTimeMillis()
                            if((System.currentTimeMillis()-nextClicked) <= 500) {
                                Log.i("clicked", "hızlı tıklama")
                            }else{
                                itemClick.previousUserStory()
                            }
                        }else {
                            timer?.cancel()
                            progressBarList[currentStory].progress = 0
                            currentStory--
                            loadData()
                            previousStory()
                        }

                    }
                }
            }

             override fun onLongClick() {
                 this@StoryFragment.onHold = true
                 storyInfoLayout.parent.requestDisallowInterceptTouchEvent(true)
                 hideStoryInfo()
                 pauseCurrentStory()
            }

            override fun onTouchView(view: View, event: MotionEvent): Boolean {
                super.onTouchView(view, event)
                //Log.i("action", event.action.toString())
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        pressTime = System.currentTimeMillis()
                        return false
                    }
                    MotionEvent.ACTION_UP -> {
                        view.parent.requestDisallowInterceptTouchEvent(false)
                        if(onHold) {
                            showStoryInfo()
                            resumeCurrentStory()
                            onHold = false
                            return limit < System.currentTimeMillis() - pressTime
                        }
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        view.parent.requestDisallowInterceptTouchEvent(false)
                    }

                }
                return false
            }
        }
        previous.setOnTouchListener(touchListener)
        next.setOnTouchListener(touchListener)

        cancelButton.setOnClickListener{
            itemClick.cancelListener()
        }

    }
    private  fun nextStory(){
        progress = 0
        setContent()
    }
    private  fun previousStory(){
        progress = 0
        setContent()
    }

    fun pauseCurrentStory() {
        storyVideo.pause()
        timer?.cancel()
    }

    fun resumeCurrentStory() {
        storyVideo.start()
        //Log.i("progress", progressBarList[currentStory].progress.toString())
        //Log.i("remainingtime", remainingTime.toString())
        setContent()
    }

    private fun showStoryInfo() {
        if (storyInfoLayout.alpha != 0F) return

        storyInfoLayout.animate()
            .setDuration(100)
            .alpha(1F)
            .start()
    }

    private fun hideStoryInfo() {
        if (storyInfoLayout.alpha != 1F) return

        storyInfoLayout.animate()
            .setDuration(200)
            .alpha(0F)
            .start()
    }


    private fun loadData() {
        progressBarList[currentStory].progress = 0
        Glide.with(requireContext()).load(user.profilePicture).into(profilePhoto)
        username.text = user.username
        storyTime.text = user.stories[currentStory].story_time
        if(user.stories[currentStory].contentType == 0){
            storyVideo.stopPlayback()
            storyVideo.visibility = View.GONE
            storyImage.visibility = View.VISIBLE
        }
        else{
            storyImage.visibility = View.GONE
            storyVideo.visibility = View.VISIBLE
        }
    }

    private fun setFragmentChangeListener(fragmentChangeListener: FragmentChangeListener) {
        this.fragmentChangeListener = fragmentChangeListener
    }
}