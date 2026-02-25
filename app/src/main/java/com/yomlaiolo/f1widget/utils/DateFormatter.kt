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
    
    /**
     * Formate une date de session
     * Ex: "Ven 08 mars"
     */
    fun formatSessionDate(date: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val displayFormat = SimpleDateFormat("EEE dd MMM", Locale.FRENCH)
            
            val parsedDate = inputFormat.parse(date)
            displayFormat.format(parsedDate!!)
        } catch (e: Exception) {
            date
        }
    }
    
    /**
     * Formate une heure
     * Ex: "15:30"
     */
    fun formatTime(time: String): String {
        return try {
            if (time.contains("Z")) {
                // Format UTC complet
                val inputFormat = SimpleDateFormat("HH:mm:ss'Z'", Locale.getDefault()).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
                val displayFormat = SimpleDateFormat("HH:mm", Locale.getDefault()).apply {
                    timeZone = TimeZone.getDefault()
                }
                val parsedTime = inputFormat.parse(time)
                displayFormat.format(parsedTime!!)
            } else {
                // Déjà au bon format
                time.substring(0, 5)
            }
        } catch (e: Exception) {
            time
        }
    }
    
    /**
     * Formate une date de course complète
     * Ex: "08 mars 2024"
     */
    fun formatRaceDate(date: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val displayFormat = SimpleDateFormat("dd MMMM yyyy", Locale.FRENCH)
            
            val parsedDate = inputFormat.parse(date)
            displayFormat.format(parsedDate!!)
        } catch (e: Exception) {
            date
        }
    }
}