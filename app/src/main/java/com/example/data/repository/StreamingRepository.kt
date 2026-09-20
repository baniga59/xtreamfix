package com.example.data.repository

import com.example.data.model.AudioTrack
import com.example.data.model.Episode
import com.example.data.model.LiveCategory
import com.example.data.model.LiveChannel
import com.example.data.model.MediaItem
import com.example.data.model.MediaType
import com.example.data.model.SubtitleTrack

class StreamingRepository {

  val defaultAudioTracks = listOf(
    AudioTrack("audio_en", "English [Original] (Dolby Atmos 5.1)", "en", isDefault = true, isDolby = true),
    AudioTrack("audio_ar", "العربية [مدبلج] (Dolby Digital)", "ar", isDefault = false, isDolby = true),
    AudioTrack("audio_fr", "Français [Doublé] (Stéréo)", "fr", isDefault = false, isDolby = false),
    AudioTrack("audio_es", "Español [Doblado] (Dolby Digital)", "es", isDefault = false, isDolby = true),
    AudioTrack("audio_de", "Deutsch [Synchronisiert]", "de", isDefault = false, isDolby = false),
    AudioTrack("audio_ja", "日本語 [Original / Dub]", "ja", isDefault = false, isDolby = true)
  )

  val defaultSubtitleTracks = listOf(
    SubtitleTrack("sub_none", "Off", ""),
    SubtitleTrack("sub_en", "English [CC]", "en"),
    SubtitleTrack("sub_ar", "العربية (Arabic)", "ar"),
    SubtitleTrack("sub_fr", "Français", "fr"),
    SubtitleTrack("sub_es", "Español", "es"),
    SubtitleTrack("sub_de", "Deutsch", "de"),
    SubtitleTrack("sub_ja", "日本語", "ja")
  )

  fun getFeaturedItem(): MediaItem {
    return MediaItem(
      id = "featured_interstellar",
      title = "Nebula Protocol: Chrono War",
      description = "When a temporal rift threatens the outer colonies of Kepler-186f, an exiled commander leads a rogue squadron into unmapped hyperspace to salvage humanity's last hope.",
      bannerResName = "hero_movie_banner",
      fallbackColorHex = 0xFF0B172E,
      videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
      type = MediaType.MOVIE,
      matchPercentage = 99,
      rating = "9.2",
      year = 2025,
      durationOrSeasons = "2h 38m",
      qualityBadge = "4K Ultra HD",
      ageRating = "16+",
      genres = listOf("Sci-Fi", "Space Thriller", "Cyberpunk", "Action"),
      cast = listOf("Sarah Jenkins", "Marcus Vance", "Elena Rostova", "David K. Cho"),
      directors = listOf("Christopher Nolan", "Denis Villeneuve"),
      audioTracks = defaultAudioTracks,
      subtitleTracks = defaultSubtitleTracks,
      isTop10 = true,
      rankNumber = 1
    )
  }

  fun getTrendingTop10(): List<MediaItem> {
    return listOf(
      getFeaturedItem(),
      MediaItem(
        id = "trending_shadow_syndicate",
        title = "Shadow Syndicate: Nightfall",
        description = "A lone operative hunts down a clandestine cyber-cartel through neon-soaked Tokyo alleys before a global power grid collapse.",
        bannerResName = "hero_action_banner",
        fallbackColorHex = 0xFF1C0E14,
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        type = MediaType.SERIES,
        matchPercentage = 97,
        rating = "8.9",
        year = 2024,
        durationOrSeasons = "3 Seasons",
        qualityBadge = "4K HDR",
        ageRating = "18+",
        genres = listOf("Action", "Crime", "Noir", "Thriller"),
        cast = listOf("Kenji Sato", "Maya Lin", "Christian Bale"),
        audioTracks = defaultAudioTracks,
        subtitleTracks = defaultSubtitleTracks,
        episodes = getSampleEpisodes("shadow_syndicate"),
        isTop10 = true,
        rankNumber = 2
      ),
      MediaItem(
        id = "trending_stadium_glory",
        title = "Championship Clash: The Final Derby",
        description = "Exclusive documentary and match series capturing the raw emotion, intense locker room tactics, and historic European clash.",
        bannerResName = "live_sports_banner",
        fallbackColorHex = 0xFF0E2319,
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
        type = MediaType.MOVIE,
        matchPercentage = 96,
        rating = "9.0",
        year = 2025,
        durationOrSeasons = "1h 55m",
        qualityBadge = "4K 60FPS",
        ageRating = "13+",
        genres = listOf("Sports", "Documentary", "Drama"),
        cast = listOf("Kylian Mbappé", "Pep Guardiola", "Carlo Ancelotti"),
        audioTracks = defaultAudioTracks,
        subtitleTracks = defaultSubtitleTracks,
        isTop10 = true,
        rankNumber = 3
      ),
      MediaItem(
        id = "trending_cyber_ronin",
        title = "Cyber Ronin: Neo Kyoto",
        description = "In the year 2088, an augmented samurai with a defective AI chip must protect an orphan whose memory holds the encryption keys to freedom.",
        bannerResName = "hero_action_banner",
        fallbackColorHex = 0xFF2A1032,
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
        type = MediaType.SERIES,
        matchPercentage = 95,
        rating = "8.8",
        year = 2024,
        durationOrSeasons = "2 Seasons",
        qualityBadge = "4K Ultra HD",
        ageRating = "16+",
        genres = listOf("Anime", "Action", "Cyberpunk"),
        cast = listOf("Hiroshi Kamiya", "Aoi Yuki", "Mamoru Miyano"),
        audioTracks = defaultAudioTracks,
        subtitleTracks = defaultSubtitleTracks,
        episodes = getSampleEpisodes("cyber_ronin"),
        isTop10 = true,
        rankNumber = 4
      ),
      MediaItem(
        id = "trending_desert_oasis",
        title = "Echoes of the Silk Dunes",
        description = "An archaeological excavation in the Rub' al Khali uncovers a legendary subterranean citadel engineered before recorded history.",
        bannerResName = "hero_movie_banner",
        fallbackColorHex = 0xFF2D1B08,
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
        type = MediaType.MOVIE,
        matchPercentage = 94,
        rating = "8.7",
        year = 2024,
        durationOrSeasons = "2h 10m",
        qualityBadge = "4K HDR",
        ageRating = "13+",
        genres = listOf("Adventure", "Mystery", "History"),
        cast = listOf("Tarek Al-Masri", "Nour Ghadir", "Liam Neeson"),
        audioTracks = defaultAudioTracks,
        subtitleTracks = defaultSubtitleTracks,
        isTop10 = true,
        rankNumber = 5
      )
    )
  }

  fun getActionBlockbusters(): List<MediaItem> {
    return listOf(
      MediaItem(
        id = "act_1",
        title = "Apex Protocol",
        description = "A tactical extraction squad infiltrates an offshore oil rig converted into an AI weapon testing facility.",
        bannerResName = "hero_action_banner",
        fallbackColorHex = 0xFF181014,
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
        type = MediaType.MOVIE,
        matchPercentage = 98,
        rating = "8.6",
        year = 2025,
        durationOrSeasons = "2h 02m",
        qualityBadge = "4K Ultra HD",
        ageRating = "18+",
        genres = listOf("Action", "Suspense", "Thriller"),
        cast = listOf("Tom Hardy", "Charlize Theron", "Idris Elba"),
        audioTracks = defaultAudioTracks,
        subtitleTracks = defaultSubtitleTracks
      ),
      MediaItem(
        id = "act_2",
        title = "Velocity Zero",
        description = "High-octane underground hypercar street racers get pulled into an international heist targeting black-market quantum batteries.",
        bannerResName = "live_sports_banner",
        fallbackColorHex = 0xFF0E1A29,
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        type = MediaType.MOVIE,
        matchPercentage = 93,
        rating = "8.4",
        year = 2024,
        durationOrSeasons = "1h 48m",
        qualityBadge = "HDR 10+",
        ageRating = "16+",
        genres = listOf("Action", "Speed", "Crime"),
        cast = listOf("Paul Walker Jr.", "Michelle Rodriguez"),
        audioTracks = defaultAudioTracks,
        subtitleTracks = defaultSubtitleTracks
      ),
      MediaItem(
        id = "act_3",
        title = "Siege of Alexandria",
        description = "A naval defense fleet stands alone against an armada of rogue automated drone gunships.",
        bannerResName = "hero_movie_banner",
        fallbackColorHex = 0xFF0B2129,
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
        type = MediaType.MOVIE,
        matchPercentage = 95,
        rating = "8.8",
        year = 2024,
        durationOrSeasons = "2h 20m",
        qualityBadge = "4K Ultra HD",
        ageRating = "16+",
        genres = listOf("Action", "Military", "Sci-Fi"),
        cast = listOf("Oscar Isaac", "Zoe Saldana"),
        audioTracks = defaultAudioTracks,
        subtitleTracks = defaultSubtitleTracks
      )
    )
  }

  fun getDramaSeries(): List<MediaItem> {
    return listOf(
      MediaItem(
        id = "drama_1",
        title = "Crown & Silicon",
        description = "The rise, internal betrayals, and ruthless boardroom battles of Europe's most secretive tech dynasty.",
        bannerResName = "hero_action_banner",
        fallbackColorHex = 0xFF17131B,
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
        type = MediaType.SERIES,
        matchPercentage = 99,
        rating = "9.3",
        year = 2024,
        durationOrSeasons = "4 Seasons",
        qualityBadge = "4K Dolby Vision",
        ageRating = "18+",
        genres = listOf("Drama", "Political", "Thriller"),
        cast = listOf("Brian Cox", "Jeremy Strong", "Sarah Snook"),
        audioTracks = defaultAudioTracks,
        subtitleTracks = defaultSubtitleTracks,
        episodes = getSampleEpisodes("crown_silicon")
      ),
      MediaItem(
        id = "drama_2",
        title = "The Merchant of Granada",
        description = "An intricate historical saga of diplomacy, love, and survival in 15th-century Andalusia.",
        bannerResName = "hero_movie_banner",
        fallbackColorHex = 0xFF24150D,
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
        type = MediaType.SERIES,
        matchPercentage = 96,
        rating = "8.9",
        year = 2023,
        durationOrSeasons = "2 Seasons",
        qualityBadge = "4K HDR",
        ageRating = "16+",
        genres = listOf("Drama", "Historical", "Romance"),
        cast = listOf("Antonio Banderas", "Hiam Abbass", "Javier Bardem"),
        audioTracks = defaultAudioTracks,
        subtitleTracks = defaultSubtitleTracks,
        episodes = getSampleEpisodes("granada")
      )
    )
  }

  fun getAnimeMasterpieces(): List<MediaItem> {
    return listOf(
      MediaItem(
        id = "anime_1",
        title = "Blade of the Celestial Dragon",
        description = "A banished swordsman with cursed spirit eyes seeks redemption against nine demonic warlords across feudal lands.",
        bannerResName = "hero_action_banner",
        fallbackColorHex = 0xFF280B18,
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        type = MediaType.SERIES,
        matchPercentage = 98,
        rating = "9.1",
        year = 2025,
        durationOrSeasons = "3 Seasons",
        qualityBadge = "4K Ultra HD",
        ageRating = "16+",
        genres = listOf("Anime", "Supernatural", "Action"),
        cast = listOf("Natsuki Hanae", "Akari Kito", "Hiro Shimono"),
        audioTracks = defaultAudioTracks,
        subtitleTracks = defaultSubtitleTracks,
        episodes = getSampleEpisodes("celestial_dragon")
      ),
      MediaItem(
        id = "anime_2",
        title = "Solaris Children: Horizon Beyond",
        description = "Two young astronomers discover strange celestial signals guiding a migratory cloud whale above the troposphere.",
        bannerResName = "hero_movie_banner",
        fallbackColorHex = 0xFF0D1B28,
        videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
        type = MediaType.MOVIE,
        matchPercentage = 97,
        rating = "8.9",
        year = 2024,
        durationOrSeasons = "1h 58m",
        qualityBadge = "4K HDR",
        ageRating = "All",
        genres = listOf("Anime", "Fantasy", "Adventure"),
        cast = listOf("Makoto Shinkai", "Ryunosuke Kamiki"),
        audioTracks = defaultAudioTracks,
        subtitleTracks = defaultSubtitleTracks
      )
    )
  }

  fun getLiveChannels(): List<LiveChannel> {
    return listOf(
      LiveChannel(
        id = "live_ch_sports_1",
        channelNumber = 101,
        name = "Arena Sports 1 HD",
        category = LiveCategory.SPORTS,
        logoResName = "live_sports_banner",
        streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
        currentProgram = "UEFA Champions League: Madrid vs Munich",
        currentProgramTime = "20:00 - 22:00",
        progressFraction = 0.65f,
        minutesRemaining = 38,
        upcomingProgram = "Post-Match Analysis & Studio Highlights",
        upcomingProgramTime = "22:00",
        viewersCount = "1.4M",
        isLive = true,
        audioTracks = defaultAudioTracks,
        subtitleTracks = defaultSubtitleTracks
      ),
      LiveChannel(
        id = "live_ch_news_1",
        channelNumber = 102,
        name = "Global News 24/7",
        category = LiveCategory.NEWS,
        logoResName = "hero_action_banner",
        streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
        currentProgram = "World News Hour: Live from London & Dubai",
        currentProgramTime = "20:30 - 21:30",
        progressFraction = 0.40f,
        minutesRemaining = 32,
        upcomingProgram = "Global Financial Markets Recap",
        upcomingProgramTime = "21:30",
        viewersCount = "890K",
        isLive = true,
        audioTracks = defaultAudioTracks,
        subtitleTracks = defaultSubtitleTracks
      ),
      LiveChannel(
        id = "live_ch_cinema_1",
        channelNumber = 103,
        name = "CineMax Premiere HD",
        category = LiveCategory.CINEMA,
        logoResName = "hero_movie_banner",
        streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
        currentProgram = "Interstellar Odyssey (Director's Cut)",
        currentProgramTime = "19:00 - 22:15",
        progressFraction = 0.72f,
        minutesRemaining = 45,
        upcomingProgram = "Midnight Action: Extraction Point",
        upcomingProgramTime = "22:15",
        viewersCount = "620K",
        isLive = true,
        audioTracks = defaultAudioTracks,
        subtitleTracks = defaultSubtitleTracks
      ),
      LiveChannel(
        id = "live_ch_ent_1",
        channelNumber = 104,
        name = "Prime Entertainment Live",
        category = LiveCategory.ENTERTAINMENT,
        logoResName = "hero_action_banner",
        streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        currentProgram = "The Late Night Comedy Showcase",
        currentProgramTime = "20:45 - 22:00",
        progressFraction = 0.30f,
        minutesRemaining = 52,
        upcomingProgram = "Celebrity Roast & Red Carpet Live",
        upcomingProgramTime = "22:00",
        viewersCount = "410K",
        isLive = true,
        audioTracks = defaultAudioTracks,
        subtitleTracks = defaultSubtitleTracks
      ),
      LiveChannel(
        id = "live_ch_kids_1",
        channelNumber = 105,
        name = "ToonMax Kids & Family",
        category = LiveCategory.KIDS,
        logoResName = "hero_movie_banner",
        streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        currentProgram = "Adventures of the Forest Heroes",
        currentProgramTime = "20:15 - 21:00",
        progressFraction = 0.85f,
        minutesRemaining = 12,
        upcomingProgram = "Magical Kingdom Chronicles",
        upcomingProgramTime = "21:00",
        viewersCount = "330K",
        isLive = true,
        audioTracks = defaultAudioTracks,
        subtitleTracks = defaultSubtitleTracks
      ),
      LiveChannel(
        id = "live_ch_sports_2",
        channelNumber = 106,
        name = "Velocity Formula Racing",
        category = LiveCategory.SPORTS,
        logoResName = "live_sports_banner",
        streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
        currentProgram = "Grand Prix Night Qualifying: Abu Dhabi",
        currentProgramTime = "20:00 - 21:45",
        progressFraction = 0.55f,
        minutesRemaining = 42,
        upcomingProgram = "Pit Lane Telegraph & Interviews",
        upcomingProgramTime = "21:45",
        viewersCount = "1.1M",
        isLive = true,
        audioTracks = defaultAudioTracks,
        subtitleTracks = defaultSubtitleTracks
      )
    )
  }

  fun getContinueWatching(): List<MediaItem> {
    return listOf(
      getTrendingTop10()[1].copy(watchedProgressFraction = 0.65f),
      getActionBlockbusters()[0].copy(watchedProgressFraction = 0.35f),
      getDramaSeries()[0].copy(watchedProgressFraction = 0.80f)
    )
  }

  private fun getSampleEpisodes(seriesKey: String): List<Episode> {
    return listOf(
      Episode(
        id = "${seriesKey}_ep1",
        episodeNumber = 1,
        title = "Episode 1: The Descent",
        duration = "54m",
        description = "An unexpected emergency beacon summons the reconnaissance squad into forbidden orbit.",
        streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
      ),
      Episode(
        id = "${seriesKey}_ep2",
        episodeNumber = 2,
        title = "Episode 2: Ghost Signal",
        duration = "49m",
        description = "Decrypted logs reveal a classified syndicate infiltration within the core network.",
        streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4"
      ),
      Episode(
        id = "${seriesKey}_ep3",
        episodeNumber = 3,
        title = "Episode 3: Point of No Return",
        duration = "58m",
        description = "With atmospheric shields failing, the crew orchestrates a daring breach at midnight.",
        streamUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
      )
    )
  }
}
