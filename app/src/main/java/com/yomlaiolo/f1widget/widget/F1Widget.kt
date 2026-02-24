package com.yomlaiolo.f1widget.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import androidx.work.*
import com.yomlaiolo.f1widget.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class F1Widget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
        
        // Planifier les mises à jour automatiques
        scheduleWidgetUpdate(context)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        scheduleWidgetUpdate(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
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

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.f1_widget)
            
            // Récupérer les données depuis SharedPreferences
            val prefs = context.getSharedPreferences("F1Widget", Context.MODE_PRIVATE)
            
            val raceName = prefs.getString("race_name", "Loading...") ?: "Loading..."
            val round = prefs.getString("race_round", "") ?: ""
            val circuitId = prefs.getString("circuit_id", "") ?: ""
            
            views.setTextViewText(R.id.race_name, raceName)
            views.setTextViewText(R.id.race_round, "Round $round")
            
            // Charger l'image du circuit
            val circuitImageRes = getCircuitDrawable(context, circuitId)
            views.setImageViewResource(R.id.circuit_image, circuitImageRes)
            
            // Afficher les sessions
            updateSessionViews(views, prefs, context)
            
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
            prefs: android.content.SharedPreferences,
            context: Context
        ) {
            // L'API retourne les dates au format "2024-03-15" et les heures au format "14:30:00Z"
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val displayFormat = SimpleDateFormat("EEE HH:mm", Locale.FRENCH).apply {
                timeZone = TimeZone.getDefault()
            }
            
            // FP1
            val fp1Date = prefs.getString("fp1_date", null)
            val fp1Time = prefs.getString("fp1_time", null)
            if (fp1Date != null && fp1Time != null) {
                try {
                    val dateTimeStr = "${fp1Date}T$fp1Time"
                    val dateTime = inputFormat.parse(dateTimeStr)
                    val formatted = displayFormat.format(dateTime!!)
                    views.setTextViewText(R.id.fp1_datetime, formatted)
                    views.setViewVisibility(R.id.fp1_container, android.view.View.VISIBLE)
                } catch (e: Exception) {
                    views.setViewVisibility(R.id.fp1_container, android.view.View.GONE)
                }
            } else {
                views.setViewVisibility(R.id.fp1_container, android.view.View.GONE)
            }
            
            // FP2
            val fp2Date = prefs.getString("fp2_date", null)
            val fp2Time = prefs.getString("fp2_time", null)
            if (fp2Date != null && fp2Time != null) {
                try {
                    val dateTimeStr = "${fp2Date}T$fp2Time"
                    val dateTime = inputFormat.parse(dateTimeStr)
                    val formatted = displayFormat.format(dateTime!!)
                    views.setTextViewText(R.id.fp2_datetime, formatted)
                    views.setViewVisibility(R.id.fp2_container, android.view.View.VISIBLE)
                } catch (e: Exception) {
                    views.setViewVisibility(R.id.fp2_container, android.view.View.GONE)
                }
            } else {
                views.setViewVisibility(R.id.fp2_container, android.view.View.GONE)
            }
            
            // FP3
            val fp3Date = prefs.getString("fp3_date", null)
            val fp3Time = prefs.getString("fp3_time", null)
            if (fp3Date != null && fp3Time != null) {
                try {
                    val dateTimeStr = "${fp3Date}T$fp3Time"
                    val dateTime = inputFormat.parse(dateTimeStr)
                    val formatted = displayFormat.format(dateTime!!)
                    views.setTextViewText(R.id.fp3_datetime, formatted)
                    views.setViewVisibility(R.id.fp3_container, android.view.View.VISIBLE)
                } catch (e: Exception) {
                    views.setViewVisibility(R.id.fp3_container, android.view.View.GONE)
                }
            } else {
                views.setViewVisibility(R.id.fp3_container, android.view.View.GONE)
            }
            
            // Sprint
            val sprintDate = prefs.getString("sprint_date", null)
            val sprintTime = prefs.getString("sprint_time", null)
            if (sprintDate != null && sprintTime != null) {
                try {
                    val dateTimeStr = "${sprintDate}T$sprintTime"
                    val dateTime = inputFormat.parse(dateTimeStr)
                    val formatted = displayFormat.format(dateTime!!)
                    views.setTextViewText(R.id.sprint_datetime, formatted)
                    views.setViewVisibility(R.id.sprint_container, android.view.View.VISIBLE)
                } catch (e: Exception) {
                    views.setViewVisibility(R.id.sprint_container, android.view.View.GONE)
                }
            } else {
                views.setViewVisibility(R.id.sprint_container, android.view.View.GONE)
            }
            
            // Qualifying
            val qualiDate = prefs.getString("quali_date", null)
            val qualiTime = prefs.getString("quali_time", null)
            if (qualiDate != null && qualiTime != null) {
                try {
                    val dateTimeStr = "${qualiDate}T$qualiTime"
                    val dateTime = inputFormat.parse(dateTimeStr)
                    val formatted = displayFormat.format(dateTime!!)
                    views.setTextViewText(R.id.quali_datetime, formatted)
                    views.setViewVisibility(R.id.quali_container, android.view.View.VISIBLE)
                } catch (e: Exception) {
                    views.setViewVisibility(R.id.quali_container, android.view.View.GONE)
                }
            } else {
                views.setViewVisibility(R.id.quali_container, android.view.View.GONE)
            }
            
            // Race
            val raceDate = prefs.getString("race_date", null)
            val raceTime = prefs.getString("race_time", null)
            if (raceDate != null && raceTime != null) {
                try {
                    val dateTimeStr = "${raceDate}T$raceTime"
                    val dateTime = inputFormat.parse(dateTimeStr)
                    val formatted = displayFormat.format(dateTime!!)
                    views.setTextViewText(R.id.race_datetime, formatted)
                    views.setViewVisibility(R.id.race_container, android.view.View.VISIBLE)
                } catch (e: Exception) {
                    views.setViewVisibility(R.id.race_container, android.view.View.GONE)
                }
            } else {
                views.setViewVisibility(R.id.race_container, android.view.View.GONE)
            }
        }

        private fun getCircuitDrawable(context: Context, circuitId: String): Int {
            // Mapper les IDs de circuits aux ressources drawable
            // D'abord essayer avec l'ID exact du circuit
            var resourceId = context.resources.getIdentifier(
                "circuit_$circuitId",
                "drawable",
                context.packageName
            )
            
            // Si pas trouvé, essayer avec l'image par défaut
            if (resourceId == 0) {
                resourceId = context.resources.getIdentifier(
                    "circuit_default",
                    "drawable",
                    context.packageName
                )
            }
            
            // Si toujours pas trouvé (ne devrait jamais arriver), utiliser l'icône de l'app
            if (resourceId == 0) {
                resourceId = R.mipmap.ic_launcher
            }
            
            return resourceId
        }
    }
}