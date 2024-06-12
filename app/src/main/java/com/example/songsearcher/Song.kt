package com.example.songsearcher

data class Song(
    val trackId: Int,
    val trackName: String,
    var albumCoverUrl: String,
    val musicBrainzId: String,
    val artistName: String
)