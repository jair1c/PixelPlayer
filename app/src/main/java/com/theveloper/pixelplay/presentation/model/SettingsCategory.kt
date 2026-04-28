package com.theveloper.pixelplay.presentation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeveloperBoard
import androidx.compose.material.icons.rounded.DeveloperMode
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.ui.graphics.vector.ImageVector
import com.theveloper.pixelplay.R

enum class SettingsCategory(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector? = null,
    val iconRes: Int? = null
) {
    LIBRARY(
        id = "library",
        title = "Gestión de música",
        subtitle = "Gestionar carpetas, actualizar biblioteca, opciones de análisis",
        icon = Icons.Rounded.LibraryMusic
    ),
    APPEARANCE(
        id = "appearance",
        title = "Apariencia",
        subtitle = "Temas, diseño y estilos visuales",
        icon = Icons.Rounded.Palette
    ),
    PLAYBACK(
        id = "playback",
        title = "Reproducción",
        subtitle = "Comportamiento de audio, crossfade y reproducción en segundo plano",
        icon = Icons.Rounded.MusicNote // Using MusicNote again or maybe PlayCircle if available
    ),
    BEHAVIOR(
        id = "behavior",
        title = "Comportamiento",
        subtitle = "Gestos, háptica y comportamiento de navegación",
        iconRes = R.drawable.rounded_touch_app_24
    ),
    AI_INTEGRATION(
        id = "ai",
        title = "Integración con IA (β)",
        subtitle = "Proveedores de IA, claves API y configuración de modelos",
        iconRes = R.drawable.gemini_ai
    ),
    BACKUP_RESTORE(
        id = "backup_restore",
        title = "Copia de seguridad y restauración",
        subtitle = "Exportar y recuperar tus datos personales",
        iconRes = R.drawable.rounded_upload_file_24
    ),
    DEVELOPER(
        id = "developer",
        title = "Opciones de desarrollador",
        subtitle = "Funciones experimentales y depuración",
        icon = Icons.Rounded.DeveloperMode
    ),
    EQUALIZER(
        id = "equalizer",
        title = "Ecualizador",
        subtitle = "Ajustar frecuencias de audio y presets",
        icon = Icons.Rounded.GraphicEq
    ),
    DEVICE_CAPABILITIES(
        id = "device_capabilities",
        title = "Capacidades del dispositivo",
        subtitle = "Especificaciones de audio, códecs e info del decodificador",
        icon = Icons.Rounded.DeveloperBoard // Placeholder, maybe Memory or SettingsInputComponent
    ),
    ABOUT(
        id = "about",
        title = "Acerca de",
        subtitle = "Información de la app, versión y créditos",
        icon = Icons.Rounded.Info
    );

    companion object {
        fun fromId(id: String): SettingsCategory? = entries.find { it.id == id }
    }
}
