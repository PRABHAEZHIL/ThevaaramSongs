package com.kvep.thevaaramsongs.player

import com.kvep.thevaaramsongs.data.model.Song
import com.kvep.thevaaramsongs.repository.mapper.Track

/**
 * An interface for handling player events such as play, pause, next, previous, and seek bar position changes.
 */

interface PlayerEvents {
    /**
     * Invoked when the play or pause button is clicked.
     */
    fun onPlayPauseClick()

    /**
     * Invoked when the previous button is clicked.
     */
    fun onPreviousClick()

    /**
     * Invoked when the next button is clicked.
     */
    fun onNextClick()

    /**
     * Invoked when a track is clicked. The clicked [Track] is provided as a parameter.
     *
     * @param track The track that was clicked.
     */
    fun onTrackClick(tract: Track)

    /**
     * Invoked when the position of the seek bar has changed. The new position is provided as a parameter.
     *
     * @param position The new position of the seek bar.
     */
    fun onSeekBarPositionChanged(position: Long)


}