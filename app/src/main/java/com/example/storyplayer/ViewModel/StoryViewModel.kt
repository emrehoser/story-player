package com.example.storyplayer.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.storyplayer.model.StoryResponse
import com.google.gson.Gson
import org.json.JSONObject
import java.io.InputStream

class StoryViewModel(application: Application): AndroidViewModel(application) {

    private lateinit var inputStream: InputStream
    val storyRead = MutableLiveData<StoryResponse>()

    fun readStories(inputStream: InputStream) {
        this.inputStream = inputStream
        val storiesJSON = readStoriesFromJson()
        val storyResponse = createStoryResponse(storiesJSON)
        storyRead.postValue(storyResponse)
    }

    fun readStoriesFromJson() : JSONObject {
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        return JSONObject(String(buffer))
    }


    private fun createStoryResponse(storyJSON: JSONObject) : StoryResponse {
        return Gson().fromJson(storyJSON.toString(), StoryResponse::class.java)
    }
}