package com.kvep.thevaaramsongs.repository

import com.kvep.thevaaramsongs.repository.mapper.Track

interface TrackRepository {
    fun getTrackList(): List<Track>
}