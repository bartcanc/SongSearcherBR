package com.example.songsearcher

import com.google.gson.annotations.SerializedName

data class MusixmatchResponse(
    @SerializedName("message") val message: Message
)

data class Message(
    @SerializedName("body") val body: Body
)

data class Body(
    @SerializedName("track_list") val trackList: List<TrackContainer>
)

data class TrackContainer(
    @SerializedName("track") val track: Track
)

data class Track(
    @SerializedName("track_name") val trackName: String,
    @SerializedName("artist_name") val artistName: String
)