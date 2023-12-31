package com.kvep.thevaaramsongs.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kvep.thevaaramsongs.player.PlaybackState
import com.kvep.thevaaramsongs.player.PlayerEvents
import com.kvep.thevaaramsongs.repository.mapper.Track
import com.kvep.thevaaramsongs.ui.theme.md_theme_dark_surfaceVariant
import com.kvep.thevaaramsongs.ui.theme.typography
import com.kvep.thevaaramsongs.utils.formatTime
import kotlinx.coroutines.flow.StateFlow


@Composable
fun BottomSheetDialog(
    selectedTrack: Track,
    playerEvents: PlayerEvents,
    playbackState: StateFlow<PlaybackState>
    ){
    Column(
        modifier= Modifier.fillMaxWidth()
    ){
       TrackInfo(trackImage = selectedTrack.trackImage, trackName = selectedTrack.trackName)
        TrackProgressSlider(playbackState = playbackState) {
            playerEvents.onSeekBarPositionChanged(it)
        }
        TrackControls(
            selectedTrack = selectedTrack,
            onPreviousClick = playerEvents::onPreviousClick,
            onPlayPauseClick = playerEvents::onPlayPauseClick,
            onNextClick = playerEvents::onNextClick
        )
    }

}
@Composable
fun TrackInfo(trackImage:String,trackName:String){
    Box(
        modifier= Modifier
            .fillMaxWidth()
            .height(height = 350.dp)
            .background(md_theme_dark_surfaceVariant)
    ){
        TrackImage(
            trackImage=trackImage,
            modifier= Modifier
                .fillMaxSize()
                .padding(all = 16.dp)
        )
    }
    Text(
        text=trackName,
        style = typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    )
}
@Composable
fun TrackProgressSlider(
    playbackState: StateFlow<PlaybackState>,
    onSeekBarPositionChanged:(Long)->Unit
){
    val playbackStateValue=playbackState.collectAsState(
        initial=PlaybackState(0L,0L)
    ).value
    var currentMediaProgress=playbackStateValue.currentPlaybackPosition.toFloat()
    var currentPosTemp by rememberSaveable { mutableStateOf(0f) }
    Slider(
        value=if(currentPosTemp==0f)  currentMediaProgress else currentPosTemp,
        onValueChange = {currentPosTemp=it},
        onValueChangeFinished = {
            currentMediaProgress=currentPosTemp
            currentPosTemp=0f
            onSeekBarPositionChanged(currentMediaProgress.toLong())
        },
        valueRange = 0f..playbackStateValue.currentTrackDuration.toFloat(),
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal=16.dp)
        )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = playbackStateValue.currentPlaybackPosition.formatTime(),
            style = typography.bodySmall
        )
        Text(
            text = playbackStateValue.currentTrackDuration.formatTime(),
            style = typography.bodySmall
        )
    }
}
@Composable
fun TrackControls(
    selectedTrack: Track,
    onPreviousClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PreviousIcon(onClick = onPreviousClick, isBottomTab = false)
        PlayPauseIcon(
            selectedTrack = selectedTrack,
            onClick = onPlayPauseClick,
            isBottomTab = false
        )
        NextIcon(onClick = onNextClick, isBottomTab = false)
    }
}

