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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.Translation
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
fun SearchScreen(
  searchQuery: String,
  searchResults: List<MediaItem>,
  trendingList: List<MediaItem>,
  watchlistIds: Set<String>,
  strings: Translation,
  onQueryChange: (String) -> Unit,
  onGenreSelected: (String) -> Unit,
  onMediaClick: (MediaItem) -> Unit,
  onToggleWatchlist: (String) -> Unit
) {
  val genres = listOf("Action", "Sci-Fi", "Drama", "Anime", "Sports", "Cyberpunk", "Crime")
  val displayItems = if (searchQuery.isBlank()) trendingList else searchResults

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

      // Search TextField
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onQueryChange,
        placeholder = {
          Text(
            text = strings.searchPlaceholder,
            color = StreamTextMuted,
            fontSize = 14.sp
          )
        },
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint = StreamTextSecondary
          )
        },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { onQueryChange("") }) {
              Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear",
                tint = StreamTextSecondary
              )
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = StreamDarkCard,
          unfocusedContainerColor = StreamDarkCard,
          focusedBorderColor = BrandRed,
          unfocusedBorderColor = StreamDarkElevated,
          focusedTextColor = StreamTextPrimary,
          unfocusedTextColor = StreamTextPrimary
        ),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("search_text_input")
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Genre Filter Chips
      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(genres) { genre ->
          val isSelected = searchQuery.equals(genre, ignoreCase = true)
          Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (isSelected) BrandRed else StreamDarkElevated,
            border = BorderStroke(1.dp, if (isSelected) BrandRed else Color.Transparent),
            modifier = Modifier
              .clickable { onGenreSelected(if (isSelected) "" else genre) }
              .testTag("genre_chip_${genre.lowercase()}")
          ) {
            Text(
              text = genre,
              color = if (isSelected) Color.White else StreamTextSecondary,
              fontSize = 12.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = if (searchQuery.isBlank()) strings.trendingNow else "Results for \"$searchQuery\"",
        color = StreamTextPrimary,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold
      )

      Spacer(modifier = Modifier.height(12.dp))

      if (displayItems.isEmpty()) {
        // Empty state
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
              imageVector = Icons.Default.Movie,
              contentDescription = null,
              tint = StreamTextMuted,
              modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = strings.noResults,
              color = StreamTextSecondary,
              fontSize = 14.sp,
              textAlign = TextAlign.Center
            )
          }
        }
      } else {
        // Results 2-column or 3-column Grid
        LazyVerticalGrid(
          columns = GridCells.Adaptive(110.dp),
          modifier = Modifier.weight(1f),
          horizontalArrangement = Arrangement.spacedBy(10.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp),
          contentPadding = PaddingValues(bottom = 90.dp)
        ) {
          items(displayItems) { media ->
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
