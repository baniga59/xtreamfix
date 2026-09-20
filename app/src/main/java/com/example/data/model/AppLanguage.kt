package com.example.data.model

enum class AppLanguage(
  val code: String,
  val nativeName: String,
  val englishName: String,
  val flagEmoji: String,
  val isRtl: Boolean = false
) {
  ENGLISH(
    code = "en",
    nativeName = "English",
    englishName = "English",
    flagEmoji = "🇺🇸",
    isRtl = false
  ),
  ARABIC(
    code = "ar",
    nativeName = "العربية",
    englishName = "Arabic",
    flagEmoji = "🇸🇦",
    isRtl = true
  ),
  FRENCH(
    code = "fr",
    nativeName = "Français",
    englishName = "French",
    flagEmoji = "🇫🇷",
    isRtl = false
  ),
  SPANISH(
    code = "es",
    nativeName = "Español",
    englishName = "Spanish",
    flagEmoji = "🇪🇸",
    isRtl = false
  ),
  GERMAN(
    code = "de",
    nativeName = "Deutsch",
    englishName = "German",
    flagEmoji = "🇩🇪",
    isRtl = false
  ),
  JAPANESE(
    code = "ja",
    nativeName = "日本語",
    englishName = "Japanese",
    flagEmoji = "🇯🇵",
    isRtl = false
  )
}
