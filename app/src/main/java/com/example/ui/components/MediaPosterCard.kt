package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MediaItem
import com.example.ui.theme.Badge4K
import com.example.ui.theme.BadgeHD
import com.example.ui.theme.BrandRed
import com.example.ui.theme.RatingGold
import com.example.ui.theme.StreamDarkCard
import com.example.ui.theme.StreamDarkElevated
import com.example.ui.theme.StreamTextPrimary
import com.example.ui.theme.StreamTextSecondary

@Composable
fun MediaPosterCard(
  mediaItem: MediaItem,
  isInWatchlist: Boolean,
  onClick: () -> Unit,
  onToggleWatchlist: () -> Unit,
  modifier: Modifier = Modifier,
  cardWidth: Int = 135,
  cardHeight: Int = 200,
  showRank: Boolean = false,
  rankNumber: Int? = null
) {
  val context = LocalContext.current
  val resId = rememberDrawableResId(mediaItem.bannerResName)

  Box(
    modifier = modifier
      .width(if (showRank) (cardWidth + 24).dp else cardWidth.dp)
      .clickable { onClick() }
      .testTag("media_card_${mediaItem.id}")
  ) {
    Row(verticalAlignment = Alignment.Bottom) {
      // Large Netflix-style Rank Number (1, 2, 3...)
      if (showRank && rankNumber != null) {
        Box(
          modifier = Modifier
            .width(36.dp)
            .height(cardHeight.dp),
          contentAlignment = Alignment.BottomCenter
        ) {
          Text(
            text = "$rankNumber",
            fontSize = 72.sp,
            fontWeight = FontWeight.Black,
            color = Color.White.copy(alpha = 0.85f),
            modifier = Modifier.padding(bottom = 8.dp)
          )
        }
      }

      Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = StreamDarkCard),
        border = BorderStroke(1.dp, StreamDarkElevated),
        modifier = Modifier
          .width(cardWidth.dp)
          .height(cardHeight.dp)
      ) {
        Box(modifier = Modifier.fillMaxSize()) {
          // Poster Image or Fallback Gradient
          if (resId != 0) {
            Image(
              painter = painterResource(id = resId),
              contentDescription = mediaItem.title,
              modifier = Modifier.fillMaxSize(),
              contentScale = ContentScale.Crop
            )
          } else {
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(
                  Brush.verticalGradient(
                    colors = listOf(
                      Color(mediaItem.fallbackColorHex),
                      StreamDarkCard
                    )
                  )
                )
            )
          }

          // Dark subtle gradient overlay at bottom
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.verticalGradient(
                  colors = listOf(
                    Color.Transparent,
                    Color.Transparent,
                    Color(0xEE09090D)
                  )
                )
              )
          )

          // Top Badges (Quality & Bookmark)
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(8.dp),
            verticalAlignment = Alignment.Top
          ) {
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = Color.Black.copy(alpha = 0.7f),
              border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.3f))
            ) {
              Text(
                text = if (mediaItem.qualityBadge.contains("4K")) "4K" else "HD",
                color = if (mediaItem.qualityBadge.contains("4K")) Badge4K else BadgeHD,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
              )
            }

            Spacer(modifier = Modifier.weight(1f))

            Surface(
              shape = CircleShape,
              color = Color.Black.copy(alpha = 0.6f),
              modifier = Modifier
                .size(28.dp)
                .clickable { onToggleWatchlist() }
            ) {
              Box(contentAlignment = Alignment.Center) {
                Icon(
                  imageVector = if (isInWatchlist) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                  contentDescription = "Watchlist",
                  tint = if (isInWatchlist) BrandRed else Color.White,
                  modifier = Modifier.size(16.dp)
                )
              }
            }
          }

          // Bottom Content Details (Title, Match %, Year)
          Column(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .fillMaxWidth()
              .padding(8.dp)
          ) {
            Text(
              text = mediaItem.title,
              color = StreamTextPrimary,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "${mediaItem.matchPercentage}% Match",
                color = Badge4K,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "${mediaItem.year}",
                color = StreamTextSecondary,
                fontSize = 10.sp
              )
            }

            // Watch Progress Bar if applicable
            if (mediaItem.watchedProgressFraction > 0f) {
              Spacer(modifier = Modifier.height(4.dp))
              LinearProgressIndicator(
                progress = { mediaItem.watchedProgressFraction },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(3.dp)
                  .clip(RoundedCornerShape(2.dp)),
                color = BrandRed,
                trackColor = Color.White.copy(alpha = 0.3f)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun rememberDrawableResId(name: String): Int {
  val context = LocalContext.current
  return remember(name) {
    if (name.isEmpty()) 0
    else {
      context.resources.getIdentifier(name, "drawable", context.packageName)
    }
  }
}
