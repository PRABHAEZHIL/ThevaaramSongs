package com.kvep.thevaaramsongs.data.model

import com.kvep.thevaaramsongs.player.PlayerStates

data class Song(
    val mediaId: String = "",
    val title: String = "",
    val songUrl: String = "",
    val imageUrl: String = "",
    var isSelected: Boolean = false,
    var state: PlayerStates = PlayerStates.STATE_IDLE
)