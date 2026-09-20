package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.Translation
import com.example.ui.theme.BrandRed
import com.example.ui.theme.StreamDarkBackground
import com.example.ui.theme.StreamDarkElevated
import com.example.ui.theme.StreamTextMuted
import com.example.ui.theme.StreamTextPrimary
import com.example.ui.viewmodel.MainTab

data class NavItem(
  val tab: MainTab,
  val title: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector
)

@Composable
fun StreamBottomNavigationBar(
  activeTab: MainTab,
  strings: Translation,
  onTabSelected: (MainTab) -> Unit
) {
  val items = listOf(
    NavItem(MainTab.HOME, strings.home, Icons.Filled.Home, Icons.Outlined.Home),
    NavItem(MainTab.LIVE_TV, strings.liveTv, Icons.Filled.LiveTv, Icons.Outlined.LiveTv),
    NavItem(MainTab.SEARCH, strings.search, Icons.Filled.Search, Icons.Outlined.Search),
    NavItem(MainTab.MY_LIST, strings.myList, Icons.Filled.Bookmark, Icons.Outlined.BookmarkBorder),
    NavItem(MainTab.SETTINGS, strings.settings, Icons.Filled.Settings, Icons.Outlined.Settings)
  )

  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .windowInsetsPadding(WindowInsets.navigationBars),
    color = StreamDarkBackground,
    tonalElevation = 8.dp
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.verticalGradient(
            colors = listOf(
              Color(0xCC111118),
              StreamDarkBackground
            )
          )
        )
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(64.dp)
          .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
      ) {
        items.forEach { item ->
          val isSelected = activeTab == item.tab
          val tintColor by animateColorAsState(
            targetValue = if (isSelected) BrandRed else StreamTextMuted,
            label = "nav_tint"
          )

          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(12.dp))
              .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
              ) { onTabSelected(item.tab) }
              .padding(vertical = 6.dp)
              .testTag("nav_tab_${item.tab.name.lowercase()}")
          ) {
            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (isSelected) BrandRed.copy(alpha = 0.15f) else Color.Transparent)
            ) {
              Icon(
                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                contentDescription = item.title,
                tint = tintColor,
                modifier = Modifier.size(24.dp)
              )
            }

            Text(
              text = item.title,
              color = tintColor,
              fontSize = 11.sp,
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              maxLines = 1
            )
          }
        }
      }
    }
  }
}
