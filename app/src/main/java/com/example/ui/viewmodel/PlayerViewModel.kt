package com.example.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem as ExoMediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import com.example.data.model.AudioTrack
import com.example.data.model.SubtitleTrack
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class ResizeMode(val mode: Int, val label: String) {
  FIT(AspectRatioFrameLayout.RESIZE_MODE_FIT, "Fit"),
  ZOOM(AspectRatioFrameLayout.RESIZE_MODE_ZOOM, "Zoom"),
  FILL(AspectRatioFrameLayout.RESIZE_MODE_FILL, "Stretch")
}

data class PlayerUiState(
  val isPlaying: Boolean = true,
  val currentPositionMs: Long = 0L,
  val durationMs: Long = 0L,
  val bufferedPositionMs: Long = 0L,
  val isBuffering: Boolean = true,
  val controlsVisible: Boolean = true,
  val isLocked: Boolean = false,
  val currentResizeMode: ResizeMode = ResizeMode.FIT,
  val volumeLevel: Float? = null, // 0.0 to 1.0, null when HUD is hidden
  val brightnessLevel: Float? = null, // 0.0 to 1.0, null when HUD is hidden
  val selectedAudioTrack: AudioTrack? = null,
  val selectedSubtitleTrack: SubtitleTrack? = null,
  val isAudioSubtitleSheetOpen: Boolean = false,
  val currentSubtitleText: String? = null
)

class PlayerViewModel : ViewModel() {

  private var exoPlayer: ExoPlayer? = null
  private val _uiState = MutableStateFlow(PlayerUiState())
  val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

  private var progressTrackerJob: Job? = null
  private var hideControlsJob: Job? = null
  private var hideHudJob: Job? = null

  fun initializePlayer(
    context: Context,
    mediaUrl: String,
    initialAudioTracks: List<AudioTrack>,
    initialSubtitleTracks: List<SubtitleTrack>
  ): ExoPlayer {
    val existing = exoPlayer
    if (existing != null) {
      return existing
    }

    val player = ExoPlayer.Builder(context.applicationContext).build().apply {
      val exoMediaItem = ExoMediaItem.fromUri(mediaUrl)
      setMediaItem(exoMediaItem)
      prepare()
      playWhenReady = true

      addListener(object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
          _uiState.value = _uiState.value.copy(isPlaying = isPlaying)
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
          val isBuffering = playbackState == Player.STATE_BUFFERING
          val duration = if (duration > 0) duration else 0L
          _uiState.value = _uiState.value.copy(
            isBuffering = isBuffering,
            durationMs = duration
          )
        }
      })
    }

    val defaultAudio = initialAudioTracks.firstOrNull { it.isDefault } ?: initialAudioTracks.firstOrNull()
    val defaultSub = initialSubtitleTracks.firstOrNull { it.id == "sub_en" } ?: initialSubtitleTracks.firstOrNull()

    _uiState.value = _uiState.value.copy(
      selectedAudioTrack = defaultAudio,
      selectedSubtitleTrack = defaultSub
    )

    exoPlayer = player
    startProgressTracker()
    scheduleControlsHide()
    return player
  }

  fun getPlayer(): ExoPlayer? = exoPlayer

  private fun startProgressTracker() {
    progressTrackerJob?.cancel()
    progressTrackerJob = viewModelScope.launch {
      while (isActive) {
        exoPlayer?.let { player ->
          val pos = player.currentPosition.coerceAtLeast(0L)
          val dur = player.duration.coerceAtLeast(0L)
          val buf = player.bufferedPosition.coerceAtLeast(0L)

          // Simulated dynamic subtitles matching the video timeline for chosen subtitle language
          val subText = generateSimulatedSubtitles(pos, _uiState.value.selectedSubtitleTrack)

          _uiState.value = _uiState.value.copy(
            currentPositionMs = pos,
            durationMs = dur,
            bufferedPositionMs = buf,
            currentSubtitleText = subText
          )
        }
        delay(300)
      }
    }
  }

  private fun generateSimulatedSubtitles(posMs: Long, track: SubtitleTrack?): String? {
    if (track == null || track.id == "sub_none" || track.languageCode.isEmpty()) {
      return null
    }
    val sec = (posMs / 1000) % 24
    return when (track.languageCode) {
      "ar" -> when (sec) {
        in 0..4 -> "الرادار يرصد إشارة غير معروفة في القطاع الرابع."
        in 5..9 -> "تأهبوا جميعاً، استعدوا لتشغيل الدفع التوربيني!"
        in 10..14 -> "المجال المغناطيسي مستقر بنسبة 98%."
        in 15..19 -> "المستقبل بين أيدينا الآن، لن نتراجع!"
        else -> null
      }
      "fr" -> when (sec) {
        in 0..4 -> "Le radar détecte un signal inconnu dans le secteur 4."
        in 5..9 -> "Tout le monde à son poste, activez les propulseurs !"
        in 10..14 -> "Le bouclier magnétique est stable à 98%."
        in 15..19 -> "L'avenir est entre nos mains, pas de retour possible !"
        else -> null
      }
      "es" -> when (sec) {
        in 0..4 -> "El radar detecta una señal desconocida en el sector 4."
        in 5..9 -> "¡Todos a sus puestos, activen los propulsores!"
        in 10..14 -> "El escudo magnético está estable al 98%."
        in 15..19 -> "El destino está en nuestras manos, ¡adelante!"
        else -> null
      }
      "de" -> when (sec) {
        in 0..4 -> "Radar erkennt ein unbekanntes Signal in Sektor 4."
        in 5..9 -> "Alle auf Gefechtsstation, Triebwerke zünden!"
        in 10..14 -> "Magnetschild ist stabil bei 98%."
        in 15..19 -> "Die Zukunft liegt in unseren Händen!"
        else -> null
      }
      "ja" -> when (sec) {
        in 0..4 -> "レーダーが第4セクターで未知の信号を感知。"
        in 5..9 -> "総員戦闘配置！メインエンジン始動！"
        in 10..14 -> "磁気シールド安定度98パーセント。"
        in 15..19 -> "我々の未来はここから始まる！"
        else -> null
      }
      else -> when (sec) {
        in 0..4 -> "[Sensors] Unknown subspace telemetry detected in Sector 4."
        in 5..9 -> "[Commander] All stations prepare for orbital boost!"
        in 10..14 -> "[AI Core] Magnetic shielding calibrated at 98% capacity."
        in 15..19 -> "[Crew] Destiny is in our hands. Engaging hyperdrive!"
        else -> null
      }
    }
  }

  fun togglePlayPause() {
    exoPlayer?.let { player ->
      if (player.isPlaying) {
        player.pause()
      } else {
        player.play()
      }
      scheduleControlsHide()
    }
  }

  fun seekTo(positionMs: Long) {
    exoPlayer?.seekTo(positionMs)
    scheduleControlsHide()
  }

  fun rewind10Seconds() {
    exoPlayer?.let { player ->
      val newPos = (player.currentPosition - 10000L).coerceAtLeast(0L)
      player.seekTo(newPos)
      scheduleControlsHide()
    }
  }

  fun forward10Seconds() {
    exoPlayer?.let { player ->
      val newPos = (player.currentPosition + 10000L).coerceAtMost(player.duration.coerceAtLeast(0L))
      player.seekTo(newPos)
      scheduleControlsHide()
    }
  }

  fun toggleControls() {
    if (_uiState.value.isLocked) return
    val newVisibility = !_uiState.value.controlsVisible
    _uiState.value = _uiState.value.copy(controlsVisible = newVisibility)
    if (newVisibility) {
      scheduleControlsHide()
    } else {
      hideControlsJob?.cancel()
    }
  }

  fun scheduleControlsHide() {
    hideControlsJob?.cancel()
    if (!_uiState.value.isLocked) {
      hideControlsJob = viewModelScope.launch {
        delay(4500)
        _uiState.value = _uiState.value.copy(controlsVisible = false)
      }
    }
  }

  fun toggleLock() {
    val newLock = !_uiState.value.isLocked
    _uiState.value = _uiState.value.copy(
      isLocked = newLock,
      controlsVisible = !newLock
    )
    if (!newLock) {
      scheduleControlsHide()
    }
  }

  fun cycleResizeMode() {
    val nextMode = when (_uiState.value.currentResizeMode) {
      ResizeMode.FIT -> ResizeMode.ZOOM
      ResizeMode.ZOOM -> ResizeMode.FILL
      ResizeMode.FILL -> ResizeMode.FIT
    }
    _uiState.value = _uiState.value.copy(currentResizeMode = nextMode)
    scheduleControlsHide()
  }

  fun openAudioSubtitleSheet() {
    _uiState.value = _uiState.value.copy(
      isAudioSubtitleSheetOpen = true,
      controlsVisible = true
    )
    hideControlsJob?.cancel()
  }

  fun closeAudioSubtitleSheet() {
    _uiState.value = _uiState.value.copy(isAudioSubtitleSheetOpen = false)
    scheduleControlsHide()
  }

  fun selectAudioTrack(track: AudioTrack) {
    _uiState.value = _uiState.value.copy(selectedAudioTrack = track)
  }

  fun selectSubtitleTrack(track: SubtitleTrack) {
    _uiState.value = _uiState.value.copy(selectedSubtitleTrack = track)
  }

  // Swipe gesture for Brightness (left half of screen)
  fun onBrightnessGesture(deltaFraction: Float, currentActivityBrightness: Float) {
    val current = if (currentActivityBrightness < 0f) 0.5f else currentActivityBrightness
    val newLevel = (current - deltaFraction).coerceIn(0.05f, 1.0f)
    _uiState.value = _uiState.value.copy(brightnessLevel = newLevel)
    scheduleHudHide()
  }

  // Swipe gesture for Volume (right half of screen)
  fun onVolumeGesture(deltaFraction: Float) {
    exoPlayer?.let { player ->
      val currentVol = player.volume
      val newVol = (currentVol - deltaFraction).coerceIn(0f, 1.0f)
      player.volume = newVol
      _uiState.value = _uiState.value.copy(volumeLevel = newVol)
      scheduleHudHide()
    }
  }

  fun onGestureEnd() {
    scheduleHudHide()
  }

  private fun scheduleHudHide() {
    hideHudJob?.cancel()
    hideHudJob = viewModelScope.launch {
      delay(1200)
      _uiState.value = _uiState.value.copy(
        volumeLevel = null,
        brightnessLevel = null
      )
    }
  }

  fun releasePlayer() {
    progressTrackerJob?.cancel()
    hideControlsJob?.cancel()
    hideHudJob?.cancel()
    exoPlayer?.release()
    exoPlayer = null
  }

  override fun onCleared() {
    super.onCleared()
    releasePlayer()
  }
}
