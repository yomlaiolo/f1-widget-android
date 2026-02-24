package com.yomlaiolo.f1widget.utils

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {
    
    /**
     * Formate la plage de dates du weekend de course
     * Ex: "06 - 08 mars"
     */
    fun formatWeekendDates(firstDate: String, lastDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val first = inputFormat.parse(firstDate)
            val last = inputFormat.parse(lastDate)
            
            if (first != null && last != null) {
                val calendar = Calendar.getInstance()
                
                // Jour de début
                calendar.time = first
                val startDay = calendar.get(Calendar.DAY_OF_MONTH)
                
                // Jour de fin
                calendar.time = last
                val endDay = calendar.get(Calendar.DAY_OF_MONTH)
                
                // Nom du mois en français
                val monthFormat = SimpleDateFormat("MMMM", Locale.FRENCH)
                val month = monthFormat.format(last)
                
                String.format("%02d - %02d %s", startDay, endDay, month)
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }
    
    /**
     * Formate une date et heure pour l'affichage dans le widget
     * Ex: "Ven 15:30"
     */
    fun formatSessionDateTime(date: String, time: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val displayFormat = SimpleDateFormat("EEE HH:mm", Locale.FRENCH).apply {
                timeZone = TimeZone.getDefault()
            }
            
            val dateTimeStr = "${date}T$time"
            val dateTime = inputFormat.parse(dateTimeStr)
            displayFormat.format(dateTime!!)
        } catch (e: Exception) {
            ""
        }
    }
}