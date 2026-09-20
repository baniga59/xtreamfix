package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.data.localization.Translation
import com.example.ui.components.LanguageDialog
import com.example.ui.components.MovieDetailsBottomSheet
import com.example.ui.components.StreamBottomNavigationBar
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LanguageSelectionScreen
import com.example.ui.screens.LiveTvScreen
import com.example.ui.screens.MyListScreen
import com.example.ui.screens.PlayerScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.StreamDarkBackground
import com.example.ui.viewmodel.MainTab
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

  private val mainViewModel: MainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      MyApplicationTheme {
        StreamFlixApp(mainViewModel = mainViewModel)
      }
    }
  }
}

@Composable
fun StreamFlixApp(mainViewModel: MainViewModel) {
  val uiState by mainViewModel.uiState.collectAsState()
  var showLanguageDialog by remember { mutableStateOf(false) }

  // Initial Language Selection startup screen
  if (!uiState.hasCompletedLanguageSetup) {
    LanguageSelectionScreen(
      initialLanguage = uiState.currentLanguage,
      onLanguageConfirmed = { chosenLang ->
        mainViewModel.completeLanguageSetup(chosenLang)
      }
    )
    return
  }

  // Active Layout Direction (RTL for Arabic, LTR for others)
  val layoutDirection = if (uiState.currentLanguage.isRtl) {
    LayoutDirection.Rtl
  } else {
    LayoutDirection.Ltr
  }

  CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(StreamDarkBackground)
    ) {
      // Main App Scaffold with Bottom Navigation Bar
      Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = StreamDarkBackground,
        bottomBar = {
          // Hide bottom bar when video player is full screen
          if (uiState.activePlayerMedia == null && uiState.activePlayerChannel == null) {
            StreamBottomNavigationBar(
              activeTab = uiState.activeTab,
              strings = uiState.strings,
              onTabSelected = { tab ->
                if (tab == MainTab.SETTINGS) {
                  showLanguageDialog = true
                } else {
                  mainViewModel.selectTab(tab)
                }
              }
            )
          }
        }
      ) { innerPadding ->
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
          Crossfade(
            targetState = uiState.activeTab,
            label = "tab_crossfade"
          ) { tab ->
            when (tab) {
              MainTab.HOME -> {
                HomeScreen(
                  featuredMedia = uiState.featuredMedia,
                  trendingList = uiState.trendingList,
                  actionList = uiState.actionList,
                  dramaList = uiState.dramaList,
                  animeList = uiState.animeList,
                  continueWatching = uiState.continueWatching,
                  liveChannels = uiState.liveChannels,
                  watchlistIds = uiState.watchlistIds,
                  currentLanguage = uiState.currentLanguage,
                  strings = uiState.strings,
                  onMediaClick = { mainViewModel.openDetails(it) },
                  onPlayMedia = { mainViewModel.playMedia(it) },
                  onToggleWatchlist = { mainViewModel.toggleWatchlist(it) },
                  onPlayChannel = { mainViewModel.playChannel(it) },
                  onOpenLanguageDialog = { showLanguageDialog = true },
                  onSearchClick = { mainViewModel.selectTab(MainTab.SEARCH) }
                )
              }

              MainTab.LIVE_TV -> {
                LiveTvScreen(
                  channels = uiState.liveChannels,
                  activeCategory = uiState.activeLiveCategory,
                  strings = uiState.strings,
                  onCategorySelected = { mainViewModel.selectLiveCategory(it) },
                  onPlayChannel = { mainViewModel.playChannel(it) }
                )
              }

              MainTab.SEARCH -> {
                SearchScreen(
                  searchQuery = uiState.searchQuery,
                  searchResults = uiState.searchResults,
                  trendingList = uiState.trendingList,
                  watchlistIds = uiState.watchlistIds,
                  strings = uiState.strings,
                  onQueryChange = { mainViewModel.setSearchQuery(it) },
                  onGenreSelected = { mainViewModel.filterByGenre(it) },
                  onMediaClick = { mainViewModel.openDetails(it) },
                  onToggleWatchlist = { mainViewModel.toggleWatchlist(it) }
                )
              }

              MainTab.MY_LIST -> {
                val savedItems = (uiState.trendingList + uiState.actionList + uiState.dramaList + uiState.animeList)
                  .distinctBy { it.id }
                  .filter { uiState.watchlistIds.contains(it.id) }

                MyListScreen(
                  watchlistItems = savedItems,
                  watchlistIds = uiState.watchlistIds,
                  currentLanguage = uiState.currentLanguage,
                  strings = uiState.strings,
                  onMediaClick = { mainViewModel.openDetails(it) },
                  onToggleWatchlist = { mainViewModel.toggleWatchlist(it) },
                  onChangeLanguageClick = { showLanguageDialog = true }
                )
              }

              MainTab.SETTINGS -> {
                // Fallback, should trigger showLanguageDialog
              }
            }
          }
        }
      }

      // Movie Details Bottom Sheet
      uiState.selectedDetailsMedia?.let { detailsMedia ->
        MovieDetailsBottomSheet(
          mediaItem = detailsMedia,
          isInWatchlist = uiState.watchlistIds.contains(detailsMedia.id),
          strings = uiState.strings,
          onDismiss = { mainViewModel.closeDetails() },
          onPlay = { mainViewModel.playMedia(it) },
          onToggleWatchlist = { mainViewModel.toggleWatchlist(it) }
        )
      }

      // Advanced Netflix/Prime Fullscreen Video Player for Movies & Series
      uiState.activePlayerMedia?.let { playerMedia ->
        PlayerScreen(
          title = playerMedia.title,
          subtitleInfo = "${playerMedia.year} • ${playerMedia.durationOrSeasons} • ${playerMedia.qualityBadge}",
          videoUrl = playerMedia.videoUrl,
          audioTracks = playerMedia.audioTracks,
          subtitleTracks = playerMedia.subtitleTracks,
          strings = uiState.strings,
          onClosePlayer = { mainViewModel.closePlayer() }
        )
      }

      // Advanced Live TV Player for Live Streaming Channels
      uiState.activePlayerChannel?.let { channel ->
        PlayerScreen(
          title = channel.name,
          subtitleInfo = "LIVE • ${channel.currentProgram} • ${channel.currentProgramTime}",
          videoUrl = channel.streamUrl,
          audioTracks = channel.audioTracks,
          subtitleTracks = channel.subtitleTracks,
          strings = uiState.strings,
          onClosePlayer = { mainViewModel.closePlayer() }
        )
      }

      // Language Quick Switcher Dialog
      if (showLanguageDialog) {
        LanguageDialog(
          currentLanguage = uiState.currentLanguage,
          strings = uiState.strings,
          onLanguageSelected = { newLang ->
            mainViewModel.setLanguage(newLang)
          },
          onDismiss = { showLanguageDialog = false }
        )
      }
    }
  }
}
