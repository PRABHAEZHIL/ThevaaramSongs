package com.kvep.thevaaramsongs.ui.composable

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kvep.thevaaramsongs.R
import com.kvep.thevaaramsongs.player.PlaybackState
import com.kvep.thevaaramsongs.player.PlayerEvents
import com.kvep.thevaaramsongs.repository.mapper.Track
import com.kvep.thevaaramsongs.ui.theme.md_theme_light_primary
import com.kvep.thevaaramsongs.ui.theme.typography
import com.kvep.thevaaramsongs.viewmodels.HomeViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreenParent(viewModel: HomeViewModel) {
    val fullScreenState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()
    val onBottomTabClick: () -> Unit = { scope.launch { fullScreenState.show() } }
    TrackList(
        tracks = viewModel.tracks,
        selectedTrack = viewModel.selectedTrack,
        fullScreenState = fullScreenState,
        playerEvents = viewModel,
        playbackState = viewModel.playbackState,
        onBottomTabClick = onBottomTabClick
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TrackList(
    tracks: List<Track>,
    selectedTrack: Track?,
    fullScreenState: ModalBottomSheetState,
    playerEvents: PlayerEvents,
    playbackState: StateFlow<PlaybackState>,
    onBottomTabClick: () -> Unit
) {
    ModalBottomSheetLayout(
        sheetContent = {
            if (selectedTrack != null) BottomSheetDialog(
                selectedTrack, playerEvents, playbackState
            )
        },
        sheetState = fullScreenState,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        sheetElevation = 8.dp

    ) {
        Scaffold(topBar = {
            Surface(shadowElevation = 5.dp) {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = typography.titleLarge
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = md_theme_light_primary)
                )
            }
        }) { paddingValues ->
            Box(modifier = Modifier.padding(top = paddingValues.calculateTopPadding())) {
                Column {
                    LazyColumn(
                        modifier = Modifier.weight(weight = 1f),
                        contentPadding = PaddingValues(5.dp)
                    ) {
                        items(tracks) {
                            TrackListItem(
                                track = it,
                                onTrackClick = {

                                    playerEvents.onTrackClick(it) }
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = selectedTrack != null,
                        enter = slideInVertically(initialOffsetY = { fullHeight -> fullHeight })
                    ) {
                        BottomPlayerTab(selectedTrack!!, playerEvents, onBottomTabClick)
                    }
                }
            }
        }
    }
}
