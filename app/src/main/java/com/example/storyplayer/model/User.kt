package com.example.storyplayer.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("profile_picture")
    val profilePicture: String,

    @SerializedName("stories")
    val stories: ArrayList<Story>,

) : Serializable