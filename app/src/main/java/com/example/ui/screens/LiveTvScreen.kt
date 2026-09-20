package com.example.ui.screens

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.Translation
import com.example.data.model.LiveCategory
import com.example.data.model.LiveChannel
import com.example.ui.components.rememberDrawableResId
import com.example.ui.theme.AccentAmber
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
fun LiveTvScreen(
  channels: List<LiveChannel>,
  activeCategory: LiveCategory,
  strings: Translation,
  onCategorySelected: (LiveCategory) -> Unit,
  onPlayChannel: (LiveChannel) -> Unit
) {
  val filteredChannels = if (activeCategory == LiveCategory.ALL) {
    channels
  } else {
    channels.filter { it.category == activeCategory }
  }

  val featuredChannel = channels.firstOrNull()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(StreamDarkBackground)
  ) {
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      contentPadding = PaddingValues(bottom = 90.dp)
    ) {
      // Screen Header
      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .clip(CircleShape)
              .background(LiveBadgeRed)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = strings.liveBroadcasts,
            color = StreamTextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      // Live TV Hero Showcase
      if (featuredChannel != null) {
        item {
          LiveTvHeroCard(
            channel = featuredChannel,
            strings = strings,
            onPlay = { onPlayChannel(featuredChannel) }
          )
        }
      }

      // Category Filter Chips
      item {
        CategoryFilterRow(
          categories = LiveCategory.values().toList(),
          activeCategory = activeCategory,
          strings = strings,
          onSelectCategory = onCategorySelected
        )
      }

      // EPG Channel Guide List
      items(filteredChannels) { channel ->
        EpgChannelCard(
          channel = channel,
          strings = strings,
          onPlay = { onPlayChannel(channel) }
        )
      }
    }
  }
}

@Composable
fun LiveTvHeroCard(
  channel: LiveChannel,
  strings: Translation,
  onPlay: () -> Unit
) {
  val bannerResId = rememberDrawableResId(channel.logoResName)

  Card(
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = StreamDarkCard),
    border = BorderStroke(1.dp, StreamDarkElevated),
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .clickable { onPlay() }
      .testTag("featured_live_card")
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
    ) {
      if (bannerResId != 0) {
        Image(
          painter = painterResource(id = bannerResId),
          contentDescription = channel.name,
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )
      }

      // Gradient overlay
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color(0x66000000),
                Color(0xCC09090D)
              )
            )
          )
      )

      // Live badge & Viewers Count
      Row(
        modifier = Modifier
          .align(Alignment.TopStart)
          .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(4.dp),
          color = LiveBadgeRed
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(Color.White)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "LIVE",
              color = Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.Black
            )
          }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Surface(
          shape = RoundedCornerShape(4.dp),
          color = Color.Black.copy(alpha = 0.6f)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Visibility,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "${channel.viewersCount} ${strings.viewers}",
              color = Color.White,
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }

      // Center Play Button
      Surface(
        shape = CircleShape,
        color = BrandRed,
        modifier = Modifier
          .align(Alignment.Center)
          .size(54.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = strings.watchLive,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
          )
        }
      }

      // Bottom Info (Channel name, current show, time)
      Column(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .fillMaxWidth()
          .padding(14.dp)
      ) {
        Text(
          text = channel.name,
          color = StreamTextSecondary,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold
        )
        Text(
          text = channel.currentProgram,
          color = StreamTextPrimary,
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold
        )
      }
    }
  }
}

@Composable
fun CategoryFilterRow(
  categories: List<LiveCategory>,
  activeCategory: LiveCategory,
  strings: Translation,
  onSelectCategory: (LiveCategory) -> Unit
) {
  LazyRow(
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    items(categories) { cat ->
      val isSelected = cat == activeCategory
      val label = when (cat) {
        LiveCategory.ALL -> strings.allChannels
        LiveCategory.NEWS -> strings.news
        LiveCategory.SPORTS -> strings.sports
        LiveCategory.CINEMA -> strings.cinema
        LiveCategory.ENTERTAINMENT -> strings.entertainment
        LiveCategory.KIDS -> strings.kids
      }

      val bgColor by animateColorAsState(
        targetValue = if (isSelected) BrandRed else StreamDarkElevated,
        label = "cat_bg"
      )

      Surface(
        shape = RoundedCornerShape(20.dp),
        color = bgColor,
        border = BorderStroke(1.dp, if (isSelected) BrandRed else Color.Transparent),
        modifier = Modifier
          .clickable { onSelectCategory(cat) }
          .testTag("live_category_${cat.name.lowercase()}")
      ) {
        Text(
          text = label,
          color = if (isSelected) Color.White else StreamTextSecondary,
          fontSize = 13.sp,
          fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
      }
    }
  }
}

@Composable
fun EpgChannelCard(
  channel: LiveChannel,
  strings: Translation,
  onPlay: () -> Unit
) {
  val bannerResId = rememberDrawableResId(channel.logoResName)

  Card(
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = StreamDarkCard),
    border = BorderStroke(1.dp, StreamDarkElevated),
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .clickable { onPlay() }
      .testTag("channel_item_${channel.id}")
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Row 1: Channel Logo / Badge, Name, Category, Live indicator, and Watch button
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
      ) {
        Box(
          modifier = Modifier
            .size(width = 56.dp, height = 38.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(StreamDarkElevated),
          contentAlignment = Alignment.Center
        ) {
          if (bannerResId != 0) {
            Image(
              painter = painterResource(id = bannerResId),
              contentDescription = channel.name,
              modifier = Modifier.fillMaxSize(),
              contentScale = ContentScale.Crop
            )
          } else {
            Icon(
              imageVector = Icons.Default.LiveTv,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(20.dp)
            )
          }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = StreamDarkElevated
            ) {
              Text(
                text = "${channel.channelNumber}",
                color = StreamTextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
              )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = channel.name,
              color = StreamTextPrimary,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Text(
            text = channel.category.displayName,
            color = StreamTextMuted,
            fontSize = 11.sp
          )
        }

        Button(
          onClick = onPlay,
          shape = RoundedCornerShape(8.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BrandRed),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
          modifier = Modifier.height(34.dp)
        ) {
          Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = strings.watchLive,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Row 2: Interactive EPG Schedule Block
      Surface(
        shape = RoundedCornerShape(10.dp),
        color = StreamDarkElevated.copy(alpha = 0.7f),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          // Now Playing Title & Time remaining
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            Surface(
              shape = RoundedCornerShape(3.dp),
              color = LiveBadgeRed
            ) {
              Text(
                text = strings.nowPlaying,
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
              )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
              text = channel.currentProgramTime,
              color = StreamTextMuted,
              fontSize = 11.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
              text = "${channel.minutesRemaining}m left",
              color = AccentAmber,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = channel.currentProgram,
            color = StreamTextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
          )

          Spacer(modifier = Modifier.height(6.dp))

          // Real-time show elapsed progress indicator
          LinearProgressIndicator(
            progress = { channel.progressFraction },
            modifier = Modifier
              .fillMaxWidth()
              .height(3.dp)
              .clip(RoundedCornerShape(2.dp)),
            color = LiveBadgeRed,
            trackColor = Color.White.copy(alpha = 0.15f)
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Up Next Line
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "${strings.upNext}: ",
              color = StreamTextMuted,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "${channel.upcomingProgram} (${channel.upcomingProgramTime})",
              color = StreamTextSecondary,
              fontSize = 11.sp
            )
          }
        }
      }
    }
  }
}
