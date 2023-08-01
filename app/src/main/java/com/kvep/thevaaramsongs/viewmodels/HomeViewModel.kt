package com.kvep.thevaaramsongs.viewmodels

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kvep.thevaaramsongs.data.model.Song
import com.kvep.thevaaramsongs.player.MyPlayer
import com.kvep.thevaaramsongs.player.PlaybackState
import com.kvep.thevaaramsongs.player.PlayerEvents
import com.kvep.thevaaramsongs.player.PlayerStates
import com.kvep.thevaaramsongs.repository.TrackRepository
import com.kvep.thevaaramsongs.repository.mapper.Track
import com.kvep.thevaaramsongs.utils.collectPlayerState
import com.kvep.thevaaramsongs.utils.launchPlaybackStateJob
import com.kvep.thevaaramsongs.utils.resetTracks
import com.kvep.thevaaramsongs.utils.toMediaItemList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    trackRepository: TrackRepository,private val myPlayer: MyPlayer
):ViewModel(), PlayerEvents {



    private val _tracks= mutableStateListOf<Track>()
    val tracks:List<Track> get()=_tracks

    private var isTrackPlay:Boolean=false

    var selectedTrack:Track? by mutableStateOf(null)
        private set

    private var selectedTrackIndex:Int by mutableStateOf(-1)

    private var playbackStateJob: Job?=null

    private val _playbackState= MutableStateFlow(PlaybackState(0L,0L))

    val playbackState:StateFlow<PlaybackState> get()=_playbackState

    private var isAuto:Boolean=false
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)


    init{

        serviceScope.launch {
         _tracks.addAll(trackRepository.fetchMediaData())
        }
        myPlayer.iniPlayer(tracks.toMediaItemList())
        observePlayerState()

    }
    private fun observePlayerState(){
        viewModelScope.collectPlayerState(myPlayer,::updateState)
    }
    private fun updateState(state: PlayerStates){
        if(selectedTrackIndex!=-1){
           isTrackPlay=state== PlayerStates.STATE_PLAYING
            _tracks[selectedTrackIndex].state=state
            _tracks[selectedTrackIndex].isSelected=true
            selectedTrack=null
            selectedTrack=tracks[selectedTrackIndex]
            updatePlaybackState(state)
            if(state==PlayerStates.STATE_NEXT_TRACK){
                isAuto=true
                onNextClick()
            }
            if(state==PlayerStates.STATE_END ) onTrackSelected(0)

        }

    }
    private fun onTrackSelected(index:Int){
        if (selectedTrackIndex==-1) isTrackPlay=true
        if(selectedTrackIndex==-1 ||selectedTrackIndex!=index){
            _tracks.resetTracks()
            selectedTrackIndex=index
            setUpTrack()
        }

    }
    private fun setUpTrack(){
        if(!isAuto) myPlayer.setUpTrack(selectedTrackIndex,isTrackPlay)
        isAuto=false
    }
    private fun updatePlaybackState(state: PlayerStates){
        playbackStateJob?.cancel()
        playbackStateJob=viewModelScope.launchPlaybackStateJob(_playbackState,state,myPlayer)
    }
    override fun onPlayPauseClick() {
        myPlayer.playPause()
    }

    override fun onPreviousClick() {
        if(selectedTrackIndex>0) onTrackSelected(selectedTrackIndex-1)
    }

    override fun onNextClick() {
     if(selectedTrackIndex<tracks.size-1) onTrackSelected(selectedTrackIndex+1)
    }

    override fun onTrackClick(track: Track) {
       onTrackSelected(tracks.indexOf(track))
    }

    override fun onSeekBarPositionChanged(position: Long) {
       viewModelScope.launch { myPlayer.seekToPosition(position) }
    }

    override fun onCleared() {
        super.onCleared()
        myPlayer.releasePlayer()
    }
}