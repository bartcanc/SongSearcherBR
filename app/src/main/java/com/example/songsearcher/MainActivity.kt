package com.example.songsearcher

import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val apiKey = "690b1c80b901a698972174631e19a9fe"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lyricsEditText: EditText = findViewById(R.id.lyricsEditText)
        val searchButton: Button = findViewById(R.id.searchButton)
        val resultsTextView: TextView = findViewById(R.id.resultsTextView)

        searchButton.setOnClickListener {
            val lyrics = lyricsEditText.text.toString()
            searchTracks(lyrics, resultsTextView)
        }
    }

    private fun searchTracks(lyrics: String, resultsTextView: TextView) {
        val call = RetrofitClient.instance.searchTracks(lyrics, apiKey)
        call.enqueue(object : Callback<MusixmatchResponse> {
            override fun onResponse(
                call: Call<MusixmatchResponse>,
                response: Response<MusixmatchResponse>
            ) {
                if (response.isSuccessful) {
                    val trackList = response.body()?.message?.body?.trackList ?: emptyList()
                    val results = trackList.joinToString("\n") { it.track.trackName + " by " + it.track.artistName }
                    resultsTextView.text = results
                } else {
                    resultsTextView.text = "No results found."
                }
            }

            override fun onFailure(call: Call<MusixmatchResponse>, t: Throwable) {
                resultsTextView.text = "Error: " + t.message
            }
        })
    }
}
