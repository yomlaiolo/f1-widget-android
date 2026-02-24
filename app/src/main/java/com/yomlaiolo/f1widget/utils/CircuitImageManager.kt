package com.yomlaiolo.f1widget.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

object CircuitImageManager {
    
    private const val TAG = "CircuitImageManager"
    
    // Map des circuits avec leurs URLs Formula1.com
    private val CIRCUIT_IMAGE_URLS = mapOf(
        "bahrain" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026tracksakhirdetailed.webp",
        "jeddah" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackjeddahdetailed.webp",
        "albert_park" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackalbertparkdetailed.webp",
        "suzuka" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026tracksuzukadetailed.webp",
        "shanghai" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackshanghaidetailed.webp",
        "miami" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackmiamidetailed.webp",
        "imola" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackimoladetailed.webp",
        "monaco" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackmonacodetailed.webp",
        "villeneuve" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackvilleneuvedetailed.webp",
        "catalunya" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackcatalunyadetailed.webp",
        "red_bull_ring" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackredbullringdetailed.webp",
        "silverstone" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026tracksilverstonedetailed.webp",
        "hungaroring" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackhungaroringdetailed.webp",
        "spa" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackspadetailed.webp",
        "zandvoort" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackzandvoortdetailed.webp",
        "monza" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackmonzadetailed.webp",
        "baku" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackbakudetailed.webp",
        "marina_bay" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackmarinabaydetailed.webp",
        "americas" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackamericasdetailed.webp",
        "rodriguez" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackrodriguezdetailed.webp",
        "interlagos" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackinterlagosdetailed.webp",
        "vegas" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackvegasdetailed.webp",
        "losail" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026tracklosaildetailed.webp",
        "yas_marina" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackyasmarindetailed.webp",
        "madrid" to "https://media.formula1.com/image/upload/c_fit,h_704,q_auto/v1740000000/common/f1/2026/track/2026trackmadriddetailed.webp"
    )
    
    /**
     * Récupère l'image d'un circuit (depuis le cache ou télécharge)
     */
    suspend fun getCircuitImage(context: Context, circuitId: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                // Vérifier si l'image existe déjà en cache
                val cachedBitmap = getCachedImage(context, circuitId)
                if (cachedBitmap != null) {
                    Log.d(TAG, "Circuit image loaded from cache: $circuitId")
                    return@withContext cachedBitmap
                }
                
                // Télécharger l'image
                val url = CIRCUIT_IMAGE_URLS[circuitId]
                if (url != null) {
                    Log.d(TAG, "Downloading circuit image: $circuitId from $url")
                    val bitmap = downloadImage(url)
                    
                    if (bitmap != null) {
                        // Mettre en cache
                        cacheImage(context, circuitId, bitmap)
                        Log.d(TAG, "Circuit image downloaded and cached: $circuitId")
                        return@withContext bitmap
                    }
                } else {
                    Log.w(TAG, "No URL found for circuit: $circuitId")
                }
                
                null
            } catch (e: Exception) {
                Log.e(TAG, "Error loading circuit image: $circuitId", e)
                null
            }
        }
    }
    
    /**
     * Télécharge une image depuis une URL
     */
    private fun downloadImage(url: String): Bitmap? {
        return try {
            val connection = URL(url).openConnection()
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.connect()
            
            val inputStream = connection.getInputStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            
            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading image from $url", e)
            null
        }
    }
    
    /**
     * Récupère une image depuis le cache
     */
    private fun getCachedImage(context: Context, circuitId: String): Bitmap? {
        val cacheFile = getCacheFile(context, circuitId)
        
        return if (cacheFile.exists()) {
            try {
                BitmapFactory.decodeFile(cacheFile.absolutePath)
            } catch (e: Exception) {
                Log.e(TAG, "Error reading cached image: $circuitId", e)
                null
            }
        } else {
            null
        }
    }
    
    /**
     * Met en cache une image
     */
    private fun cacheImage(context: Context, circuitId: String, bitmap: Bitmap) {
        try {
            val cacheFile = getCacheFile(context, circuitId)
            
            // Créer le dossier si nécessaire
            cacheFile.parentFile?.mkdirs()
            
            // Sauvegarder l'image
            FileOutputStream(cacheFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error caching image: $circuitId", e)
        }
    }
    
    /**
     * Récupère le fichier de cache pour un circuit
     */
    private fun getCacheFile(context: Context, circuitId: String): File {
        val cacheDir = File(context.cacheDir, "circuit_images")
        return File(cacheDir, "$circuitId.png")
    }
    
    /**
     * Efface le cache des images
     */
    fun clearCache(context: Context) {
        try {
            val cacheDir = File(context.cacheDir, "circuit_images")
            cacheDir.deleteRecursively()
            Log.d(TAG, "Circuit image cache cleared")
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing cache", e)
        }
    }
}