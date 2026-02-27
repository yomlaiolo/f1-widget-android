package com.yomlaiolo.f1widget.data.repository

import android.util.Log
import com.yomlaiolo.f1widget.data.F1ApiService
import com.yomlaiolo.f1widget.data.models.ConstructorStanding
import com.yomlaiolo.f1widget.data.models.DriverStanding
import com.yomlaiolo.f1widget.data.models.Race
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class F1Repository(private val apiService: F1ApiService) {

    data class RaceWithContext(
        val race: Race,
        val totalRaces: Int
    )
    
    suspend fun getUpcomingRace(): RaceWithContext? = withContext(Dispatchers.IO) {
        try {
            val year = Calendar.getInstance().get(Calendar.YEAR)
            val response = apiService.getSeasonCalendar(year)
            
            if (response.isSuccessful) {
                val races = response.body()?.mrData?.raceTable?.races ?: emptyList()
                val now = Date()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                
                // Trouver la prochaine course
                val nextRace = races.firstOrNull { race ->
                    try {
                        val raceDate = dateFormat.parse(race.date)
                        raceDate?.after(now) ?: false
                    } catch (_: Exception) {
                        false
                    }
                }
                
                nextRace?.let { 
                    RaceWithContext(
                        race = it,
                        totalRaces = races.size
                    )
                }
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("F1Repository", "Exception: ${e.message}")
            null
        }
    }
    
    suspend fun getSeasonCalendar(): List<Race> = withContext(Dispatchers.IO) {
        try {
            val year = Calendar.getInstance().get(Calendar.YEAR)
            val response = apiService.getSeasonCalendar(year)
            
            if (response.isSuccessful) {
                response.body()?.mrData?.raceTable?.races ?: emptyList()
            } else {
                Log.e("F1Repository", "Error: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("F1Repository", "Exception: ${e.message}")
            emptyList()
        }
    }
    
    suspend fun getDriverStandings(): List<DriverStanding> = withContext(Dispatchers.IO) {
        try {
            // Essayer la saison courante
            val response = apiService.getDriverStandings()
            if (response.isSuccessful) {
                val standings = response.body()?.mrData?.standingsTable?.standingsLists?.firstOrNull()?.driverStandings
                if (!standings.isNullOrEmpty()) {
                    return@withContext standings
                }
            }
            // Fallback : la saison n'a pas encore de classements, on prend l'année précédente
            Log.d("F1Repository", "No current driver standings, falling back to previous year")
            val previousYear = Calendar.getInstance().get(Calendar.YEAR) - 1
            getDriverStandingsByYear(previousYear)
        } catch (e: Exception) {
            Log.e("F1Repository", "Exception: ${e.message}")
            emptyList()
        }
    }
    
    suspend fun getConstructorStandings(): List<ConstructorStanding> = withContext(Dispatchers.IO) {
        try {
            // Essayer la saison courante
            val response = apiService.getConstructorStandings()
            if (response.isSuccessful) {
                val standings = response.body()?.mrData?.standingsTable?.standingsLists?.firstOrNull()?.constructorStandings
                if (!standings.isNullOrEmpty()) {
                    return@withContext standings
                }
            }
            // Fallback : la saison n'a pas encore de classements, on prend l'année précédente
            Log.d("F1Repository", "No current constructor standings, falling back to previous year")
            val previousYear = Calendar.getInstance().get(Calendar.YEAR) - 1
            getConstructorStandingsByYear(previousYear)
        } catch (e: Exception) {
            Log.e("F1Repository", "Exception: ${e.message}")
            emptyList()
        }
    }

    suspend fun getDriverStandingsByYear(year: Int): List<DriverStanding> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getDriverStandingsByYear(year)
            if (response.isSuccessful) {
                response.body()?.mrData?.standingsTable?.standingsLists?.firstOrNull()?.driverStandings ?: emptyList()
            } else {
                Log.e("F1Repository", "Error getting driver standings for $year: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("F1Repository", "Exception: ${e.message}")
            emptyList()
        }
    }

    suspend fun getConstructorStandingsByYear(year: Int): List<ConstructorStanding> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getConstructorStandingsByYear(year)
            if (response.isSuccessful) {
                response.body()?.mrData?.standingsTable?.standingsLists?.firstOrNull()?.constructorStandings ?: emptyList()
            } else {
                Log.e("F1Repository", "Error getting constructor standings for $year: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("F1Repository", "Exception: ${e.message}")
            emptyList()
        }
    }
}