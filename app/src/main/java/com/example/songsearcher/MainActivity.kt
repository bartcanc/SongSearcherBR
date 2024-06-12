package com.example.songsearcher
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var songAdapter: SongAdapter
    private val client = OkHttpClient()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val searchButton: Button = findViewById(R.id.searchButton)
        val lyricsEditText: EditText = findViewById(R.id.lyricsEditText)

        recyclerView.layoutManager = LinearLayoutManager(this)
        songAdapter = SongAdapter(emptyList())
        recyclerView.adapter = songAdapter

        searchButton.setOnClickListener {
            val lyrics = lyricsEditText.text.toString()
            searchSongs(lyrics)
        }
    }

    private fun searchSongs(lyrics: String) {
        val url = "https://api.musixmatch.com/ws/1.1/track.search?q_lyrics=$lyrics&apikey=${BuildConfig.MUSIXMATCH_API_KEY}"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    Log.d("API_RESPONSE", responseBody)
                    try {
                        val jsonObject = JSONObject(responseBody)
                        val trackList = jsonObject.getJSONObject("message").getJSONObject("body").getJSONArray("track_list")
                        val songs = mutableListOf<Song>()
                        for (i in 0 until trackList.length()) {
                            val trackObject = trackList.getJSONObject(i).getJSONObject("track")
                            val trackId = trackObject.getInt("track_id")
                            val trackName = trackObject.getString("track_name")
                            val musicBrainzId = trackObject.optString("musicbrainz_album_id", "")
                            val artistName = trackObject.getString("artist_name")

                            val song = Song(trackId, trackName, "", musicBrainzId, artistName)
                            songs.add(song)
                        }

                        runOnUiThread {
                            songAdapter.updateSongs(songs)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    Log.e("API_ERROR", "Empty response body")
                }
            }
        })
    }
}
