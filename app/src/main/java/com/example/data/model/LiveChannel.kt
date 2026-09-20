package com.example.data.model

data class ProgramSchedule(
  val id: String,
  val title: String,
  val timeSlot: String,
  val durationMinutes: Int,
  val description: String
)

enum class LiveCategory(val displayName: String) {
  ALL("All"),
  NEWS("News"),
  SPORTS("Sports"),
  CINEMA("Cinema"),
  ENTERTAINMENT("Entertainment"),
  KIDS("Kids")
}

data class LiveChannel(
  val id: String,
  val channelNumber: Int,
  val name: String,
  val category: LiveCategory,
  val logoResName: String,
  val streamUrl: String,
  val currentProgram: String,
  val currentProgramTime: String,
  val progressFraction: Float, // 0.0 to 1.0
  val minutesRemaining: Int,
  val upcomingProgram: String,
  val upcomingProgramTime: String,
  val viewersCount: String,
  val isLive: Boolean = true,
  val audioTracks: List<AudioTrack> = emptyList(),
  val subtitleTracks: List<SubtitleTrack> = emptyList()
)
