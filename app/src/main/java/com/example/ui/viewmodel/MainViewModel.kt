package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.localization.AppStrings
import com.example.data.localization.Translation
import com.example.data.model.AppLanguage
import com.example.data.model.LiveCategory
import com.example.data.model.LiveChannel
import com.example.data.model.MediaItem
import com.example.data.repository.StreamingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class MainTab {
  HOME,
  LIVE_TV,
  SEARCH,
  MY_LIST,
  SETTINGS
}

data class MainUiState(
  val currentLanguage: AppLanguage = AppLanguage.ENGLISH,
  val hasCompletedLanguageSetup: Boolean = false,
  val activeTab: MainTab = MainTab.HOME,
  val featuredMedia: MediaItem? = null,
  val trendingList: List<MediaItem> = emptyList(),
  val actionList: List<MediaItem> = emptyList(),
  val dramaList: List<MediaItem> = emptyList(),
  val animeList: List<MediaItem> = emptyList(),
  val liveChannels: List<LiveChannel> = emptyList(),
  val continueWatching: List<MediaItem> = emptyList(),
  val watchlistIds: Set<String> = setOf("trending_shadow_syndicate"),
  val activeLiveCategory: LiveCategory = LiveCategory.ALL,
  val activePlayerMedia: MediaItem? = null,
  val activePlayerChannel: LiveChannel? = null,
  val selectedDetailsMedia: MediaItem? = null,
  val searchQuery: String = "",
  val searchResults: List<MediaItem> = emptyList()
) {
  val strings: Translation get() = AppStrings.get(currentLanguage)
}

class MainViewModel(
  private val repository: StreamingRepository = StreamingRepository()
) : ViewModel() {

  private val _uiState = MutableStateFlow(MainUiState())
  val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

  init {
    loadCatalog()
  }

  private fun loadCatalog() {
    val featured = repository.getFeaturedItem()
    val trending = repository.getTrendingTop10()
    val action = repository.getActionBlockbusters()
    val drama = repository.getDramaSeries()
    val anime = repository.getAnimeMasterpieces()
    val channels = repository.getLiveChannels()
    val continueWatching = repository.getContinueWatching()

    _uiState.value = _uiState.value.copy(
      featuredMedia = featured,
      trendingList = trending,
      actionList = action,
      dramaList = drama,
      animeList = anime,
      liveChannels = channels,
      continueWatching = continueWatching
    )
  }

  fun setLanguage(language: AppLanguage) {
    _uiState.value = _uiState.value.copy(currentLanguage = language)
  }

  fun completeLanguageSetup(selectedLanguage: AppLanguage) {
    _uiState.value = _uiState.value.copy(
      currentLanguage = selectedLanguage,
      hasCompletedLanguageSetup = true
    )
  }

  fun reopenLanguageSelection() {
    _uiState.value = _uiState.value.copy(hasCompletedLanguageSetup = false)
  }

  fun selectTab(tab: MainTab) {
    _uiState.value = _uiState.value.copy(activeTab = tab)
  }

  fun selectLiveCategory(category: LiveCategory) {
    _uiState.value = _uiState.value.copy(activeLiveCategory = category)
  }

  fun toggleWatchlist(mediaId: String) {
    val currentSet = _uiState.value.watchlistIds.toMutableSet()
    if (currentSet.contains(mediaId)) {
      currentSet.remove(mediaId)
    } else {
      currentSet.add(mediaId)
    }
    _uiState.value = _uiState.value.copy(watchlistIds = currentSet)
  }

  fun openDetails(mediaItem: MediaItem) {
    _uiState.value = _uiState.value.copy(selectedDetailsMedia = mediaItem)
  }

  fun closeDetails() {
    _uiState.value = _uiState.value.copy(selectedDetailsMedia = null)
  }

  fun playMedia(mediaItem: MediaItem) {
    _uiState.value = _uiState.value.copy(
      activePlayerMedia = mediaItem,
      activePlayerChannel = null,
      selectedDetailsMedia = null
    )
  }

  fun playChannel(channel: LiveChannel) {
    _uiState.value = _uiState.value.copy(
      activePlayerChannel = channel,
      activePlayerMedia = null
    )
  }

  fun closePlayer() {
    _uiState.value = _uiState.value.copy(
      activePlayerMedia = null,
      activePlayerChannel = null
    )
  }

  fun setSearchQuery(query: String) {
    val trimmed = query.trim()
    val allItems = (_uiState.value.trendingList + _uiState.value.actionList + _uiState.value.dramaList + _uiState.value.animeList).distinctBy { it.id }
    val filtered = if (trimmed.isEmpty()) {
      emptyList()
    } else {
      allItems.filter { item ->
        item.title.contains(trimmed, ignoreCase = true) ||
          item.description.contains(trimmed, ignoreCase = true) ||
          item.genres.any { it.contains(trimmed, ignoreCase = true) } ||
          item.cast.any { it.contains(trimmed, ignoreCase = true) }
      }
    }
    _uiState.value = _uiState.value.copy(
      searchQuery = query,
      searchResults = filtered
    )
  }

  fun filterByGenre(genre: String) {
    setSearchQuery(genre)
  }
}
