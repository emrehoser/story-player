package com.example.storyplayer.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StoryResponse(
        @SerializedName("users")
        val users: ArrayList<User>
) : Serializable

