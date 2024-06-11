package com.example.songsearcher
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MusixmatchApiService {
    @GET("track.search")
    fun searchTracks(
        @Query("q_lyrics") lyrics: String,
        @Query("apikey") apiKey: String
    ): Call<MusixmatchResponse>
}