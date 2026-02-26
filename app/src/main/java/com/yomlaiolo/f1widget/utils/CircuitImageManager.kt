package com.yomlaiolo.f1widget.utils

import android.content.Context
import com.yomlaiolo.f1widget.R

/**
 * Gestionnaire des images de circuits F1.
 *
 * Les images sont stockées localement en tant que Vector Drawables (SVG → XML)
 * dans res/drawable/ avec le nommage : circuit_{circuitId}.xml
 *
 * Spécifications des images :
 * - Fond : TRANSPARENT (pas de rectangle de fond)
 * - Tracé : BLANC (#FFFFFF)
 * - Format : Vector Drawable XML
 *
 * Pour ajouter un nouveau circuit :
 * 1. Convertir le SVG en Vector Drawable (Android Studio > New > Vector Asset > Local file)
 * 2. Nommer le fichier circuit_{circuitId}.xml (ex: circuit_monaco.xml)
 * 3. Le placer dans app/src/main/res/drawable/
 * 4. S'assurer que le tracé est en blanc (#FFFFFF) et le fond transparent
 */
object CircuitImageManager {

    // Liste des circuit IDs supportés (correspondant aux IDs de l'API Ergast/Jolpica)
    val SUPPORTED_CIRCUITS = listOf(
        "bahrain",
        "jeddah",
        "albert_park",
        "suzuka",
        "shanghai",
        "miami",
        "monaco",
        "villeneuve",
        "catalunya",
        "red_bull_ring",
        "silverstone",
        "hungaroring",
        "spa",
        "zandvoort",
        "monza",
        "baku",
        "marina_bay",
        "americas",
        "rodriguez",
        "interlagos",
        "vegas",
        "losail",
        "yas_marina",
        "madrid"
    )

    /**
     * Retourne l'ID de la ressource drawable pour un circuit donné.
     * Cherche circuit_{circuitId} dans les drawables, sinon retourne circuit_default.
     */
    fun getCircuitDrawableRes(context: Context, circuitId: String): Int {
        // Chercher la ressource circuit_{circuitId} dans les drawables
        val resourceId = context.resources.getIdentifier(
            "circuit_$circuitId",
            "drawable",
            context.packageName
        )

        // Fallback sur circuit_default si non trouvé
        return if (resourceId != 0) resourceId else R.drawable.circuit_default
    }
}
