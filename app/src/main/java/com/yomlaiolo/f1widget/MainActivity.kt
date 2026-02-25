package com.yomlaiolo.f1widget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.yomlaiolo.f1widget.ui.MainViewModel
import com.yomlaiolo.f1widget.ui.navigation.F1App

class MainActivity : ComponentActivity() {
    
    private val viewModel: MainViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            F1WidgetTheme {
                F1App(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun F1WidgetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = androidx.compose.ui.graphics.Color(0xFFE10600),
            secondary = androidx.compose.ui.graphics.Color(0xFF1E41FF),
            tertiary = androidx.compose.ui.graphics.Color(0xFF00D2BE)
        )
    } else {
        lightColorScheme(
            primary = androidx.compose.ui.graphics.Color(0xFFE10600),
            secondary = androidx.compose.ui.graphics.Color(0xFF1E41FF),
            tertiary = androidx.compose.ui.graphics.Color(0xFF00D2BE)
        )
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}