package com.yomlaiolo.f1widget.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yomlaiolo.f1widget.data.F1ApiService
import com.yomlaiolo.f1widget.data.repository.F1Repository
import com.yomlaiolo.f1widget.utils.CircuitImageManager
import com.yomlaiolo.f1widget.utils.CountryFlags
import com.yomlaiolo.f1widget.utils.DateFormatter

class F1WidgetUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            Log.d("F1WidgetWorker", "Starting widget update...")
            
            val apiService = F1ApiService.create()
            val repository = F1Repository(apiService)
            
            val raceWithContext = repository.getUpcomingRace()
            
            if (raceWithContext != null) {
                val nextRace = raceWithContext.race
                val totalRaces = raceWithContext.totalRaces
                
                Log.d("F1WidgetWorker", "Got race data: ${nextRace.raceName} (${nextRace.round}/$totalRaces)")
                
                // Déterminer la date de début du weekend (première session)
                val firstSessionDate = listOfNotNull(
                    nextRace.firstPractice?.date,
                    nextRace.secondPractice?.date,
                    nextRace.thirdPractice?.date,
                    nextRace.sprint?.date,
                    nextRace.qualifying?.date,
                    nextRace.date
                ).minOrNull() ?: nextRace.date
                
                // Date de fin = date de la course
                val lastSessionDate = nextRace.date
                
                // Formater les dates du weekend
                val weekendDates = DateFormatter.formatWeekendDates(firstSessionDate, lastSessionDate)
                
                // Récupérer le drapeau du pays
                val countryFlag = CountryFlags.getFlag(nextRace.circuit.location.country)
                
                // Télécharger l'image du circuit
                val circuitId = nextRace.circuit.circuitId
                Log.d("F1WidgetWorker", "Downloading circuit image for: $circuitId")
                val circuitBitmap = CircuitImageManager.getCircuitImage(applicationContext, circuitId)
                
                if (circuitBitmap != null) {
                    Log.d("F1WidgetWorker", "Circuit image loaded successfully: $circuitId")
                } else {
                    Log.w("F1WidgetWorker", "Failed to load circuit image: $circuitId")
                }
                
                // Sauvegarder dans SharedPreferences
                val prefs = applicationContext.getSharedPreferences("F1Widget", Context.MODE_PRIVATE)
                prefs.edit().apply {
                    putString("race_name", nextRace.raceName)
                    putString("race_round", nextRace.round)
                    putInt("total_races", totalRaces)
                    putString("circuit_id", nextRace.circuit.circuitId)
                    putString("circuit_name", nextRace.circuit.circuitName)
                    putString("country_flag", countryFlag)
                    putString("weekend_dates", weekendDates)
                    
                    // Sessions
                    nextRace.firstPractice?.let {
                        Log.d("F1WidgetWorker", "FP1: ${it.date} ${it.time}")
                        putString("fp1_date", it.date)
                        putString("fp1_time", it.time)
                    } ?: run {
                        remove("fp1_date")
                        remove("fp1_time")
                    }
                    
                    nextRace.secondPractice?.let {
                        Log.d("F1WidgetWorker", "FP2: ${it.date} ${it.time}")
                        putString("fp2_date", it.date)
                        putString("fp2_time", it.time)
                    } ?: run {
                        remove("fp2_date")
                        remove("fp2_time")
                    }
                    
                    nextRace.thirdPractice?.let {
                        Log.d("F1WidgetWorker", "FP3: ${it.date} ${it.time}")
                        putString("fp3_date", it.date)
                        putString("fp3_time", it.time)
                    } ?: run {
                        remove("fp3_date")
                        remove("fp3_time")
                    }
                    
                    nextRace.sprint?.let {
                        Log.d("F1WidgetWorker", "Sprint: ${it.date} ${it.time}")
                        putString("sprint_date", it.date)
                        putString("sprint_time", it.time)
                    } ?: run {
                        remove("sprint_date")
                        remove("sprint_time")
                    }
                    
                    nextRace.qualifying?.let {
                        Log.d("F1WidgetWorker", "Quali: ${it.date} ${it.time}")
                        putString("quali_date", it.date)
                        putString("quali_time", it.time)
                    } ?: run {
                        remove("quali_date")
                        remove("quali_time")
                    }
                    
                    Log.d("F1WidgetWorker", "Race: ${nextRace.date} ${nextRace.time}")
                    putString("race_date", nextRace.date)
                    putString("race_time", nextRace.time ?: "")
                    
                    putLong("last_update", System.currentTimeMillis())
                    
                    apply()
                }
                
                // Mettre à jour tous les widgets
                val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
                val widgetComponent = ComponentName(applicationContext, F1Widget::class.java)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(widgetComponent)
                
                Log.d("F1WidgetWorker", "Updating ${appWidgetIds.size} widgets")
                
                for (appWidgetId in appWidgetIds) {
                    F1Widget.updateAppWidget(applicationContext, appWidgetManager, appWidgetId)
                }
                
                Log.d("F1WidgetWorker", "Widget updated successfully")
                Result.success()
            } else {
                Log.e("F1WidgetWorker", "No race data found")
                Result.retry()
            }
        } catch (e: Exception) {
            Log.e("F1WidgetWorker", "Error updating widget: ${e.message}", e)
            e.printStackTrace()
            Result.retry()
        }
    }
}