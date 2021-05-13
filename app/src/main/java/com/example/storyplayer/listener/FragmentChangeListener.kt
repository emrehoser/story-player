package com.example.storyplayer.listener

import android.os.Bundle
import androidx.fragment.app.Fragment

interface FragmentChangeListener {
    fun addFragment(fragment: Fragment, bundle: Bundle? = null)
    fun destroyCurrentFragment()
}