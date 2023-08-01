package com.kvep.thevaaramsongs.repository.mapper

import com.kvep.thevaaramsongs.data.model.Song
import com.kvep.thevaaramsongs.player.PlayerStates

fun Song.toTrack(): Track {
    return Track(
        trackId = mediaId,
        trackName = title,
        trackUrl = songUrl,
        trackImage = imageUrl,
        isSelected = false,
        state = PlayerStates.STATE_IDLE
    )
}