package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.localization.Translation
import com.example.data.model.AppLanguage
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.BrandRed
import com.example.ui.theme.StreamDarkCard
import com.example.ui.theme.StreamDarkElevated
import com.example.ui.theme.StreamDarkSurface
import com.example.ui.theme.StreamTextMuted
import com.example.ui.theme.StreamTextPrimary
import com.example.ui.theme.StreamTextSecondary

@Composable
fun LanguageDialog(
  currentLanguage: AppLanguage,
  strings: Translation,
  onLanguageSelected: (AppLanguage) -> Unit,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = StreamDarkSurface),
      border = BorderStroke(1.dp, StreamDarkElevated),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("language_dialog")
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = strings.chooseLanguage,
            color = StreamTextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
          )

          IconButton(onClick = onDismiss) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Close",
              tint = Color.White
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          items(AppLanguage.values()) { lang ->
            val isSelected = currentLanguage == lang
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = if (isSelected) StreamDarkCard else Color.Transparent,
              border = BorderStroke(1.dp, if (isSelected) BrandRed else StreamDarkElevated),
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  onLanguageSelected(lang)
                  onDismiss()
                }
                .testTag("dialog_language_${lang.code}")
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = lang.flagEmoji, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = lang.nativeName,
                    color = StreamTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                  )
                  Text(
                    text = lang.englishName,
                    color = StreamTextMuted,
                    fontSize = 11.sp
                  )
                }
                if (lang.isRtl) {
                  Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = AccentCyan.copy(alpha = 0.15f)
                  ) {
                    Text(
                      text = "RTL",
                      color = AccentCyan,
                      fontSize = 9.sp,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                  }
                  Spacer(modifier = Modifier.width(8.dp))
                }
                if (isSelected) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Active",
                    tint = BrandRed,
                    modifier = Modifier.size(20.dp)
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}
