package com.yomlaiolo.f1widget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.work.*
import com.yomlaiolo.f1widget.MainActivity
import com.yomlaiolo.f1widget.R
import com.yomlaiolo.f1widget.utils.CircuitImageManager
import com.yomlaiolo.f1widget.utils.DateFormatter
import java.util.concurrent.TimeUnit

class F1Widget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d("F1Widget", "onUpdate called for ${appWidgetIds.size} widgets")
        
        // Déclencher une mise à jour immédiate
        triggerImmediateUpdate(context)
        
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        
        // Planifier les mises à jour automatiques
        scheduleWidgetUpdate(context)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d("F1Widget", "Widget enabled - triggering immediate update")
        triggerImmediateUpdate(context)
        scheduleWidgetUpdate(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        WorkManager.getInstance(context).cancelUniqueWork(IMMEDIATE_WORK_NAME)
    }

    private fun triggerImmediateUpdate(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val immediateRequest = OneTimeWorkRequestBuilder<F1WidgetUpdateWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            IMMEDIATE_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            immediateRequest
        )
    }

    private fun scheduleWidgetUpdate(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val updateRequest = PeriodicWorkRequestBuilder<F1WidgetUpdateWorker>(
            1, TimeUnit.HOURS  // Mise à jour toutes les heures
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            updateRequest
        )
    }

    companion object {
        private const val WORK_NAME = "F1WidgetUpdate"
        private const val IMMEDIATE_WORK_NAME = "F1WidgetImmediateUpdate"

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.f1_widget)
            
            // Rendre le widget cliquable pour ouvrir l'app
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            // Récupérer les données depuis SharedPreferences
            val prefs = context.getSharedPreferences("F1Widget", Context.MODE_PRIVATE)
            
            val raceName = prefs.getString("race_name", "Loading...") ?: "Loading..."
            val round = prefs.getString("race_round", "") ?: ""
            val totalRaces = prefs.getInt("total_races", 0)
            val circuitId = prefs.getString("circuit_id", "") ?: ""
            val circuitName = prefs.getString("circuit_name", "") ?: ""
            val countryFlag = prefs.getString("country_flag", "🏁") ?: "🏁"
            val weekendDates = prefs.getString("weekend_dates", "") ?: ""
            
            Log.d("F1Widget", "Updating widget with race: $raceName, round: $round/$totalRaces")
            
            // Afficher les informations
            views.setTextViewText(R.id.country_flag, countryFlag)
            views.setTextViewText(R.id.race_name, raceName)
            
            // Format du round: "Round 8/24"
            val roundText = if (totalRaces > 0) {
                "Round $round/$totalRaces"
            } else {
                "Round $round"
            }
            views.setTextViewText(R.id.race_round, roundText)
            
            // Nom du circuit
            views.setTextViewText(R.id.circuit_name, circuitName)
            
            // Dates du weekend
            views.setTextViewText(R.id.weekend_dates, weekendDates)
            
            // Charger l'image du circuit
            val circuitImageRes = getCircuitDrawable(context, circuitId)
            views.setImageViewResource(R.id.circuit_image, circuitImageRes)
            
            // Afficher les sessions
            updateSessionViews(views, prefs)
            
            // Dernière mise à jour
            val lastUpdate = prefs.getLong("last_update", 0)
            val updateText = if (lastUpdate > 0) {
                val diff = System.currentTimeMillis() - lastUpdate
                val minutes = diff / (1000 * 60)
                "Mis à jour il y a ${minutes}min"
            } else {
                "En attente..."
            }
            views.setTextViewText(R.id.last_update, updateText)
            
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun updateSessionViews(
            views: RemoteViews,
            prefs: android.content.SharedPreferences
        ) {
            // FP1
            val fp1Date = prefs.getString("fp1_date", null)
            val fp1Time = prefs.getString("fp1_time", null)
            if (fp1Date != null && fp1Time != null) {
                val formatted = DateFormatter.formatSessionDateTime(fp1Date, fp1Time)
                if (formatted.isNotEmpty()) {
                    views.setTextViewText(R.id.fp1_datetime, formatted)
                    views.setViewVisibility(R.id.fp1_container, android.view.View.VISIBLE)
                    Log.d("F1Widget", "FP1 displayed: $formatted")
                } else {
                    views.setViewVisibility(R.id.fp1_container, android.view.View.GONE)
                }
            } else {
                views.setViewVisibility(R.id.fp1_container, android.view.View.GONE)
            }
            
            // FP2
            val fp2Date = prefs.getString("fp2_date", null)
            val fp2Time = prefs.getString("fp2_time", null)
            if (fp2Date != null && fp2Time != null) {
                val formatted = DateFormatter.formatSessionDateTime(fp2Date, fp2Time)
                if (formatted.isNotEmpty()) {
                    views.setTextViewText(R.id.fp2_datetime, formatted)
                    views.setViewVisibility(R.id.fp2_container, android.view.View.VISIBLE)
                    Log.d("F1Widget", "FP2 displayed: $formatted")
                } else {
                    views.setViewVisibility(R.id.fp2_container, android.view.View.GONE)
                }
            } else {
                views.setViewVisibility(R.id.fp2_container, android.view.View.GONE)
            }
            
            // FP3
            val fp3Date = prefs.getString("fp3_date", null)
            val fp3Time = prefs.getString("fp3_time", null)
            if (fp3Date != null && fp3Time != null) {
                val formatted = DateFormatter.formatSessionDateTime(fp3Date, fp3Time)
                if (formatted.isNotEmpty()) {
                    views.setTextViewText(R.id.fp3_datetime, formatted)
                    views.setViewVisibility(R.id.fp3_container, android.view.View.VISIBLE)
                    Log.d("F1Widget", "FP3 displayed: $formatted")
                } else {
                    views.setViewVisibility(R.id.fp3_container, android.view.View.GONE)
                }
            } else {
                views.setViewVisibility(R.id.fp3_container, android.view.View.GONE)
            }
            
            // Sprint
            val sprintDate = prefs.getString("sprint_date", null)
            val sprintTime = prefs.getString("sprint_time", null)
            if (sprintDate != null && sprintTime != null) {
                val formatted = DateFormatter.formatSessionDateTime(sprintDate, sprintTime)
                if (formatted.isNotEmpty()) {
                    views.setTextViewText(R.id.sprint_datetime, formatted)
                    views.setViewVisibility(R.id.sprint_container, android.view.View.VISIBLE)
                    Log.d("F1Widget", "Sprint displayed: $formatted")
                } else {
                    views.setViewVisibility(R.id.sprint_container, android.view.View.GONE)
                }
            } else {
                views.setViewVisibility(R.id.sprint_container, android.view.View.GONE)
            }
            
            // Qualifying
            val qualiDate = prefs.getString("quali_date", null)
            val qualiTime = prefs.getString("quali_time", null)
            if (qualiDate != null && qualiTime != null) {
                val formatted = DateFormatter.formatSessionDateTime(qualiDate, qualiTime)
                if (formatted.isNotEmpty()) {
                    views.setTextViewText(R.id.quali_datetime, formatted)
                    views.setViewVisibility(R.id.quali_container, android.view.View.VISIBLE)
                    Log.d("F1Widget", "Quali displayed: $formatted")
                } else {
                    views.setViewVisibility(R.id.quali_container, android.view.View.GONE)
                }
            } else {
                views.setViewVisibility(R.id.quali_container, android.view.View.GONE)
            }
            
            // Race
            val raceDate = prefs.getString("race_date", null)
            val raceTime = prefs.getString("race_time", null)
            if (raceDate != null && raceTime != null) {
                val formatted = DateFormatter.formatSessionDateTime(raceDate, raceTime)
                if (formatted.isNotEmpty()) {
                    views.setTextViewText(R.id.race_datetime, formatted)
                    views.setViewVisibility(R.id.race_container, android.view.View.VISIBLE)
                    Log.d("F1Widget", "Race displayed: $formatted")
                } else {
                    views.setViewVisibility(R.id.race_container, android.view.View.GONE)
                }
            } else {
                views.setViewVisibility(R.id.race_container, android.view.View.GONE)
            }
        }

        private fun getCircuitDrawable(context: Context, circuitId: String): Int {
            return CircuitImageManager.getCircuitDrawableRes(context, circuitId)
        }
    }
}
