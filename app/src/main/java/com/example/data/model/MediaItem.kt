package com.example.data.model

data class AudioTrack(
  val id: String,
  val label: String,
  val languageCode: String,
  val isDefault: Boolean = false,
  val isDolby: Boolean = true
)

data class SubtitleTrack(
  val id: String,
  val label: String,
  val languageCode: String
)

data class Episode(
  val id: String,
  val episodeNumber: Int,
  val title: String,
  val duration: String,
  val description: String,
  val streamUrl: String
)

enum class MediaType {
  MOVIE,
  SERIES,
  LIVE
}

data class MediaItem(
  val id: String,
  val title: String,
  val description: String,
  val bannerResName: String,
  val fallbackColorHex: Long = 0xFF1C1C28,
  val videoUrl: String,
  val type: MediaType = MediaType.MOVIE,
  val matchPercentage: Int = 98,
  val rating: String = "8.9",
  val year: Int = 2024,
  val durationOrSeasons: String = "2h 14m",
  val qualityBadge: String = "4K Ultra HD",
  val ageRating: String = "16+",
  val genres: List<String> = emptyList(),
  val cast: List<String> = emptyList(),
  val directors: List<String> = emptyList(),
  val audioTracks: List<AudioTrack> = emptyList(),
  val subtitleTracks: List<SubtitleTrack> = emptyList(),
  val episodes: List<Episode> = emptyList(),
  val isTop10: Boolean = false,
  val rankNumber: Int? = null,
  val watchedProgressFraction: Float = 0f
)
