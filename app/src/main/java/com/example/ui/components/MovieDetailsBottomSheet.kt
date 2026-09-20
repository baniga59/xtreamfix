package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.Translation
import com.example.data.model.MediaItem
import com.example.data.model.MediaType
import com.example.ui.theme.Badge4K
import com.example.ui.theme.BrandRed
import com.example.ui.theme.StreamDarkBackground
import com.example.ui.theme.StreamDarkCard
import com.example.ui.theme.StreamDarkElevated
import com.example.ui.theme.StreamTextMuted
import com.example.ui.theme.StreamTextPrimary
import com.example.ui.theme.StreamTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsBottomSheet(
  mediaItem: MediaItem,
  isInWatchlist: Boolean,
  strings: Translation,
  onDismiss: () -> Unit,
  onPlay: (MediaItem) -> Unit,
  onToggleWatchlist: (String) -> Unit
) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  val bannerResId = rememberDrawableResId(mediaItem.bannerResName)

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = StreamDarkBackground,
    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    dragHandle = null
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 32.dp)
    ) {
      // Hero Media Header Image
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(230.dp)
        ) {
          if (bannerResId != 0) {
            Image(
              painter = painterResource(id = bannerResId),
              contentDescription = mediaItem.title,
              modifier = Modifier.fillMaxSize(),
              contentScale = ContentScale.Crop
            )
          } else {
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(Color(mediaItem.fallbackColorHex))
            )
          }

          // Dark bottom gradient overlay
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.verticalGradient(
                  colors = listOf(
                    Color.Transparent,
                    Color(0x8808080C),
                    StreamDarkBackground
                  )
                )
              )
          )

          // Close Button
          Surface(
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.65f),
            modifier = Modifier
              .align(Alignment.TopEnd)
              .padding(16.dp)
              .size(36.dp)
          ) {
            IconButton(
              onClick = onDismiss,
              modifier = Modifier.testTag("close_details_button")
            ) {
              Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
              )
            }
          }

          // Floating Play Icon on Banner
          Surface(
            shape = CircleShape,
            color = BrandRed,
            modifier = Modifier
              .align(Alignment.Center)
              .size(60.dp)
              .clickable {
                onDismiss()
                onPlay(mediaItem)
              }
              .testTag("banner_center_play_button")
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = strings.play,
                tint = Color.White,
                modifier = Modifier.size(36.dp)
              )
            }
          }
        }
      }

      // Title & Tags Info
      item {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
          Text(
            text = mediaItem.title,
            color = StreamTextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Metadata Chips Row (Match %, Year, Age, Quality, Duration)
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
              text = "${mediaItem.matchPercentage}% ${strings.match}",
              color = Badge4K,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )

            Text(
              text = "${mediaItem.year}",
              color = StreamTextSecondary,
              fontSize = 13.sp
            )

            Surface(
              shape = RoundedCornerShape(4.dp),
              color = StreamDarkElevated
            ) {
              Text(
                text = mediaItem.ageRating,
                color = StreamTextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }

            Surface(
              shape = RoundedCornerShape(4.dp),
              color = BrandRed.copy(alpha = 0.2f),
              border = BorderStroke(0.5.dp, BrandRed.copy(alpha = 0.5f))
            ) {
              Text(
                text = mediaItem.qualityBadge,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }

            Text(
              text = mediaItem.durationOrSeasons,
              color = StreamTextSecondary,
              fontSize = 13.sp
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Primary Play Button
          Button(
            onClick = {
              onDismiss()
              onPlay(mediaItem)
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("details_play_button"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = BrandRed,
              contentColor = Color.White
            )
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = strings.play,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Synopsis
          Text(
            text = mediaItem.description,
            color = StreamTextPrimary,
            fontSize = 14.sp,
            lineHeight = 20.sp
          )

          Spacer(modifier = Modifier.height(16.dp))

          // Cast & Directors
          if (mediaItem.cast.isNotEmpty()) {
            Text(
              text = "Cast: " + mediaItem.cast.joinToString(", "),
              color = StreamTextMuted,
              fontSize = 12.sp,
              maxLines = 2
            )
          }

          if (mediaItem.genres.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Genres: " + mediaItem.genres.joinToString(" • "),
              color = StreamTextMuted,
              fontSize = 12.sp
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Action Toolbar: My List, Share, Download
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier
                .clickable { onToggleWatchlist(mediaItem.id) }
                .padding(8.dp)
                .testTag("details_toggle_watchlist")
            ) {
              Icon(
                imageVector = if (isInWatchlist) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                contentDescription = strings.addToMyList,
                tint = if (isInWatchlist) BrandRed else Color.White,
                modifier = Modifier.size(26.dp)
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = if (isInWatchlist) strings.addedToList else strings.addToMyList,
                color = if (isInWatchlist) BrandRed else StreamTextSecondary,
                fontSize = 11.sp
              )
            }

            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.padding(8.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = Color.White,
                modifier = Modifier.size(26.dp)
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Share",
                color = StreamTextSecondary,
                fontSize = 11.sp
              )
            }

            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.padding(8.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Download,
                contentDescription = strings.downloaded,
                tint = Color.White,
                modifier = Modifier.size(26.dp)
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = strings.downloaded,
                color = StreamTextSecondary,
                fontSize = 11.sp
              )
            }
          }
        }
      }

      // Episodes List if series
      if (mediaItem.type == MediaType.SERIES && mediaItem.episodes.isNotEmpty()) {
        item {
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = strings.episodes,
            color = StreamTextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
          )
        }

        items(mediaItem.episodes) { ep ->
          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = StreamDarkCard),
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 20.dp, vertical = 6.dp)
              .clickable {
                onDismiss()
                onPlay(mediaItem)
              }
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(width = 90.dp, height = 55.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(StreamDarkElevated),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.PlayArrow,
                  contentDescription = "Play Episode",
                  tint = Color.White,
                  modifier = Modifier.size(24.dp)
                )
              }

              Spacer(modifier = Modifier.width(14.dp))

              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = ep.title,
                  color = StreamTextPrimary,
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = ep.duration,
                  color = StreamTextMuted,
                  fontSize = 12.sp
                )
                Text(
                  text = ep.description,
                  color = StreamTextSecondary,
                  fontSize = 11.sp,
                  maxLines = 2
                )
              }
            }
          }
        }
      }
    }
  }
}
