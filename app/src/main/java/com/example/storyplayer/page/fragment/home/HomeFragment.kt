package com.example.storyplayer.page.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.storyplayer.R
import com.example.storyplayer.viewmodel.StoryViewModel
import com.example.storyplayer.adapter.StoryRecyclerAdapter
import com.example.storyplayer.listener.FragmentChangeListener
import com.example.storyplayer.model.StoryResponse
import com.example.storyplayer.page.fragment.story.StoryContainerFragment

class HomeFragment : Fragment(), StoryRecyclerAdapter.StoryClickListener{

    private lateinit var storyRecyclerView: RecyclerView
    private lateinit var fragmentChangeListener: FragmentChangeListener
    private lateinit var storyResponse: StoryResponse

    private lateinit var viewModel: StoryViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(StoryViewModel::class.java)

        initViews(view)
        setFragmentChangeListener(activity as FragmentChangeListener)
        viewModel.readStories(requireContext().assets.open("data.json"))
        viewModel.storyRead.observe(viewLifecycleOwner,{
            storiesRead(it)
        } )

    }

    private fun initViews(root: View) {
        storyRecyclerView = root.findViewById(R.id.home_page_recycler_view)
        storyRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        storyRecyclerView.adapter = StoryRecyclerAdapter(arrayListOf(), requireContext(), this)
    }

    fun storiesRead(storyResponse: StoryResponse) {
        this.storyResponse = storyResponse
        if (activity != null && requireActivity().isFinishing.not()){
            (storyRecyclerView.adapter as StoryRecyclerAdapter).users.addAll(storyResponse.users)
            (storyRecyclerView.adapter as StoryRecyclerAdapter).notifyDataSetChanged()
        }
    }


    private fun setFragmentChangeListener(fragmentChangeListener: FragmentChangeListener) {
        this.fragmentChangeListener = fragmentChangeListener
    }

    override fun onStoryClicked(index: Int) {
        val bundle = Bundle()
        bundle.putInt("current_user", index)
        bundle.putSerializable("users", storyResponse)
        fragmentChangeListener.addFragment(StoryContainerFragment(),bundle)
    }
}