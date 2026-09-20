package com.example.ui.screens

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.ui.PlayerView
import com.example.data.localization.Translation
import com.example.data.model.AudioTrack
import com.example.data.model.SubtitleTrack
import com.example.ui.theme.AccentAmber
import com.example.ui.theme.Badge4K
import com.example.ui.theme.BrandRed
import com.example.ui.theme.StreamDarkBackground
import com.example.ui.theme.StreamDarkCard
import com.example.ui.theme.StreamDarkElevated
import com.example.ui.theme.StreamTextMuted
import com.example.ui.theme.StreamTextPrimary
import com.example.ui.theme.StreamTextSecondary
import com.example.ui.viewmodel.PlayerUiState
import com.example.ui.viewmodel.PlayerViewModel
import java.util.Locale

@Composable
fun PlayerScreen(
  title: String,
  subtitleInfo: String,
  videoUrl: String,
  audioTracks: List<AudioTrack>,
  subtitleTracks: List<SubtitleTrack>,
  strings: Translation,
  onClosePlayer: () -> Unit,
  viewModel: PlayerViewModel = viewModel()
) {
  val context = LocalContext.current
  val activity = context as? Activity
  val state by viewModel.uiState.collectAsState()

  // Manage player lifecycle and orientation
  DisposableEffect(videoUrl) {
    val player = viewModel.initializePlayer(context, videoUrl, audioTracks, subtitleTracks)
    onDispose {
      viewModel.releasePlayer()
    }
  }

  BackHandler {
    if (state.isLocked) {
      viewModel.toggleLock()
    } else {
      onClosePlayer()
    }
  }

  BoxWithConstraints(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.Black)
  ) {
    val screenWidth = maxWidth

    // Gesture Detector for volume (right half) and brightness (left half)
    Box(
      modifier = Modifier
        .fillMaxSize()
        .pointerInput(state.isLocked) {
          if (!state.isLocked) {
            detectVerticalDragGestures(
              onDragEnd = { viewModel.onGestureEnd() },
              onVerticalDrag = { change, dragAmount ->
                val x = change.position.x
                val fraction = dragAmount / 500f
                if (x < size.width / 2) {
                  // Left side = brightness
                  val curBrightness = activity?.window?.attributes?.screenBrightness ?: -1f
                  viewModel.onBrightnessGesture(fraction, curBrightness)
                  // Apply to activity window
                  activity?.let { act ->
                    val lp = act.window.attributes
                    val newB = (state.brightnessLevel ?: 0.5f).coerceIn(0.05f, 1f)
                    lp.screenBrightness = newB
                    act.window.attributes = lp
                  }
                } else {
                  // Right side = volume
                  viewModel.onVolumeGesture(fraction)
                }
              }
            )
          }
        }
        .clickable { viewModel.toggleControls() }
    ) {
      // AndroidView Hosting Media3 PlayerView
      AndroidView(
        factory = { ctx ->
          PlayerView(ctx).apply {
            layoutParams = FrameLayout.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT
            )
            useController = false
            resizeMode = state.currentResizeMode.mode
            player = viewModel.getPlayer()
          }
        },
        update = { playerView ->
          playerView.player = viewModel.getPlayer()
          playerView.resizeMode = state.currentResizeMode.mode
        },
        modifier = Modifier.fillMaxSize()
      )

      // Buffering Spinner
      if (state.isBuffering) {
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          CircularProgressIndicator(
            color = BrandRed,
            strokeWidth = 4.dp,
            modifier = Modifier.size(52.dp)
          )
        }
      }

      // Live Subtitles Display Overlay
      if (state.currentSubtitleText != null) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(bottom = if (state.controlsVisible) 90.dp else 36.dp),
          contentAlignment = Alignment.Center
        ) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color.Black.copy(alpha = 0.75f),
            modifier = Modifier.padding(horizontal = 24.dp)
          ) {
            Text(
              text = state.currentSubtitleText!!,
              color = Color.Yellow,
              fontSize = 17.sp,
              fontWeight = FontWeight.Bold,
              textAlign = TextAlign.Center,
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
          }
        }
      }

      // Gesture HUD: Brightness & Volume
      GestureIndicatorsHud(
        volumeLevel = state.volumeLevel,
        brightnessLevel = state.brightnessLevel,
        strings = strings,
        modifier = Modifier.align(Alignment.Center)
      )

      // Locked floating indicator
      if (state.isLocked) {
        Surface(
          shape = RoundedCornerShape(24.dp),
          color = Color.Black.copy(alpha = 0.8f),
          border = BorderStroke(1.dp, BrandRed),
          modifier = Modifier
            .align(Alignment.TopCenter)
            .statusBarsPadding()
            .padding(top = 16.dp)
            .clickable { viewModel.toggleLock() }
            .testTag("screen_locked_pill")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = "Unlock",
              tint = BrandRed,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = strings.tapToUnlock,
              color = Color.White,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }

      // Custom Netflix Controls Overlay
      AnimatedVisibility(
        visible = state.controlsVisible && !state.isLocked,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier.fillMaxSize()
      ) {
        NetflixControlsOverlay(
          title = title,
          subtitleInfo = subtitleInfo,
          state = state,
          strings = strings,
          onBack = onClosePlayer,
          onTogglePlayPause = { viewModel.togglePlayPause() },
          onRewind = { viewModel.rewind10Seconds() },
          onForward = { viewModel.forward10Seconds() },
          onSeek = { viewModel.seekTo(it) },
          onCycleResize = { viewModel.cycleResizeMode() },
          onOpenAudioSubtitles = { viewModel.openAudioSubtitleSheet() },
          onToggleLock = { viewModel.toggleLock() }
        )
      }
    }
  }

  // Audio & Subtitles Bottom Sheet
  if (state.isAudioSubtitleSheetOpen) {
    AudioAndSubtitlesSheet(
      audioTracks = audioTracks,
      subtitleTracks = subtitleTracks,
      selectedAudio = state.selectedAudioTrack,
      selectedSubtitle = state.selectedSubtitleTrack,
      strings = strings,
      onSelectAudio = { viewModel.selectAudioTrack(it) },
      onSelectSubtitle = { viewModel.selectSubtitleTrack(it) },
      onDismiss = { viewModel.closeAudioSubtitleSheet() }
    )
  }
}

@Composable
fun NetflixControlsOverlay(
  title: String,
  subtitleInfo: String,
  state: PlayerUiState,
  strings: Translation,
  onBack: () -> Unit,
  onTogglePlayPause: () -> Unit,
  onRewind: () -> Unit,
  onForward: () -> Unit,
  onSeek: (Long) -> Unit,
  onCycleResize: () -> Unit,
  onOpenAudioSubtitles: () -> Unit,
  onToggleLock: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color(0xCC000000),
            Color(0x44000000),
            Color(0xCC000000)
          )
        )
      )
  ) {
    // Top Bar (Back, Title, Audio/Subs, Resize, Lock)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.TopCenter)
        .statusBarsPadding()
        .padding(horizontal = 14.dp, vertical = 8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = onBack,
        modifier = Modifier.testTag("player_back_button")
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Back",
          tint = Color.White,
          modifier = Modifier.size(26.dp)
        )
      }

      Spacer(modifier = Modifier.width(6.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          color = Color.White,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = subtitleInfo,
          color = StreamTextSecondary,
          fontSize = 12.sp,
          maxLines = 1
        )
      }

      // Aspect Ratio mode button (Fit / Zoom / Stretch)
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color.Black.copy(alpha = 0.5f),
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.3f)),
        modifier = Modifier
          .clickable { onCycleResize() }
          .testTag("player_aspect_ratio_button")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.AspectRatio,
            contentDescription = "Resize",
            tint = Color.White,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = state.currentResizeMode.label,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.width(8.dp))

      // Audio & Subtitles button
      IconButton(
        onClick = onOpenAudioSubtitles,
        modifier = Modifier.testTag("player_audio_subtitles_button")
      ) {
        Icon(
          imageVector = Icons.Default.Subtitles,
          contentDescription = strings.audioAndSubtitles,
          tint = if (state.selectedSubtitleTrack?.id != "sub_none") AccentAmber else Color.White,
          modifier = Modifier.size(24.dp)
        )
      }

      // Screen Lock button
      IconButton(
        onClick = onToggleLock,
        modifier = Modifier.testTag("player_lock_button")
      ) {
        Icon(
          imageVector = Icons.Default.LockOpen,
          contentDescription = strings.lockScreen,
          tint = Color.White,
          modifier = Modifier.size(24.dp)
        )
      }
    }

    // Center Playback Buttons: Rewind 10s, Big Play/Pause, Forward 10s
    Row(
      modifier = Modifier
        .align(Alignment.Center)
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // 10s Rewind
      IconButton(
        onClick = onRewind,
        modifier = Modifier
          .size(56.dp)
          .testTag("player_rewind_button")
      ) {
        Icon(
          imageVector = Icons.Default.Replay10,
          contentDescription = "Rewind 10s",
          tint = Color.White,
          modifier = Modifier.size(42.dp)
        )
      }

      Spacer(modifier = Modifier.width(36.dp))

      // Play / Pause Circle
      Surface(
        shape = CircleShape,
        color = BrandRed,
        modifier = Modifier
          .size(68.dp)
          .clickable { onTogglePlayPause() }
          .testTag("player_play_pause_button")
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = if (state.isPlaying) "Pause" else "Play",
            tint = Color.White,
            modifier = Modifier.size(38.dp)
          )
        }
      }

      Spacer(modifier = Modifier.width(36.dp))

      // 10s Forward
      IconButton(
        onClick = onForward,
        modifier = Modifier
          .size(56.dp)
          .testTag("player_forward_button")
      ) {
        Icon(
          imageVector = Icons.Default.Forward10,
          contentDescription = "Forward 10s",
          tint = Color.White,
          modifier = Modifier.size(42.dp)
        )
      }
    }

    // Bottom Controls: Scrubber Timeline Bar & Time Labels
    Column(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
      // Slider Scrubber
      val currentPos = state.currentPositionMs.toFloat()
      val totalDuration = if (state.durationMs > 0L) state.durationMs.toFloat() else 1f

      Slider(
        value = currentPos.coerceIn(0f, totalDuration),
        onValueChange = { newPos -> onSeek(newPos.toLong()) },
        valueRange = 0f..totalDuration,
        colors = SliderDefaults.colors(
          thumbColor = BrandRed,
          activeTrackColor = BrandRed,
          inactiveTrackColor = Color.White.copy(alpha = 0.3f)
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("player_timeline_scrubber")
      )

      // Time stamps (Elapsed / Remaining)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          text = formatDuration(state.currentPositionMs),
          color = Color.White,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold
        )

        Text(
          text = formatDuration(state.durationMs),
          color = StreamTextSecondary,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold
        )
      }
    }
  }
}

@Composable
fun GestureIndicatorsHud(
  volumeLevel: Float?,
  brightnessLevel: Float?,
  strings: Translation,
  modifier: Modifier = Modifier
) {
  if (volumeLevel != null) {
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = Color.Black.copy(alpha = 0.85f),
      border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
      modifier = modifier.size(width = 150.dp, height = 90.dp)
    ) {
      Column(
        modifier = Modifier.padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Icon(
          imageVector = Icons.Default.VolumeUp,
          contentDescription = strings.volume,
          tint = Color.White,
          modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
          progress = { volumeLevel },
          color = BrandRed,
          trackColor = Color.White.copy(alpha = 0.2f),
          modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "${(volumeLevel * 100).toInt()}%",
          color = Color.White,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  } else if (brightnessLevel != null) {
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = Color.Black.copy(alpha = 0.85f),
      border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
      modifier = modifier.size(width = 150.dp, height = 90.dp)
    ) {
      Column(
        modifier = Modifier.padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Icon(
          imageVector = Icons.Default.BrightnessHigh,
          contentDescription = strings.brightness,
          tint = AccentAmber,
          modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
          progress = { brightnessLevel },
          color = AccentAmber,
          trackColor = Color.White.copy(alpha = 0.2f),
          modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "${(brightnessLevel * 100).toInt()}%",
          color = Color.White,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioAndSubtitlesSheet(
  audioTracks: List<AudioTrack>,
  subtitleTracks: List<SubtitleTrack>,
  selectedAudio: AudioTrack?,
  selectedSubtitle: SubtitleTrack?,
  strings: Translation,
  onSelectAudio: (AudioTrack) -> Unit,
  onSelectSubtitle: (SubtitleTrack) -> Unit,
  onDismiss: () -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = StreamDarkCard,
    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
      Text(
        text = strings.audioAndSubtitles,
        color = StreamTextPrimary,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
      )

      Spacer(modifier = Modifier.height(18.dp))

      Row(modifier = Modifier.fillMaxWidth()) {
        // Audio Tracks Column
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = strings.audio,
            color = StreamTextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(10.dp))

          audioTracks.forEach { track ->
            val isSelected = selectedAudio?.id == track.id
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectAudio(track) }
                .padding(vertical = 10.dp)
                .testTag("audio_track_${track.id}"),
              verticalAlignment = Alignment.CenterVertically
            ) {
              if (isSelected) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = "Selected",
                  tint = BrandRed,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
              }
              Text(
                text = track.label,
                color = if (isSelected) Color.White else StreamTextSecondary,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            }
          }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Subtitles Tracks Column
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = strings.subtitles,
            color = StreamTextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(10.dp))

          subtitleTracks.forEach { track ->
            val isSelected = selectedSubtitle?.id == track.id
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelectSubtitle(track) }
                .padding(vertical = 10.dp)
                .testTag("sub_track_${track.id}"),
              verticalAlignment = Alignment.CenterVertically
            ) {
              if (isSelected) {
                Icon(
                  imageVector = Icons.Default.Check,
                  contentDescription = "Selected",
                  tint = BrandRed,
                  modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
              }
              Text(
                text = track.label,
                color = if (isSelected) Color.White else StreamTextSecondary,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

fun formatDuration(ms: Long): String {
  val totalSeconds = (ms / 1000).coerceAtLeast(0L)
  val hours = totalSeconds / 3600
  val minutes = (totalSeconds % 3600) / 60
  val seconds = totalSeconds % 60

  return if (hours > 0) {
    String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds)
  } else {
    String.format(Locale.US, "%02d:%02d", minutes, seconds)
  }
}
