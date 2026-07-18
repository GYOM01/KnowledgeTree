package com.knowledgetree.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Colors = lightColorScheme(
    primary = Color(0xFF7256B7),
    primaryContainer = Color(0xFFEBDDFF),
    secondary = Color(0xFF67507A),
    background = Color(0xFFF8F5FF),
    surface = Color.White
)

@Composable
fun KnowledgeTreeTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Colors, content = content)
}
