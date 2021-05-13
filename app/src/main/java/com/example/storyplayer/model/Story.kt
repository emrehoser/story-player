package com.example.storyplayer.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Story(

        @SerializedName("story_id")
        val storyId: String,

        @SerializedName("story_time")
        val story_time: String,

        @SerializedName("url")
        val url: String,

        @SerializedName("content_type")
        val contentType: Int,

) : Serializable