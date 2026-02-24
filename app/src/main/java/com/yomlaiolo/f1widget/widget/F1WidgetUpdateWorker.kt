package com.yomlaiolo.f1widget.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yomlaiolo.f1widget.data.F1ApiService
import com.yomlaiolo.f1widget.data.repository.F1Repository

class F1WidgetUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val apiService = F1ApiService.create()
            val repository = F1Repository(apiService)
            
            val nextRace = repository.getUpcomingRace()
            
            if (nextRace != null) {
                // Sauvegarder dans SharedPreferences
                val prefs = applicationContext.getSharedPreferences("F1Widget", Context.MODE_PRIVATE)
                prefs.edit().apply {
                    putString("race_name", nextRace.raceName)
                    putString("race_round", nextRace.round)
                    putString("circuit_id", nextRace.circuit.circuitId)
                    
                    // Sessions
                    nextRace.firstPractice?.let {
                        putString("fp1_date", it.date)
                        putString("fp1_time", it.time)
                    } ?: run {
                        remove("fp1_date")
                        remove("fp1_time")
                    }
                    
                    nextRace.secondPractice?.let {
                        putString("fp2_date", it.date)
                        putString("fp2_time", it.time)
                    } ?: run {
                        remove("fp2_date")
                        remove("fp2_time")
                    }
                    
                    nextRace.thirdPractice?.let {
                        putString("fp3_date", it.date)
                        putString("fp3_time", it.time)
                    } ?: run {
                        remove("fp3_date")
                        remove("fp3_time")
                    }
                    
                    nextRace.sprint?.let {
                        putString("sprint_date", it.date)
                        putString("sprint_time", it.time)
                    } ?: run {
                        remove("sprint_date")
                        remove("sprint_time")
                    }
                    
                    nextRace.qualifying?.let {
                        putString("quali_date", it.date)
                        putString("quali_time", it.time)
                    } ?: run {
                        remove("quali_date")
                        remove("quali_time")
                    }
                    
                    putString("race_date", nextRace.date)
                    putString("race_time", nextRace.time ?: "")
                    
                    putLong("last_update", System.currentTimeMillis())
                    
                    apply()
                }
                
                // Mettre à jour tous les widgets
                val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
                val widgetComponent = ComponentName(applicationContext, F1Widget::class.java)
                val appWidgetIds = appWidgetManager.getAppWidgetIds(widgetComponent)
                
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
            Log.e("F1WidgetWorker", "Error updating widget: ${e.message}")
            Result.retry()
        }
    }
}