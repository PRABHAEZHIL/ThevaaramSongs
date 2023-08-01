package com.kvep.thevaaramsongs.repository.mapper

import com.kvep.thevaaramsongs.player.PlayerStates

data class Track(
    val trackId: Int = 0,
    val trackName: String = "",
    val trackUrl: String = "",
    val trackImage: String = "",
    var isSelected: Boolean = false,
    var state: PlayerStates = PlayerStates.STATE_IDLE
)
