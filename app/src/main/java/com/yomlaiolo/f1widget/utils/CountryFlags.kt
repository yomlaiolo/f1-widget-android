package com.yomlaiolo.f1widget.utils

object CountryFlags {
    private val countryToFlag = mapOf(
        "Australia" to "🇦🇺",
        "Austria" to "🇦🇹",
        "Azerbaijan" to "🇦🇿",
        "Bahrain" to "🇧🇭",
        "Belgium" to "🇧🇪",
        "Brazil" to "🇧🇷",
        "Canada" to "🇨🇦",
        "China" to "🇨🇳",
        "Netherlands" to "🇳🇱",
        "France" to "🇫🇷",
        "Germany" to "🇩🇪",
        "Hungary" to "🇭🇺",
        "Italy" to "🇮🇹",
        "Japan" to "🇯🇵",
        "Mexico" to "🇲🇽",
        "Monaco" to "🇲🇨",
        "Portugal" to "🇵🇹",
        "Qatar" to "🇶🇦",
        "Russia" to "🇷🇺",
        "Saudi Arabia" to "🇸🇦",
        "Singapore" to "🇸🇬",
        "Spain" to "🇪🇸",
        "UAE" to "🇦🇪",
        "UK" to "🇬🇧",
        "USA" to "🇺🇸",
        "United States" to "🇺🇸",
        "Great Britain" to "🇬🇧",
        "Turkey" to "🇹🇷",
        "Korea" to "🇰🇷",
        "India" to "🇮🇳",
        "Malaysia" to "🇲🇾"
    )
    
    fun getFlag(country: String): String {
        return countryToFlag[country] ?: "🏁" // Drapeau à damier par défaut
    }
}