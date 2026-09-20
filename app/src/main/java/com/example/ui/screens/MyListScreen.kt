package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.Translation
import com.example.data.model.AppLanguage
import com.example.data.model.MediaItem
import com.example.ui.components.MediaPosterCard
import com.example.ui.theme.BrandRed
import com.example.ui.theme.StreamDarkBackground
import com.example.ui.theme.StreamDarkCard
import com.example.ui.theme.StreamDarkElevated
import com.example.ui.theme.StreamTextMuted
import com.example.ui.theme.StreamTextPrimary
import com.example.ui.theme.StreamTextSecondary

@Composable
fun MyListScreen(
  watchlistItems: List<MediaItem>,
  watchlistIds: Set<String>,
  currentLanguage: AppLanguage,
  strings: Translation,
  onMediaClick: (MediaItem) -> Unit,
  onToggleWatchlist: (String) -> Unit,
  onChangeLanguageClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(StreamDarkBackground)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .padding(horizontal = 16.dp)
    ) {
      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = strings.myList,
        color = StreamTextPrimary,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Language Switcher Banner in Settings / My List
      Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = StreamDarkCard),
        border = BorderStroke(1.dp, StreamDarkElevated),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onChangeLanguageClick() }
          .testTag("my_list_change_language_card")
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(RoundedCornerShape(10.dp))
              .background(BrandRed.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Translate,
              contentDescription = null,
              tint = BrandRed,
              modifier = Modifier.size(22.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "${strings.switchLanguage} (${currentLanguage.nativeName})",
              color = StreamTextPrimary,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = strings.changeLanguageAnytime,
              color = StreamTextMuted,
              fontSize = 11.sp
            )
          }

          Text(
            text = currentLanguage.flagEmoji,
            fontSize = 22.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      if (watchlistItems.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.BookmarkBorder,
              contentDescription = null,
              tint = StreamTextMuted,
              modifier = Modifier.size(54.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "No saved movies or series yet",
              color = StreamTextSecondary,
              fontSize = 15.sp,
              fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
              text = "Tap the bookmark icon on any title to save it to your personal watchlist",
              color = StreamTextMuted,
              fontSize = 12.sp,
              textAlign = TextAlign.Center,
              modifier = Modifier.padding(horizontal = 32.dp)
            )
          }
        }
      } else {
        LazyVerticalGrid(
          columns = GridCells.Adaptive(110.dp),
          modifier = Modifier.weight(1f),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
          contentPadding = PaddingValues(bottom = 90.dp)
        ) {
          items(watchlistItems) { media ->
            MediaPosterCard(
              mediaItem = media,
              isInWatchlist = watchlistIds.contains(media.id),
              onClick = { onMediaClick(media) },
              onToggleWatchlist = { onToggleWatchlist(media.id) },
              cardWidth = 110,
              cardHeight = 165
            )
          }
        }
      }
    }
  }
}
