package com.kvep.thevaaramsongs.repository

import com.kvep.thevaaramsongs.repository.mapper.Track

interface TrackRepository {

   suspend fun fetchMediaData(): MutableList<Track>
}