package com.kvep.thevaaramsongs.repository

import com.kvep.thevaaramsongs.data.model.Song
import com.kvep.thevaaramsongs.data.remote.MusicDatabase
import com.kvep.thevaaramsongs.repository.mapper.Track
import com.kvep.thevaaramsongs.repository.mapper.toTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(private val musicDatabase: MusicDatabase) : TrackRepository {

  private var tracks= mutableListOf<Track>()// List<Track>
    private lateinit var allSongs: List<Song>

    suspend fun fetchMediaData() = withContext(Dispatchers.IO) {
        allSongs = musicDatabase.getAllSongs()
        val allTracks = allSongs.map {
            it.toTrack()
        }
        tracks=allTracks.toMutableList()
    }

    override fun getTrackList(): List<Track> {
        return tracks
    }
}