package alkaafinternational92.daroodpakcollection.dalailulbarkaat.others

object LanguageUtils {
  
  // Define your language codes and their corresponding names
  val languageMap = mapOf(
    "en" to "English",
    "es" to "Español",
    "fr" to "Français",
    "de" to "Deutsch",
    "it" to "Italiano",
    "pt" to "Português",
    "zh" to "中文",
    "ja" to "日本語",
    "ko" to "한국어",
    "ar" to "العربية",
    "ru" to "Русский",
    "tr" to "Türkçe",
  )
  
  
  // This list can be used in the Spinner adapter or anywhere else
  val languages = languageMap.values.toList()
  
  // Helper function to get language code by name
  fun getLanguageCode(languageName: String): String {
    return languageMap.entries.find { it.value == languageName }?.key ?: "en"
  }
  
  // Helper function to get language code by position using languageMap
  fun getLanguageCodeByPosition(position: Int): String {
    return languageMap.keys.elementAtOrNull(position) ?: "en"
  }
}
