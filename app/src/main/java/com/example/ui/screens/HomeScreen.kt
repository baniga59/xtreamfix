package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.Translation
import com.example.data.model.AppLanguage
import com.example.data.model.LiveChannel
import com.example.data.model.MediaItem
import com.example.ui.components.MediaPosterCard
import com.example.ui.components.rememberDrawableResId
import com.example.ui.theme.Badge4K
import com.example.ui.theme.BrandRed
import com.example.ui.theme.LiveBadgeRed
import com.example.ui.theme.StreamDarkBackground
import com.example.ui.theme.StreamDarkCard
import com.example.ui.theme.StreamDarkElevated
import com.example.ui.theme.StreamTextMuted
import com.example.ui.theme.StreamTextPrimary
import com.example.ui.theme.StreamTextSecondary

@Composable
fun HomeScreen(
  featuredMedia: MediaItem?,
  trendingList: List<MediaItem>,
  actionList: List<MediaItem>,
  dramaList: List<MediaItem>,
  animeList: List<MediaItem>,
  continueWatching: List<MediaItem>,
  liveChannels: List<LiveChannel>,
  watchlistIds: Set<String>,
  currentLanguage: AppLanguage,
  strings: Translation,
  onMediaClick: (MediaItem) -> Unit,
  onPlayMedia: (MediaItem) -> Unit,
  onToggleWatchlist: (String) -> Unit,
  onPlayChannel: (LiveChannel) -> Unit,
  onOpenLanguageDialog: () -> Unit,
  onSearchClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(StreamDarkBackground)
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 80.dp)
    ) {
      // Top App Bar with Logo, Language selector & Search
      item {
        TopHomeBar(
          currentLanguage = currentLanguage,
          onOpenLanguageDialog = onOpenLanguageDialog,
          onSearchClick = onSearchClick
        )
      }

      // Hero Banner
      if (featuredMedia != null) {
        item {
          HeroBanner(
            media = featuredMedia,
            isInWatchlist = watchlistIds.contains(featuredMedia.id),
            strings = strings,
            onPlayClick = { onPlayMedia(featuredMedia) },
            onInfoClick = { onMediaClick(featuredMedia) },
            onToggleWatchlist = { onToggleWatchlist(featuredMedia.id) }
          )
        }
      }

      // Continue Watching Section
      if (continueWatching.isNotEmpty()) {
        item {
          CarouselSection(
            title = strings.continueWatching,
            items = continueWatching,
            watchlistIds = watchlistIds,
            onMediaClick = onMediaClick,
            onToggleWatchlist = onToggleWatchlist
          )
        }
      }

      // Trending Now - Top 10 (Netflix Rank Numbers)
      item {
        Top10Section(
          title = strings.trendingNow,
          items = trendingList,
          watchlistIds = watchlistIds,
          onMediaClick = onMediaClick,
          onToggleWatchlist = onToggleWatchlist
        )
      }

      // Live Channels Quick Bar
      item {
        LiveChannelsQuickBar(
          title = strings.liveChannels,
          channels = liveChannels,
          strings = strings,
          onChannelClick = onPlayChannel
        )
      }

      // Action Blockbusters
      item {
        CarouselSection(
          title = strings.actionBlockbusters,
          items = actionList,
          watchlistIds = watchlistIds,
          onMediaClick = onMediaClick,
          onToggleWatchlist = onToggleWatchlist
        )
      }

      // Critically Acclaimed Drama
      item {
        CarouselSection(
          title = strings.topDramaSeries,
          items = dramaList,
          watchlistIds = watchlistIds,
          onMediaClick = onMediaClick,
          onToggleWatchlist = onToggleWatchlist
        )
      }

      // Anime & Animation Masterpieces
      item {
        CarouselSection(
          title = strings.animeMasterpieces,
          items = animeList,
          watchlistIds = watchlistIds,
          onMediaClick = onMediaClick,
          onToggleWatchlist = onToggleWatchlist
        )
      }

      item {
        Spacer(modifier = Modifier.height(24.dp))
      }
    }
  }
}

@Composable
fun TopHomeBar(
  currentLanguage: AppLanguage,
  onOpenLanguageDialog: () -> Unit,
  onSearchClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .statusBarsPadding()
      .padding(horizontal = 16.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // StreamFlix Cinematic Brand Logo
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.weight(1f)
    ) {
      Box(
        modifier = Modifier
          .size(32.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(BrandRed),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.PlayArrow,
          contentDescription = "Logo",
          tint = Color.White,
          modifier = Modifier.size(20.dp)
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      Text(
        text = "STREAMFLIX",
        color = BrandRed,
        fontSize = 20.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = 2.sp
      )
    }

    // Language Selector Chip
    Surface(
      shape = RoundedCornerShape(20.dp),
      color = StreamDarkElevated,
      border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
      modifier = Modifier
        .clickable { onOpenLanguageDialog() }
        .testTag("switch_language_chip")
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = currentLanguage.flagEmoji,
          fontSize = 14.sp
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = currentLanguage.nativeName,
          color = StreamTextPrimary,
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }

    Spacer(modifier = Modifier.width(8.dp))

    // Search Icon Button
    IconButton(
      onClick = onSearchClick,
      modifier = Modifier.testTag("top_search_button")
    ) {
      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = "Search",
        tint = Color.White,
        modifier = Modifier.size(24.dp)
      )
    }
  }
}

@Composable
fun HeroBanner(
  media: MediaItem,
  isInWatchlist: Boolean,
  strings: Translation,
  onPlayClick: () -> Unit,
  onInfoClick: () -> Unit,
  onToggleWatchlist: () -> Unit
) {
  val resId = rememberDrawableResId(media.bannerResName)

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(390.dp)
      .padding(horizontal = 12.dp, vertical = 8.dp)
      .clip(RoundedCornerShape(20.dp))
      .testTag("hero_banner")
  ) {
    if (resId != 0) {
      Image(
        painter = painterResource(id = resId),
        contentDescription = media.title,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
      )
    } else {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(Color(media.fallbackColorHex))
      )
    }

    // Multi-stage cinematic dark gradient overlay
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          Brush.verticalGradient(
            colors = listOf(
              Color.Transparent,
              Color(0x5508080C),
              Color(0xDD08080C),
              StreamDarkBackground
            )
          )
        )
    )

    // Hero Content
    Column(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 18.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Top 10 / Quality Badges
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Surface(
          shape = RoundedCornerShape(4.dp),
          color = BrandRed
        ) {
          Text(
            text = "TOP 10 TODAY",
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }

        Surface(
          shape = RoundedCornerShape(4.dp),
          color = Color.Black.copy(alpha = 0.7f),
          border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.3f))
        ) {
          Text(
            text = media.qualityBadge,
            color = Badge4K,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = media.title,
        color = StreamTextPrimary,
        fontSize = 24.sp,
        fontWeight = FontWeight.Black,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Genre Tags
      Text(
        text = media.genres.take(3).joinToString(" • "),
        color = StreamTextSecondary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Action Buttons (Play, My List, Info)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // My List Button
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .weight(1f)
            .clickable { onToggleWatchlist() }
            .testTag("hero_toggle_watchlist")
        ) {
          Icon(
            imageVector = if (isInWatchlist) Icons.Default.Check else Icons.Default.Add,
            contentDescription = strings.addToMyList,
            tint = if (isInWatchlist) BrandRed else Color.White,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = if (isInWatchlist) strings.addedToList else strings.addToMyList,
            color = if (isInWatchlist) BrandRed else StreamTextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        // Prominent Play Button (White pill Netflix style)
        Button(
          onClick = onPlayClick,
          modifier = Modifier
            .weight(1.6f)
            .height(46.dp)
            .testTag("hero_play_button"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
          )
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = null,
              tint = Color.Black,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = strings.play,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = Color.Black
            )
          }
        }

        // Info Button
        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .weight(1f)
            .clickable { onInfoClick() }
            .testTag("hero_info_button")
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = strings.info,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = strings.info,
            color = StreamTextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
          )
        }
      }
    }
  }
}

@Composable
fun CarouselSection(
  title: String,
  items: List<MediaItem>,
  watchlistIds: Set<String>,
  onMediaClick: (MediaItem) -> Unit,
  onToggleWatchlist: (String) -> Unit
) {
  Column(modifier = Modifier.padding(vertical = 10.dp)) {
    Text(
      text = title,
      color = StreamTextPrimary,
      fontSize = 18.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
    )

    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      items(items) { media ->
        MediaPosterCard(
          mediaItem = media,
          isInWatchlist = watchlistIds.contains(media.id),
          onClick = { onMediaClick(media) },
          onToggleWatchlist = { onToggleWatchlist(media.id) }
        )
      }
    }
  }
}

@Composable
fun Top10Section(
  title: String,
  items: List<MediaItem>,
  watchlistIds: Set<String>,
  onMediaClick: (MediaItem) -> Unit,
  onToggleWatchlist: (String) -> Unit
) {
  Column(modifier = Modifier.padding(vertical = 10.dp)) {
    Text(
      text = title,
      color = StreamTextPrimary,
      fontSize = 18.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
    )

    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      itemsIndexed(items) { index, media ->
        MediaPosterCard(
          mediaItem = media,
          isInWatchlist = watchlistIds.contains(media.id),
          onClick = { onMediaClick(media) },
          onToggleWatchlist = { onToggleWatchlist(media.id) },
          showRank = true,
          rankNumber = index + 1
        )
      }
    }
  }
}

@Composable
fun LiveChannelsQuickBar(
  title: String,
  channels: List<LiveChannel>,
  strings: Translation,
  onChannelClick: (LiveChannel) -> Unit
) {
  Column(modifier = Modifier.padding(vertical = 10.dp)) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        imageVector = Icons.Default.LiveTv,
        contentDescription = null,
        tint = LiveBadgeRed,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = title,
        color = StreamTextPrimary,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.weight(1f))
      Surface(
        shape = RoundedCornerShape(4.dp),
        color = LiveBadgeRed
      ) {
        Text(
          text = "LIVE",
          color = Color.White,
          fontSize = 10.sp,
          fontWeight = FontWeight.Black,
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
      }
    }

    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      items(channels) { channel ->
        val bannerResId = rememberDrawableResId(channel.logoResName)
        Card(
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = StreamDarkCard),
          border = BorderStroke(1.dp, StreamDarkElevated),
          modifier = Modifier
            .width(200.dp)
            .height(130.dp)
            .clickable { onChannelClick(channel) }
            .testTag("quick_channel_${channel.id}")
        ) {
          Box(modifier = Modifier.fillMaxSize()) {
            if (bannerResId != 0) {
              Image(
                painter = painterResource(id = bannerResId),
                contentDescription = channel.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
              )
            } else {
              Box(
                modifier = Modifier
                  .fillMaxSize()
                  .background(StreamDarkElevated)
              )
            }

            // Dark gradient overlay
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(
                  Brush.verticalGradient(
                    colors = listOf(
                      Color(0x33000000),
                      Color(0xCC000000)
                    )
                  )
                )
            )

            // Live red badge top left
            Row(
              modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(LiveBadgeRed)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = channel.category.displayName,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }

            // Channel name and current program at bottom
            Column(
              modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
            ) {
              Text(
                text = channel.name,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
              )
              Text(
                text = channel.currentProgram,
                color = StreamTextSecondary,
                fontSize = 11.sp,
                maxLines = 1
              )
            }
          }
        }
      }
    }
  }
}
