package com.yomlaiolo.f1widget.data.repository

import android.util.Log
import com.yomlaiolo.f1widget.data.F1ApiService
import com.yomlaiolo.f1widget.data.models.Race
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class F1Repository(private val apiService: F1ApiService) {
    
    suspend fun getNextRace(): Race? = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getNextRace()
            if (response.isSuccessful) {
                response.body()?.mrData?.raceTable?.races?.firstOrNull()
            } else {
                Log.e("F1Repository", "Error: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("F1Repository", "Exception: ${e.message}")
            null
        }
    }
    
    suspend fun getUpcomingRace(): Race? = withContext(Dispatchers.IO) {
        try {
            val year = Calendar.getInstance().get(Calendar.YEAR)
            val response = apiService.getSeasonCalendar(year)
            
            if (response.isSuccessful) {
                val races = response.body()?.mrData?.raceTable?.races ?: emptyList()
                val now = Date()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                
                // Trouver la prochaine course
                races.firstOrNull { race ->
                    try {
                        val raceDate = dateFormat.parse(race.date)
                        raceDate?.after(now) ?: false
                    } catch (e: Exception) {
                        false
                    }
                }
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("F1Repository", "Exception: ${e.message}")
            null
        }
    }
}