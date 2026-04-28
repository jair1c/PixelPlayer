package com.theveloper.pixelplay.presentation.screens

import com.theveloper.pixelplay.presentation.navigation.navigateSafely
import com.theveloper.pixelplay.presentation.components.BackupModuleSelectionDialog
import com.theveloper.pixelplay.data.preferences.AiPreferencesRepository
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.draw.rotate

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.provider.Settings
import android.text.format.Formatter
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Restore
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import kotlin.math.roundToInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.theveloper.pixelplay.R
import com.theveloper.pixelplay.data.backup.model.BackupHistoryEntry
import com.theveloper.pixelplay.data.backup.model.BackupOperationType
import com.theveloper.pixelplay.data.backup.model.BackupSection
import com.theveloper.pixelplay.data.backup.model.BackupTransferProgressUpdate
import com.theveloper.pixelplay.data.backup.model.ModuleRestoreDetail
import com.theveloper.pixelplay.data.backup.model.RestorePlan
import com.theveloper.pixelplay.data.preferences.AppThemeMode
import com.theveloper.pixelplay.data.preferences.CollagePattern
import com.theveloper.pixelplay.data.preferences.CarouselStyle
import com.theveloper.pixelplay.data.preferences.LaunchTab
import com.theveloper.pixelplay.data.preferences.LibraryNavigationMode
import com.theveloper.pixelplay.data.preferences.NavBarStyle
import com.theveloper.pixelplay.data.preferences.ThemePreference
import com.theveloper.pixelplay.data.model.Song
import com.theveloper.pixelplay.data.model.LyricsSourcePreference
import com.theveloper.pixelplay.presentation.components.CollapsibleCommonTopBar
import com.theveloper.pixelplay.presentation.components.ExpressiveTopBarContent
import com.theveloper.pixelplay.presentation.components.FileExplorerDialog
import com.theveloper.pixelplay.presentation.components.MiniPlayerHeight
import com.theveloper.pixelplay.presentation.model.SettingsCategory
import com.theveloper.pixelplay.presentation.navigation.Screen
import com.theveloper.pixelplay.presentation.viewmodel.LyricsRefreshProgress
import com.theveloper.pixelplay.presentation.viewmodel.PlayerViewModel
import com.theveloper.pixelplay.presentation.viewmodel.SettingsViewModel
import com.theveloper.pixelplay.ui.theme.GoogleSansRounded
import androidx.compose.ui.res.stringResource

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsCategoryScreen(
    categoryId: String,
    navController: NavController,
    playerViewModel: PlayerViewModel,
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    statsViewModel: com.theveloper.pixelplay.presentation.viewmodel.StatsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val category = SettingsCategory.fromId(categoryId) ?: return
    val context = LocalContext.current
    
    // State Collection (Duplicated from SettingsScreen for now to ensure functionality)
    val uiState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    val currentAiApiKey by settingsViewModel.currentAiApiKey.collectAsStateWithLifecycle()
    val currentAiModel by settingsViewModel.currentAiModel.collectAsStateWithLifecycle()
    val currentAiSystemPrompt by settingsViewModel.currentAiSystemPrompt.collectAsStateWithLifecycle()
    val aiProvider by settingsViewModel.aiProvider.collectAsStateWithLifecycle()
    val currentPath by settingsViewModel.currentPath.collectAsStateWithLifecycle()
    val directoryChildren by settingsViewModel.currentDirectoryChildren.collectAsStateWithLifecycle()
    val availableStorages by settingsViewModel.availableStorages.collectAsStateWithLifecycle()
    val selectedStorageIndex by settingsViewModel.selectedStorageIndex.collectAsStateWithLifecycle()
    val isLoadingDirectories by settingsViewModel.isLoadingDirectories.collectAsStateWithLifecycle()
    val isExplorerPriming by settingsViewModel.isExplorerPriming.collectAsStateWithLifecycle()
    val isExplorerReady by settingsViewModel.isExplorerReady.collectAsStateWithLifecycle()
    val isCurrentDirectoryResolved by settingsViewModel.isCurrentDirectoryResolved.collectAsStateWithLifecycle()
    val isSyncing by settingsViewModel.isSyncing.collectAsStateWithLifecycle()
    val syncProgress by settingsViewModel.syncProgress.collectAsStateWithLifecycle()
    val dataTransferProgress by settingsViewModel.dataTransferProgress.collectAsStateWithLifecycle()
    val paletteRegenerateTargets by playerViewModel.paletteRegenerationTargets.collectAsStateWithLifecycle()
    val explorerRoot = settingsViewModel.explorerRoot()

    // Local State
    var showExplorerSheet by remember { mutableStateOf(false) }
    var refreshRequested by remember { mutableStateOf(false) }
    var syncRequestObservedRunning by remember { mutableStateOf(false) }
    var syncIndicatorLabel by remember { mutableStateOf<String?>(null) }
    var showClearLyricsDialog by remember { mutableStateOf(false) }
    var showRebuildDatabaseWarning by remember { mutableStateOf(false) }
    var showRegenerateDailyMixDialog by remember { mutableStateOf(false) }
    var showRegenerateStatsDialog by remember { mutableStateOf(false) }
    var showRegenerateAllPalettesDialog by remember { mutableStateOf(false) }
    var showExportDataDialog by remember { mutableStateOf(false) }
    var showImportFlow by remember { mutableStateOf(false) }
    var exportSections by remember { mutableStateOf(BackupSection.defaultSelection) }
    var importFileUri by remember { mutableStateOf<Uri?>(null) }
    var minSongDurationDraft by remember(uiState.minSongDuration) {
        mutableStateOf(uiState.minSongDuration.toFloat())
    }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri ->
        if (uri != null) {
            settingsViewModel.exportAppData(uri, exportSections)
        }
    }

    val importFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            importFileUri = uri
            settingsViewModel.inspectBackupFile(uri)
        }
    }

    LaunchedEffect(Unit) {
        settingsViewModel.dataTransferEvents.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(isSyncing, refreshRequested) {
        if (!refreshRequested) return@LaunchedEffect

        if (isSyncing) {
            syncRequestObservedRunning = true
        } else if (syncRequestObservedRunning) {
            Toast.makeText(context, "Library sync finished", Toast.LENGTH_SHORT).show()
            refreshRequested = false
            syncRequestObservedRunning = false
            syncIndicatorLabel = null
        }
    }

    var showPaletteRegenerateSheet by remember { mutableStateOf(false) }
    var isPaletteRegenerateRunning by remember { mutableStateOf(false) }
    var isPaletteBulkRegenerateRunning by remember { mutableStateOf(false) }
    var paletteBulkCompletedCount by remember { mutableStateOf(0) }
    var paletteBulkTotalCount by remember { mutableStateOf(0) }
    var paletteSongSearchQuery by remember { mutableStateOf("") }
    val paletteRegenerateSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isAnyPaletteRegenerateRunning = isPaletteRegenerateRunning || isPaletteBulkRegenerateRunning
    val filteredPaletteSongs = remember(paletteRegenerateTargets, paletteSongSearchQuery) {
        val query = paletteSongSearchQuery.trim()
        if (query.isBlank()) {
            paletteRegenerateTargets
        } else {
            paletteRegenerateTargets.filter { song ->
                song.title.contains(query, ignoreCase = true) ||
                    song.displayArtist.contains(query, ignoreCase = true) ||
                    song.album.contains(query, ignoreCase = true)
            }
        }
    }

    // Auto-load models when entering the AI settings page or when provider/key changes
    LaunchedEffect(category, aiProvider) {
        if (category == SettingsCategory.AI_INTEGRATION) {
            // Small delay to let StateFlows settle after navigation
            kotlinx.coroutines.delay(200)
            settingsViewModel.loadModelsForCurrentProvider()
        }
    }

    // TopBar Animations (identical to SettingsScreen)
    // TopBar Animations (identical to SettingsScreen)
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    
    val isLongTitle = category.title.length > 13
    
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val minTopBarHeight = 64.dp + statusBarHeight
    val maxTopBarHeight = if (isLongTitle) 200.dp else 180.dp //for 2 lines use 220 and make text use \n

    val minTopBarHeightPx = with(density) { minTopBarHeight.toPx() }
    val maxTopBarHeightPx = with(density) { maxTopBarHeight.toPx() }
    
    val titleMaxLines = if (isLongTitle) 2 else 1

    val topBarHeight = remember(maxTopBarHeightPx) { Animatable(maxTopBarHeightPx) }
    var collapseFraction by remember { mutableStateOf(0f) }

    LaunchedEffect(topBarHeight.value, maxTopBarHeightPx) {
        collapseFraction =
                1f -
                        ((topBarHeight.value - minTopBarHeightPx) /
                                        (maxTopBarHeightPx - minTopBarHeightPx))
                                .coerceIn(0f, 1f)
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val isScrollingDown = delta < 0

                if (!isScrollingDown &&
                                (lazyListState.firstVisibleItemIndex > 0 ||
                                        lazyListState.firstVisibleItemScrollOffset > 0)
                ) {
                    return Offset.Zero
                }

                val previousHeight = topBarHeight.value
                val newHeight =
                        (previousHeight + delta).coerceIn(minTopBarHeightPx, maxTopBarHeightPx)
                val consumed = newHeight - previousHeight

                if (consumed.roundToInt() != 0) {
                    coroutineScope.launch { topBarHeight.snapTo(newHeight) }
                }

                val canConsumeScroll = !(isScrollingDown && newHeight == minTopBarHeightPx)
                return if (canConsumeScroll) Offset(0f, consumed) else Offset.Zero
            }
        }
    }

    LaunchedEffect(lazyListState.isScrollInProgress) {
        if (!lazyListState.isScrollInProgress) {
            val shouldExpand = topBarHeight.value > (minTopBarHeightPx + maxTopBarHeightPx) / 2
            val canExpand =
                    lazyListState.firstVisibleItemIndex == 0 &&
                            lazyListState.firstVisibleItemScrollOffset == 0

            val targetValue =
                    if (shouldExpand && canExpand) maxTopBarHeightPx else minTopBarHeightPx

            if (topBarHeight.value != targetValue) {
                coroutineScope.launch {
                    topBarHeight.animateTo(targetValue, spring(stiffness = Spring.StiffnessMedium))
                }
            }
        }
    }

    Box(
        modifier =
            Modifier.nestedScroll(nestedScrollConnection).fillMaxSize()
    ) {
        val currentTopBarHeightDp = with(density) { topBarHeight.value.toDp() }
        
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = currentTopBarHeightDp + 8.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = MiniPlayerHeight + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 16.dp
            )
        ) {
            item {
               // Use a simple Column for now, or ExpressiveSettingsGroup if preferred strictly for items
               Column(
                    modifier = Modifier.background(Color.Transparent)
               ) {
                    when (category) {
                        SettingsCategory.LIBRARY -> {
                            SettingsSubsection(title = stringResource(R.string.library_structure)) {
                                SettingsItem(
                                    title = stringResource(R.string.excluded_directories),
                                    subtitle = stringResource(R.string.excluded_folders_desc),
                                    leadingIcon = { Icon(Icons.Outlined.Folder, null, tint = MaterialTheme.colorScheme.secondary) },
                                    trailingIcon = { Icon(Icons.Rounded.ChevronRight, "Open", tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                                    onClick = {
                                        showExplorerSheet = true
                                        settingsViewModel.openExplorer()
                                    }
                                )
                                SettingsItem(
                                    title = stringResource(R.string.multi_artist_parsing),
                                    subtitle = stringResource(R.string.multi_artist_parsing_options),
                                    leadingIcon = { Icon(Icons.Outlined.Person, null, tint = MaterialTheme.colorScheme.secondary) },
                                    trailingIcon = { Icon(Icons.Rounded.ChevronRight, "Open", tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                                    onClick = { navController.navigateSafely(Screen.ArtistSettings.route) }
                                )
                            }

                            SettingsSubsection(title = stringResource(R.string.filtering)) {
                                SliderSettingsItem(
                                    label = stringResource(R.string.label_min_song_duration),
                                    value = minSongDurationDraft,
                                    valueRange = 0f..120000f,
                                    steps = 23, // 0, 5, 10, 15, ... 120 seconds (24 positions, 23 steps)
                                    onValueChange = { minSongDurationDraft = it },
                                    onValueChangeFinished = {
                                        val selectedDuration = minSongDurationDraft.toInt()
                                        if (selectedDuration != uiState.minSongDuration) {
                                            settingsViewModel.setMinSongDuration(selectedDuration)
                                        }
                                    },
                                    valueText = { value -> "${(value / 1000).toInt()}s" }
                                )
                            }

                            SettingsSubsection(title = stringResource(R.string.sync_and_scanning)) {
                                RefreshLibraryItem(
                                    isSyncing = isSyncing,
                                    syncProgress = syncProgress,
                                    activeOperationLabel = if (isSyncing) syncIndicatorLabel else null,
                                    onFullSync = {
                                        if (isSyncing) return@RefreshLibraryItem
                                        refreshRequested = true
                                        syncRequestObservedRunning = false
                                        syncIndicatorLabel = "Running full rescan"
                                        Toast.makeText(context, "Full rescan started…", Toast.LENGTH_SHORT).show()
                                        settingsViewModel.fullSyncLibrary()
                                    },
                                    onRebuild = {
                                        if (isSyncing) return@RefreshLibraryItem
                                        showRebuildDatabaseWarning = true
                                    }
                                )
                                SwitchSettingItem(
                                    title = stringResource(R.string.auto_scan_lrc),
                                    subtitle = stringResource(R.string.auto_scan_lrc_desc),
                                    checked = uiState.autoScanLrcFiles,
                                    onCheckedChange = { settingsViewModel.setAutoScanLrcFiles(it) },
                                    leadingIcon = { Icon(Icons.Outlined.Folder, null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                            }

                            SettingsSubsection(
                                title = stringResource(R.string.lyrics_management),
                                addBottomSpace = false
                            ) {
                                ThemeSelectorItem(
                                    label = stringResource(R.string.label_lyrics_source_priority),
                                    description = "Choose which source to try first when fetching lyrics.",
                                    options = mapOf(
                                        LyricsSourcePreference.EMBEDDED_FIRST.name to "Embedded First",
                                        LyricsSourcePreference.API_FIRST.name to "Online First",
                                        LyricsSourcePreference.LOCAL_FIRST.name to "Local (.lrc) First"
                                    ),
                                    selectedKey = uiState.lyricsSourcePreference.name,
                                    onSelectionChanged = { key ->
                                        settingsViewModel.setLyricsSourcePreference(
                                            LyricsSourcePreference.fromName(key)
                                        )
                                    },
                                    leadingIcon = { Icon(painterResource(R.drawable.rounded_lyrics_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                                SettingsItem(
                                    title = stringResource(R.string.reset_imported_lyrics),
                                    subtitle = stringResource(R.string.reset_imported_lyrics_desc),
                                    leadingIcon = { Icon(Icons.Outlined.ClearAll, null, tint = MaterialTheme.colorScheme.secondary) },
                                    onClick = { showClearLyricsDialog = true }
                                )
                            }
                        }
                        SettingsCategory.APPEARANCE -> {
                            val useSmoothCorners by settingsViewModel.useSmoothCorners.collectAsStateWithLifecycle()

                            SettingsSubsection(title = stringResource(R.string.global_theme)) {
                                ThemeSelectorItem(
                                    label = stringResource(R.string.label_app_theme),
                                    description = "Switch between light, dark, or follow system appearance.",
                                    options = mapOf(
                                        AppThemeMode.LIGHT to "Light Theme",
                                        AppThemeMode.DARK to "Dark Theme",
                                        AppThemeMode.FOLLOW_SYSTEM to "Follow System"
                                    ),
                                    selectedKey = uiState.appThemeMode,
                                    onSelectionChanged = { settingsViewModel.setAppThemeMode(it) },
                                    leadingIcon = { Icon(Icons.Outlined.LightMode, null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                                SwitchSettingItem(
                                    title = stringResource(R.string.use_smooth_corners),
                                    subtitle = stringResource(R.string.use_smooth_corners_desc),
                                    checked = useSmoothCorners,
                                    onCheckedChange = settingsViewModel::setUseSmoothCorners,
                                    leadingIcon = { Icon(painterResource(R.drawable.rounded_rounded_corner_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                            }

                            SettingsSubsection(title = stringResource(R.string.now_playing)) {
                                ThemeSelectorItem(
                                    label = stringResource(R.string.label_player_theme),
                                    description = "Choose the appearance for the floating player.",
                                    options = mapOf(
                                        ThemePreference.ALBUM_ART to "Album Art",
                                        ThemePreference.DYNAMIC to "System Dynamic"
                                    ),
                                    selectedKey = uiState.playerThemePreference,
                                    onSelectionChanged = { settingsViewModel.setPlayerThemePreference(it) },
                                    leadingIcon = { Icon(Icons.Outlined.PlayCircle, null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                                SwitchSettingItem(
                                    title = stringResource(R.string.show_player_file_info),
                                    subtitle = stringResource(R.string.show_player_file_info_desc),
                                    checked = uiState.showPlayerFileInfo,
                                    onCheckedChange = { settingsViewModel.setShowPlayerFileInfo(it) },
                                    leadingIcon = { Icon(painterResource(R.drawable.rounded_attach_file_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                                SettingsItem(
                                    title = stringResource(R.string.album_art_palette_style),
                                    subtitle = stringResource(R.string.current_style_prefix) + uiState.albumArtPaletteStyle.label + ". " + stringResource(R.string.open_live_preview),
                                    leadingIcon = { Icon(Icons.Outlined.Style, null, tint = MaterialTheme.colorScheme.secondary) },
                                    trailingIcon = { Icon(Icons.Rounded.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                                    onClick = { navController.navigateSafely(Screen.PaletteStyle.route) }
                                )
                                ThemeSelectorItem(
                                    label = stringResource(R.string.label_carousel_style),
                                    description = "Choose the appearance for the album carousel.",
                                    options = mapOf(
                                        CarouselStyle.NO_PEEK to "No Peek",
                                        CarouselStyle.ONE_PEEK to "One Peek"
                                    ),
                                    selectedKey = uiState.carouselStyle,
                                    onSelectionChanged = { settingsViewModel.setCarouselStyle(it) },
                                    leadingIcon = { Icon(painterResource(R.drawable.rounded_view_carousel_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                            }

                            SettingsSubsection(title = stringResource(R.string.home_collage)) {
                                ThemeSelectorItem(
                                    label = stringResource(R.string.label_collage_pattern),
                                    description = "Choose the shape arrangement for the Your Mix collage.",
                                    options = CollagePattern.entries.associate { it.storageKey to it.label },
                                    selectedKey = uiState.collagePattern.storageKey,
                                    onSelectionChanged = { key ->
                                        settingsViewModel.setCollagePattern(CollagePattern.fromStorageKey(key))
                                    },
                                    leadingIcon = { Icon(painterResource(R.drawable.rounded_view_column_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                                SwitchSettingItem(
                                    title = stringResource(R.string.auto_rotate_patterns),
                                    subtitle = stringResource(R.string.auto_rotate_patterns_desc),
                                    checked = uiState.collageAutoRotate,
                                    onCheckedChange = { settingsViewModel.setCollageAutoRotate(it) },
                                    leadingIcon = { Icon(painterResource(R.drawable.rounded_shuffle_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                            }

                            SettingsSubsection(title = stringResource(R.string.navigation_bar)) {
                                ThemeSelectorItem(
                                    label = stringResource(R.string.label_navbar_style),
                                    description = "Choose the appearance for the navigation bar.",
                                    options = mapOf(
                                        NavBarStyle.DEFAULT to "Default",
                                        NavBarStyle.FULL_WIDTH to "Full Width"
                                    ),
                                    selectedKey = uiState.navBarStyle,
                                    onSelectionChanged = { settingsViewModel.setNavBarStyle(it) },
                                    leadingIcon = { Icon(Icons.Outlined.Style, null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                                SwitchSettingItem(
                                    title = stringResource(R.string.compact_mode),
                                    subtitle = stringResource(R.string.compact_mode_desc),
                                    checked = uiState.navBarCompactMode,
                                    onCheckedChange = { settingsViewModel.setNavBarCompactMode(it) },
                                    leadingIcon = {
                                        Icon(
                                            painterResource(R.drawable.rounded_view_week_24),
                                            null,
                                            tint = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                )
                                SettingsItem(
                                    title = stringResource(R.string.navbar_corner_radius),
                                    subtitle = stringResource(R.string.navbar_corner_radius_desc),
                                    leadingIcon = { Icon(painterResource(R.drawable.rounded_rounded_corner_24), null, tint = MaterialTheme.colorScheme.secondary) },
                                    trailingIcon = { Icon(Icons.Rounded.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                                    onClick = { navController.navigateSafely("nav_bar_corner_radius") }
                                )
                            }

                            SettingsSubsection(title = stringResource(R.string.lyrics_screen)) {
                                SwitchSettingItem(
                                    title = stringResource(R.string.immersive_lyrics),
                                    subtitle = stringResource(R.string.immersive_lyrics_desc),
                                    checked = uiState.immersiveLyricsEnabled,
                                    onCheckedChange = { settingsViewModel.setImmersiveLyricsEnabled(it) },
                                    leadingIcon = { Icon(painterResource(R.drawable.rounded_lyrics_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                )

                                if (uiState.immersiveLyricsEnabled) {
                                    ThemeSelectorItem(
                                        label = stringResource(R.string.label_autohide_delay),
                                        description = "Time before controls hide.",
                                        options = mapOf(
                                            "3000" to "3s",
                                            "4000" to "4s",
                                            "5000" to "5s",
                                            "6000" to "6s"
                                        ),
                                        selectedKey = uiState.immersiveLyricsTimeout.toString(),
                                        onSelectionChanged = { settingsViewModel.setImmersiveLyricsTimeout(it.toLong()) },
                                        leadingIcon = { Icon(Icons.Rounded.Timer, null, tint = MaterialTheme.colorScheme.secondary) }
                                    )
                                }
                            }

                            SettingsSubsection(
                                title = stringResource(R.string.app_navigation),
                                addBottomSpace = false
                            ) {
                                ThemeSelectorItem(
                                    label = stringResource(R.string.label_default_tab),
                                    description = "Choose the Default launch tab.",
                                    options = mapOf(
                                        LaunchTab.HOME to stringResource(R.string.nav_home),
                                        LaunchTab.SEARCH to stringResource(R.string.search),
                                        LaunchTab.LIBRARY to stringResource(R.string.tab_library),
                                    ),
                                    selectedKey = uiState.launchTab,
                                    onSelectionChanged = { settingsViewModel.setLaunchTab(it) },
                                    leadingIcon = { Icon(painterResource(R.drawable.tab_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                                ThemeSelectorItem(
                                    label = stringResource(R.string.label_library_navigation),
                                    description = "Choose how to move between Library tabs.",
                                    options = mapOf(
                                        LibraryNavigationMode.TAB_ROW to "Tab row (default)",
                                        LibraryNavigationMode.COMPACT_PILL to "Compact pill & grid"
                                    ),
                                    selectedKey = uiState.libraryNavigationMode,
                                    onSelectionChanged = { settingsViewModel.setLibraryNavigationMode(it) },
                                    leadingIcon = { Icon(painterResource(R.drawable.rounded_library_music_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                            }
                        }
                        SettingsCategory.PLAYBACK -> {
                            SettingsSubsection(title = stringResource(R.string.background_playback)) {
                                ThemeSelectorItem(
                                    label = stringResource(R.string.label_keep_playing_on_close),
                                    description = "If off, removing the app from recents will stop playback.",
                                    options = mapOf("true" to "On", "false" to "Off"),
                                    selectedKey = if (uiState.keepPlayingInBackground) "true" else "false",
                                    onSelectionChanged = { settingsViewModel.setKeepPlayingInBackground(it.toBoolean()) },
                                    leadingIcon = { Icon(Icons.Rounded.MusicNote, null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                                SettingsItem(
                                    title = stringResource(R.string.battery_optimization),
                                    subtitle = stringResource(R.string.battery_optimization_desc),
                                    onClick = {
                                        val powerManager = context.getSystemService(android.content.Context.POWER_SERVICE) as android.os.PowerManager
                                        if (powerManager.isIgnoringBatteryOptimizations(context.packageName)) {
                                            Toast.makeText(context, "Battery optimization is already disabled", Toast.LENGTH_SHORT).show()
                                            return@SettingsItem
                                        }
                                        try {
                                            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                                                data = "package:${context.packageName}".toUri()
                                            }
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            try {
                                                val fallbackIntent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                                                context.startActivity(fallbackIntent)
                                            } catch (e2: Exception) {
                                                Toast.makeText(context, "Could not open battery settings", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    },
                                    leadingIcon = { Icon(painterResource(R.drawable.rounded_all_inclusive_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                            }

                            SettingsSubsection(title = stringResource(R.string.volume_normalization)) {
                                SwitchSettingItem(
                                    title = stringResource(R.string.enable_replaygain),
                                    subtitle = stringResource(R.string.enable_replaygain_desc),
                                    checked = uiState.replayGainEnabled,
                                    onCheckedChange = { settingsViewModel.setReplayGainEnabled(it) },
                                    leadingIcon = { Icon(painterResource(R.drawable.rounded_volume_down_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                                AnimatedVisibility(
                                    visible = uiState.replayGainEnabled,
                                    enter = expandVertically(animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f)) + fadeIn(animationSpec = spring(stiffness = 400f)),
                                    exit = shrinkVertically(animationSpec = spring(stiffness = 500f)) + fadeOut(animationSpec = spring(stiffness = 500f))
                                ) {
                                    ThemeSelectorItem(
                                        label = stringResource(R.string.label_gain_mode),
                                        description = "Track: normalize each song. Album: normalize per album.",
                                        options = mapOf("track" to stringResource(R.string.track_label), "album" to stringResource(R.string.album_label)),
                                        selectedKey = if (uiState.replayGainUseAlbumGain) "album" else "track",
                                        onSelectionChanged = { settingsViewModel.setReplayGainUseAlbumGain(it == "album") },
                                        leadingIcon = { Icon(painterResource(R.drawable.rounded_volume_down_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                    )
                                }
                            }

                            SettingsSubsection(title = stringResource(R.string.cast)) {
                                ThemeSelectorItem(
                                    label = stringResource(R.string.label_auto_play_cast),
                                    description = "Start playing immediately after switching cast connections.",
                                    options = mapOf("false" to "Enabled", "true" to "Disabled"),
                                    selectedKey = if (uiState.disableCastAutoplay) "true" else "false",
                                    onSelectionChanged = { settingsViewModel.setDisableCastAutoplay(it.toBoolean()) },
                                    leadingIcon = { Icon(painterResource(R.drawable.rounded_cast_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                            }

                            SettingsSubsection(title = stringResource(R.string.headphones)) {
                                SwitchSettingItem(
                                    title = stringResource(R.string.resume_when_headphones_reconnect),
                                    subtitle = stringResource(R.string.resume_when_headphones_reconnect_desc),
                                    checked = uiState.resumeOnHeadsetReconnect,
                                    onCheckedChange = { settingsViewModel.setResumeOnHeadsetReconnect(it) },
                                    leadingIcon = { Icon(painterResource(R.drawable.rounded_headphones_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                            }

                            SettingsSubsection(title = stringResource(R.string.queue_and_transitions)) {
                                ThemeSelectorItem(
                                    label = stringResource(R.string.label_crossfade),
                                    description = "Enable smooth transition between songs.",
                                    options = mapOf("true" to "Enabled", "false" to "Disabled"),
                                    selectedKey = if (uiState.isCrossfadeEnabled) "true" else "false",
                                    onSelectionChanged = { settingsViewModel.setCrossfadeEnabled(it.toBoolean()) },
                                    leadingIcon = { Icon(painterResource(R.drawable.rounded_align_justify_space_even_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                                if (uiState.isCrossfadeEnabled) {
                                    SliderSettingsItem(
                                        label = stringResource(R.string.label_crossfade_duration),
                                        value = uiState.crossfadeDuration.toFloat(),
                                        valueRange = 1000f..12000f,
                                        steps= 10,
                                        onValueChange = { settingsViewModel.setCrossfadeDuration(it.toInt()) },
                                        valueText = { value -> "${(value / 1000).toInt()}s" }
                                    )
                                }
                                SwitchSettingItem(
                                    title = stringResource(R.string.hifi_mode),
                                    subtitle = if (uiState.hiFiModeDeviceSupported)
                                        "Float 32-bit audio output. Disable if playback stutters on your device."
                                    else
                                        "Not supported on this device (PCM_FLOAT AudioTrack unavailable).",
                                    checked = uiState.hiFiModeEnabled,
                                    onCheckedChange = { settingsViewModel.setHiFiModeEnabled(it) },
                                    enabled = uiState.hiFiModeDeviceSupported,
                                    leadingIcon = { Icon(painterResource(R.drawable.outline_high_quality_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                                SwitchSettingItem(
                                    title = stringResource(R.string.persistent_shuffle),
                                    subtitle = stringResource(R.string.persistent_shuffle_desc),
                                    checked = uiState.persistentShuffleEnabled,
                                    onCheckedChange = { settingsViewModel.setPersistentShuffleEnabled(it) },
                                    leadingIcon = { Icon(painterResource(R.drawable.rounded_shuffle_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                                SwitchSettingItem(
                                    title = stringResource(R.string.show_queue_history),
                                    subtitle = stringResource(R.string.show_queue_history_desc),
                                    checked = uiState.showQueueHistory,
                                    onCheckedChange = { settingsViewModel.setShowQueueHistory(it) },
                                    leadingIcon = { Icon(painterResource(R.drawable.rounded_queue_music_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                            }

                        }
                        SettingsCategory.BEHAVIOR -> {
                            SettingsSubsection(
                                title = stringResource(R.string.folders)
                            ) {
                                SwitchSettingItem(
                                    title = stringResource(R.string.back_gesture_folders),
                                    subtitle = stringResource(R.string.back_gesture_folders_desc),
                                    checked = uiState.folderBackGestureNavigation,
                                    onCheckedChange = { settingsViewModel.setFolderBackGestureNavigation(it) },
                                    leadingIcon = {
                                        Icon(
                                            painterResource(R.drawable.rounded_touch_app_24),
                                            null,
                                            tint = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                )
                            }
                            SettingsSubsection(
                                title = stringResource(R.string.player_gestures)
                            ) {
                                SwitchSettingItem(
                                    title = stringResource(R.string.tap_background_closes_player),
                                    subtitle = stringResource(R.string.tap_background_closes_player_desc),
                                    checked = uiState.tapBackgroundClosesPlayer,
                                    onCheckedChange = { settingsViewModel.setTapBackgroundClosesPlayer(it) },
                                    leadingIcon = { Icon(painterResource(R.drawable.rounded_touch_app_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                            }
                            SettingsSubsection(
                                title = stringResource(R.string.haptics),
                                addBottomSpace = false
                            ) {
                                SwitchSettingItem(
                                    title = stringResource(R.string.haptic_feedback),
                                    subtitle = stringResource(R.string.haptic_feedback_desc),
                                    checked = uiState.hapticsEnabled,
                                    onCheckedChange = { settingsViewModel.setHapticsEnabled(it) },
                                    leadingIcon = { Icon(painterResource(R.drawable.rounded_touch_app_24), null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                            }
                        }
                        SettingsCategory.AI_INTEGRATION -> {
                            // AI Provider Selection
                            SettingsSubsection(title = stringResource(R.string.ai_provider)) {
                                ThemeSelectorItem(
                                    label = stringResource(R.string.label_provider),
                                    description = "Choose your AI provider",
                                    options = com.theveloper.pixelplay.data.ai.provider.AiProvider.entries.associate { it.name to it.displayName },
                                    selectedKey = aiProvider,
                                    onSelectionChanged = { settingsViewModel.onAiProviderChange(it) },
                                    leadingIcon = { Icon(Icons.Rounded.Science, null, tint = MaterialTheme.colorScheme.secondary) }
                                )
                                SwitchSettingItem(
                                    title = stringResource(R.string.safe_token_mode),
                                    subtitle = if (uiState.isSafeTokenLimitEnabled) {
                                        "ON — Fast & cheap. Sends minimal data (~1K tokens) to AI."
                                    } else {
                                        "OFF — Deep context. Sends full listening profile (~8K tokens) for richer results."
                                    },
                                    checked = uiState.isSafeTokenLimitEnabled,
                                    onCheckedChange = { settingsViewModel.setSafeTokenLimitEnabled(it) },
                                    leadingIcon = {
                                        Icon(
                                            painterResource(R.drawable.rounded_monitoring_24),
                                            null,
                                            tint = if (uiState.isSafeTokenLimitEnabled) MaterialTheme.colorScheme.primary
                                                   else MaterialTheme.colorScheme.tertiary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                )
                            }
                            
                            // Consolidated API Key Section
                            SettingsSubsection(title = stringResource(R.string.credentials)) {
                                val provider = com.theveloper.pixelplay.data.ai.provider.AiProvider.fromString(aiProvider)
                                val sourceLabel = when(provider) {
                                    com.theveloper.pixelplay.data.ai.provider.AiProvider.GEMINI -> "Google AI Studio (aistudio.google.com)"
                                    com.theveloper.pixelplay.data.ai.provider.AiProvider.DEEPSEEK -> "DeepSeek Platform (api.deepseek.com)"
                                    com.theveloper.pixelplay.data.ai.provider.AiProvider.GROQ -> "Groq Console (console.groq.com)"
                                    com.theveloper.pixelplay.data.ai.provider.AiProvider.MISTRAL -> "Mistral AI Platform (console.mistral.ai)"
                                    com.theveloper.pixelplay.data.ai.provider.AiProvider.NVIDIA -> "NVIDIA Build (build.nvidia.com)"
                                    com.theveloper.pixelplay.data.ai.provider.AiProvider.KIMI -> "Moonshot AI Platform (platform.moonshot.cn)"
                                    com.theveloper.pixelplay.data.ai.provider.AiProvider.GLM -> "Zhipu AI Open Platform (bigmodel.cn)"
                                    com.theveloper.pixelplay.data.ai.provider.AiProvider.OPENAI -> "OpenAI Platform (platform.openai.com)"
                                    com.theveloper.pixelplay.data.ai.provider.AiProvider.OPENROUTER -> "OpenRouter (openrouter.ai)"
                                }
                                
                                AiApiKeyItem(
                                    apiKey = currentAiApiKey,
                                    onApiKeySave = { settingsViewModel.onAiApiKeyChange(it) },
                                    title = "${provider.displayName} API Key",
                                    subtitle = stringResource(R.string.get_from) + sourceLabel
                                )
                            }

                            // Model Selection Section
                            if (currentAiApiKey.isNotBlank()) {
                                SettingsSubsection(title = stringResource(R.string.model_selection)) {
                                    if (uiState.isLoadingModels) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.surfaceContainer,
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(16.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                                            ) {
                                                CircularProgressIndicator(
                                                    modifier = Modifier.size(24.dp),
                                                    strokeWidth = 2.dp
                                                )
                                                Text(
                                                    text = stringResource(R.string.loading_models),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    } else if (uiState.modelsFetchError != null) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.errorContainer,
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = uiState.modelsFetchError ?: "Error loading models",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onErrorContainer,
                                                modifier = Modifier.padding(16.dp)
                                            )
                                        }
                                    } else if (uiState.availableModels.isNotEmpty()) {
                                        ThemeSelectorItem(
                                            label = stringResource(R.string.ai_model_label),
                                            description = "Select a model.",
                                            options = uiState.availableModels.associate { it.name to it.displayName },
                                            selectedKey = currentAiModel.ifEmpty { uiState.availableModels.firstOrNull()?.name ?: "" },
                                            onSelectionChanged = { settingsViewModel.onAiModelChange(it) },
                                            leadingIcon = { Icon(Icons.Rounded.Science, null, tint = MaterialTheme.colorScheme.secondary) }
                                        )
                                    }
                                }
                            }

                            // Prompt Behavior Section
                            SettingsSubsection(
                                title = stringResource(R.string.prompt_behavior),
                                addBottomSpace = false
                            ) {
                                AiSystemPromptItem(
                                    systemPrompt = currentAiSystemPrompt,
                                    defaultPrompt = com.theveloper.pixelplay.data.preferences.AiPreferencesRepository.DEFAULT_SYSTEM_PROMPT,
                                    onSystemPromptSave = { settingsViewModel.onAiSystemPromptChange(it) },
                                    onReset = { settingsViewModel.resetAiSystemPrompt() },
                                    title = stringResource(R.string.system_prompt),
                                    subtitle = stringResource(R.string.system_prompt_desc)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            SettingsSubsection(title = stringResource(R.string.ai_usage_report)) {
                                val recentAiUsage by settingsViewModel.recentAiUsage.collectAsStateWithLifecycle()
                                val totalPromptTokens by settingsViewModel.totalPromptTokens.collectAsStateWithLifecycle()
                                val totalOutputTokens by settingsViewModel.totalOutputTokens.collectAsStateWithLifecycle()
                                val totalThoughtTokens by settingsViewModel.totalThoughtTokens.collectAsStateWithLifecycle()

                                val totalTokens = totalPromptTokens + totalOutputTokens + totalThoughtTokens

                                ActionSettingsItem(
                                    title = stringResource(R.string.total_consumption),
                                    subtitle = "${String.format("%, d", totalTokens)} tokens tracking\nPrompt: ${String.format("%, d", totalPromptTokens)} | Output: ${String.format("%, d", totalOutputTokens)} | Thought: ${String.format("%, d", totalThoughtTokens)}",
                                    icon = {
                                        Icon(
                                            painter = painterResource(R.drawable.rounded_monitoring_24),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.tertiary
                                        )
                                    },
                                    primaryActionLabel = "Clear Logs",
                                    onPrimaryAction = { settingsViewModel.clearAiUsageData() }
                                )

                                if (recentAiUsage.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    var expanded by remember { mutableStateOf(false) }
                                    val rotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f)
                                    
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { expanded = !expanded },
                                        color = Color.Transparent
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    painter = painterResource(R.drawable.rounded_monitoring_24),
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(
                                                    text = "AI Activity Log (${recentAiUsage.size})",
                                                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = GoogleSansRounded),
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                            Icon(
                                                imageVector = Icons.Rounded.ExpandMore,
                                                contentDescription = if (expanded) "Hide" else "Show",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.rotate(rotation)
                                            )
                                        }
                                    }

                                    AnimatedVisibility(
                                        visible = expanded,
                                        enter = expandVertically() + fadeIn(),
                                        exit = shrinkVertically() + fadeOut()
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 8.dp, bottom = 8.dp)
                                        ) {
                                            val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
                                            val groupedUsage = recentAiUsage.groupBy { 
                                                dateFormat.format(Date(it.timestamp)) 
                                            }

                                            groupedUsage.forEach { (date, items) ->
                                                AiUsageDateHeader(date = date)
                                                items.forEach { usage ->
                                                    AiUsageLogItem(usage = usage)
                                                }
                                                Spacer(modifier = Modifier.height(8.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        SettingsCategory.BACKUP_RESTORE -> {
                            if (!uiState.backupInfoDismissed) {
                                BackupInfoNoticeCard(
                                    onDismiss = { settingsViewModel.setBackupInfoDismissed(true) }
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                            SettingsSubsection(title = stringResource(R.string.create_backup)) {
                                ActionSettingsItem(
                                    title = stringResource(R.string.export_backup),
                                    subtitle = "${buildBackupSelectionSummary(exportSections)} " + stringResource(R.string.creates_backup_file),
                                    icon = {
                                        Icon(
                                            painter = painterResource(R.drawable.outline_save_24),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.secondary
                                        )
                                    },
                                    primaryActionLabel = "Select & Export",
                                    onPrimaryAction = { showExportDataDialog = true },
                                    enabled = !uiState.isDataTransferInProgress
                                )
                            }

                            SettingsSubsection(
                                title = stringResource(R.string.restore_backup),
                                addBottomSpace = false
                            ) {
                                ActionSettingsItem(
                                    title = stringResource(R.string.import_backup),
                                    subtitle = stringResource(R.string.import_backup_desc),
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Rounded.Restore,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.secondary
                                        )
                                    },
                                    primaryActionLabel = "Select & Restore",
                                    onPrimaryAction = { showImportFlow = true },
                                    enabled = !uiState.isDataTransferInProgress
                                )
                            }
                        }
                        SettingsCategory.DEVELOPER -> {
                            SettingsSubsection(title = stringResource(R.string.experiments)) {
                                SettingsItem(
                                    title = stringResource(R.string.experimental_settings),
                                    subtitle = stringResource(R.string.experimental_settings_desc),
                                    leadingIcon = { Icon(Icons.Rounded.Science, null, tint = MaterialTheme.colorScheme.secondary) },
                                    trailingIcon = { Icon(Icons.Rounded.ChevronRight, "Open", tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                                    onClick = { navController.navigateSafely(Screen.Experimental.route) }
                                )
                                SettingsItem(
                                    title = stringResource(R.string.test_setup_flow),
                                    subtitle = stringResource(R.string.test_setup_flow_desc),
                                    leadingIcon = { Icon(Icons.Rounded.Science, null, tint = MaterialTheme.colorScheme.tertiary) },
                                    onClick = {
                                        settingsViewModel.resetSetupFlow()
                                    }
                                )
                            }

                            SettingsSubsection(title = stringResource(R.string.maintenance)) {
                                ActionSettingsItem(
                                    title = stringResource(R.string.force_daily_mix_regen),
                                    subtitle = stringResource(R.string.force_daily_mix_regen_desc),
                                    icon = { Icon(painterResource(R.drawable.rounded_instant_mix_24), null, tint = MaterialTheme.colorScheme.secondary) },
                                    primaryActionLabel = "Regenerate Daily Mix",
                                    onPrimaryAction = { showRegenerateDailyMixDialog = true }
                                )
                                ActionSettingsItem(
                                    title = stringResource(R.string.force_stats_regen),
                                    subtitle = stringResource(R.string.force_stats_regen_desc),
                                    icon = { Icon(painterResource(R.drawable.rounded_monitoring_24), null, tint = MaterialTheme.colorScheme.secondary) },
                                    primaryActionLabel = "Regenerate Stats",
                                    onPrimaryAction = { showRegenerateStatsDialog = true }
                                )
                                ActionSettingsItem(
                                    title = stringResource(R.string.force_album_palette_regen),
                                    subtitle = if (paletteRegenerateTargets.isEmpty()) {
                                        "No songs with album art were found."
                                    } else {
                                        "Rebuild all cached palette variants for every album art, or pick a single one to refresh."
                                    },
                                    icon = { Icon(Icons.Outlined.Style, null, tint = MaterialTheme.colorScheme.secondary) },
                                    primaryActionLabel = if (isPaletteBulkRegenerateRunning) "Regenerating..." else "Regenerate All",
                                    onPrimaryAction = { showRegenerateAllPalettesDialog = true },
                                    secondaryActionLabel = "Choose Song",
                                    onSecondaryAction = { showPaletteRegenerateSheet = true },
                                    enabled = paletteRegenerateTargets.isNotEmpty() && !isAnyPaletteRegenerateRunning
                                )
                            }

                            SettingsSubsection(
                                title = stringResource(R.string.diagnostics),
                                addBottomSpace = false
                            ) {
                                SettingsItem(
                                    title = stringResource(R.string.trigger_test_crash),
                                    subtitle = stringResource(R.string.trigger_test_crash_desc),
                                    leadingIcon = { Icon(Icons.Outlined.Warning, null, tint = MaterialTheme.colorScheme.error) },
                                    onClick = { settingsViewModel.triggerTestCrash() }
                                )
                            }
                        }
                        SettingsCategory.ABOUT -> {
                            SettingsSubsection(
                                title = stringResource(R.string.application),
                                addBottomSpace = false
                            ) {
                                SettingsItem(
                                    title = stringResource(R.string.about_pixelplayer),
                                    subtitle = stringResource(R.string.about_pixelplayer_subtitle),
                                    leadingIcon = { Icon(Icons.Outlined.Info, null, tint = MaterialTheme.colorScheme.secondary) },
                                    trailingIcon = { Icon(Icons.Rounded.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                                    onClick = { navController.navigateSafely("about") }
                                )
                            }
                        }
                        SettingsCategory.EQUALIZER -> {
                             // Equalizer has its own screen, so this block is unreachable via standard navigation
                             // but required for exhaustiveness.
                        }
                        SettingsCategory.DEVICE_CAPABILITIES -> {
                             // Device Capabilities has its own screen
                        }

                    }
               }
            }

            item {
                // Spacer handled by contentPadding
                Spacer(Modifier.height(1.dp))
            }
        }

        CollapsibleCommonTopBar(
            collapseFraction = collapseFraction,
            headerHeight = currentTopBarHeightDp,
            onBackClick = onBackClick,
            title = category.title,
            maxLines = titleMaxLines
        )

        // Block interaction during transition
        var isTransitioning by remember { mutableStateOf(true) }
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(com.theveloper.pixelplay.presentation.navigation.TRANSITION_DURATION.toLong())
            isTransitioning = false
        }
        
        if (isTransitioning) {
            Box(modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                   awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent()
                        }
                    }
                }
            )
        }
    }

    BackupTransferProgressDialogHost(progress = dataTransferProgress)

    // Dialogs
    FileExplorerDialog(
        visible = showExplorerSheet,
        currentPath = currentPath,
        directoryChildren = directoryChildren,
        availableStorages = availableStorages,
        selectedStorageIndex = selectedStorageIndex,
        isLoading = isLoadingDirectories,
        isPriming = isExplorerPriming,
        isReady = isExplorerReady,
        isCurrentDirectoryResolved = isCurrentDirectoryResolved,
        isAtRoot = settingsViewModel.isAtRoot(),
        rootDirectory = explorerRoot,
        onNavigateTo = settingsViewModel::loadDirectory,
        onNavigateUp = settingsViewModel::navigateUp,
        onNavigateHome = { settingsViewModel.loadDirectory(explorerRoot) },
        onToggleAllowed = settingsViewModel::toggleDirectoryAllowed,
        onRefresh = settingsViewModel::refreshExplorer,
        onStorageSelected = settingsViewModel::selectStorage,
        onDone = {
            settingsViewModel.applyPendingDirectoryRuleChanges()
            showExplorerSheet = false
        },
        onDismiss = {
            settingsViewModel.applyPendingDirectoryRuleChanges()
            showExplorerSheet = false
        }
    )

    if (showPaletteRegenerateSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                if (!isAnyPaletteRegenerateRunning) {
                    showPaletteRegenerateSheet = false
                    paletteSongSearchQuery = ""
                }
            },
            sheetState = paletteRegenerateSheetState
        ) {
            PaletteRegenerateSongSheetContent(
                songs = filteredPaletteSongs,
                isRunning = isPaletteRegenerateRunning,
                searchQuery = paletteSongSearchQuery,
                onSearchQueryChange = { paletteSongSearchQuery = it },
                onClearSearch = { paletteSongSearchQuery = "" },
                onSongClick = { song ->
                    if (isAnyPaletteRegenerateRunning) return@PaletteRegenerateSongSheetContent
                    isPaletteRegenerateRunning = true
                    coroutineScope.launch {
                        val success = playerViewModel.forceRegenerateAlbumPaletteForSong(song)
                        isPaletteRegenerateRunning = false
                        if (success) {
                            showPaletteRegenerateSheet = false
                            paletteSongSearchQuery = ""
                            Toast.makeText(
                                context,
                                "Palette regenerated for ${song.title}",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                "Could not regenerate palette for ${song.title}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            )
        }
    }

    if (showRegenerateAllPalettesDialog) {
        AlertDialog(
            icon = {
                Icon(
                    Icons.Outlined.Style,
                    null,
                    tint = if (isPaletteBulkRegenerateRunning) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.secondary
                    }
                )
            },
            title = {
                Text(
                    if (isPaletteBulkRegenerateRunning) {
                        "Regenerating album palettes..."
                    } else {
                        "Regenerate all album palettes?"
                    }
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (isPaletteBulkRegenerateRunning) {
                            "Rebuilding cached palette variants for $paletteBulkTotalCount unique album arts. This can take a while on large libraries."
                        } else {
                            "This will clear cached theme data and rebuild all palette styles for ${paletteRegenerateTargets.size} unique album arts."
                        }
                    )

                    if (isPaletteBulkRegenerateRunning) {
                        val progress = if (paletteBulkTotalCount > 0) {
                            paletteBulkCompletedCount.toFloat() / paletteBulkTotalCount.toFloat()
                        } else {
                            0f
                        }

                        LinearWavyProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(50)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceContainerHighest
                        )

                        Text(
                            text = "$paletteBulkCompletedCount of $paletteBulkTotalCount completed",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            },
            onDismissRequest = {
                if (!isPaletteBulkRegenerateRunning) {
                    showRegenerateAllPalettesDialog = false
                }
            },
            confirmButton = {
                if (isPaletteBulkRegenerateRunning) {
                    TextButton(
                        onClick = {},
                        enabled = false
                    ) {
                        Text(stringResource(R.string.working_ellipsis))
                    }
                } else {
                    TextButton(
                        onClick = {
                            isPaletteBulkRegenerateRunning = true
                            paletteBulkCompletedCount = 0
                            paletteBulkTotalCount = paletteRegenerateTargets.size

                            coroutineScope.launch {
                                var successCount = 0
                                paletteRegenerateTargets.forEachIndexed { index, song ->
                                    if (playerViewModel.forceRegenerateAlbumPaletteForSong(song)) {
                                        successCount++
                                    }
                                    paletteBulkCompletedCount = index + 1
                                }

                                isPaletteBulkRegenerateRunning = false
                                showRegenerateAllPalettesDialog = false

                                val totalCount = paletteRegenerateTargets.size
                                Toast.makeText(
                                    context,
                                    if (successCount == totalCount) {
                                        "Regenerated $successCount album art palettes"
                                    } else {
                                        "Regenerated $successCount of $totalCount album art palettes"
                                    },
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    ) {
                        Text(stringResource(R.string.regenerate))
                    }
                }
            },
            dismissButton = {
                if (!isPaletteBulkRegenerateRunning) {
                    TextButton(
                        onClick = { showRegenerateAllPalettesDialog = false }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            }
        )
    }
    
     // Dialogs logic (copied)
    if (showClearLyricsDialog) {
        AlertDialog(
            icon = { Icon(Icons.Outlined.Warning, null) },
            title = { Text(stringResource(R.string.reset_imported_lyrics_dialog_title)) },
            text = { Text(stringResource(R.string.this_action_cannot_be_undone)) },
            onDismissRequest = { showClearLyricsDialog = false },
            confirmButton = { TextButton(onClick = { showClearLyricsDialog = false; playerViewModel.resetAllLyrics() }) { Text(stringResource(R.string.confirm)) } },
            dismissButton = { TextButton(onClick = { showClearLyricsDialog = false }) { Text(stringResource(R.string.cancel)) } }
        )
    }

    
    if (showRebuildDatabaseWarning) {
        AlertDialog(
            icon = { Icon(Icons.Outlined.Warning, null, tint = MaterialTheme.colorScheme.error) },
            title = { Text(stringResource(R.string.rebuild_database_title2)) },
            text = { Text(stringResource(R.string.rebuild_database_body)) },
            onDismissRequest = { showRebuildDatabaseWarning = false },
            confirmButton = { 
                TextButton(
                    onClick = { 
                        showRebuildDatabaseWarning = false
                        refreshRequested = true
                        syncRequestObservedRunning = false
                        syncIndicatorLabel = "Rebuilding database"
                        Toast.makeText(context, "Rebuilding database…", Toast.LENGTH_SHORT).show()
                        settingsViewModel.rebuildDatabase() 
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { 
                    Text(stringResource(R.string.rebuild)) 
                } 
            },
            dismissButton = { TextButton(onClick = { showRebuildDatabaseWarning = false }) { Text(stringResource(R.string.cancel)) } }
        )
    }

    if (showRegenerateDailyMixDialog) {
        AlertDialog(
            icon = { Icon(painterResource(R.drawable.rounded_instant_mix_24), null, tint = MaterialTheme.colorScheme.primary) },
            title = { Text(stringResource(R.string.regenerate_daily_mix_title2)) },
            text = { Text(stringResource(R.string.regenerate_daily_mix_body)) },
            onDismissRequest = { showRegenerateDailyMixDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRegenerateDailyMixDialog = false
                        playerViewModel.forceUpdateDailyMix()
                        Toast.makeText(context, "Daily Mix regeneration started", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text(stringResource(R.string.regenerate))
                }
            },
            dismissButton = { TextButton(onClick = { showRegenerateDailyMixDialog = false }) { Text(stringResource(R.string.cancel)) } }
        )
    }

    if (showRegenerateStatsDialog) {
        AlertDialog(
            icon = { Icon(painterResource(R.drawable.rounded_monitoring_24), null, tint = MaterialTheme.colorScheme.primary) },
            title = { Text(stringResource(R.string.regenerate_stats_title2)) },
            text = { Text(stringResource(R.string.regenerate_stats_body)) },
            onDismissRequest = { showRegenerateStatsDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRegenerateStatsDialog = false
                        statsViewModel.forceRegenerateStats()
                        Toast.makeText(context, "Stats regeneration started", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text(stringResource(R.string.regenerate))
                }
            },
            dismissButton = { TextButton(onClick = { showRegenerateStatsDialog = false }) { Text(stringResource(R.string.cancel)) } }
        )
    }

    if (showExportDataDialog) {
        BackupSectionSelectionDialog(
            operation = BackupOperationType.EXPORT,
            title = stringResource(R.string.export_backup),
            supportingText = "Choose exactly what you want to include in the backup package.",
            selectedSections = exportSections,
            confirmLabel = "Export .pxpl",
            inProgress = uiState.isDataTransferInProgress,
            onDismiss = { showExportDataDialog = false },
            onSelectionChanged = { exportSections = it },
            onConfirm = {
                showExportDataDialog = false
                val fileName = "PixelPlayer_Backup_${System.currentTimeMillis()}.pxpl"
                exportLauncher.launch(fileName)
            }
        )
    }

    if (showImportFlow) {
        val restorePlan = uiState.restorePlan
        if (restorePlan != null && importFileUri != null) {
            // Step 2: Module selection from inspected backup
            ImportModuleSelectionDialog(
                plan = restorePlan,
                inProgress = uiState.isDataTransferInProgress,
                onDismiss = {
                    showImportFlow = false
                    importFileUri = null
                    settingsViewModel.clearRestorePlan()
                },
                onBack = {
                    importFileUri = null
                    settingsViewModel.clearRestorePlan()
                },
                onSelectionChanged = { settingsViewModel.updateRestorePlanSelection(it) },
                onConfirm = {
                    settingsViewModel.restoreFromPlan(importFileUri!!)
                    showImportFlow = false
                    importFileUri = null
                }
            )
        } else {
            // Step 1: File selection with backup history
            ImportFileSelectionDialog(
                backupHistory = uiState.backupHistory,
                isInspecting = uiState.isInspectingBackup,
                onDismiss = {
                    showImportFlow = false
                    importFileUri = null
                    settingsViewModel.clearRestorePlan()
                },
                onBrowseFile = { importFilePicker.launch("*/*") },
                onHistoryItemSelected = { entry ->
                    val uri = entry.uri.toUri()
                    importFileUri = uri
                    settingsViewModel.inspectBackupFile(uri)
                },
                onRemoveHistoryEntry = { settingsViewModel.removeBackupHistoryEntry(it) }
            )
        }
    }
}

private fun buildBackupSelectionSummary(selected: Set<BackupSection>): String {
    if (selected.isEmpty()) return "No sections selected."
    val total = BackupSection.entries.size
    return if (selected.size == total) {
        "All sections selected."
    } else {
        "Selected ${selected.size} of $total sections."
    }
}

private fun backupSectionIconRes(section: BackupSection): Int {
    return section.iconRes
}

@Composable
private fun BackupInfoNoticeCard(
    onDismiss: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                painter = painterResource(R.drawable.rounded_upload_file_24),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(20.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "How backup works",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Choose sections, export a .pxpl file, and import it later. Restore only replaces the sections you select.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                )
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.rounded_close_24),
                    contentDescription = stringResource(R.string.cd_close),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun BackupSectionSelectionDialog(
    operation: BackupOperationType,
    title: String,
    supportingText: String,
    selectedSections: Set<BackupSection>,
    confirmLabel: String,
    inProgress: Boolean,
    onDismiss: () -> Unit,
    onSelectionChanged: (Set<BackupSection>) -> Unit,
    onConfirm: () -> Unit
) {
    val listState = rememberLazyListState()
    val selectedCount = selectedSections.size
    val totalCount = BackupSection.entries.size
    val transitionState = remember { MutableTransitionState(false) }
    var shouldShowDialog by remember { mutableStateOf(true) }
    var onDialogHiddenAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    transitionState.targetState = shouldShowDialog

    fun closeDialog(afterClose: () -> Unit) {
        if (!shouldShowDialog) return
        onDialogHiddenAction = afterClose
        shouldShowDialog = false
    }

    LaunchedEffect(transitionState.currentState, transitionState.targetState) {
        if (!transitionState.currentState && !transitionState.targetState) {
            onDialogHiddenAction?.let { action ->
                onDialogHiddenAction = null
                action()
            }
        }
    }

    if (transitionState.currentState || transitionState.targetState) {
        Dialog(
            onDismissRequest = { closeDialog(onDismiss) },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            AnimatedVisibility(
                visibleState = transitionState,
                enter = slideInVertically(initialOffsetY = { it / 6 }) + fadeIn(animationSpec = tween(220)),
                exit = slideOutVertically(targetOffsetY = { it / 6 }) + fadeOut(animationSpec = tween(200)),
                label = "backup_section_dialog"
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest
                ) {
                    Scaffold(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        contentWindowInsets = WindowInsets.systemBars,
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontSize = 24.sp,
                                            textGeometricTransform = TextGeometricTransform(scaleX = 1.2f),
                                        ),
                                        fontFamily = GoogleSansRounded,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                navigationIcon = {
                                    FilledIconButton(
                                        modifier = Modifier.padding(start = 6.dp),
                                        onClick = { closeDialog(onDismiss) },
                                        colors = IconButtonDefaults.filledIconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                                            contentColor = MaterialTheme.colorScheme.onSurface
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Close,
                                            contentDescription = stringResource(R.string.cd_close)
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                                )
                            )
                        },
                        bottomBar = {
                            BottomAppBar(
                                windowInsets = WindowInsets.navigationBars,
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier.padding(start = 10.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        FilledIconButton(
                                            onClick = { onSelectionChanged(BackupSection.entries.toSet()) },
                                            enabled = !inProgress,
                                            colors = IconButtonDefaults.filledIconButtonColors(
                                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                            )
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.round_select_all_24),
                                                contentDescription = stringResource(R.string.cd_select_all)
                                            )
                                        }
                                        FilledIconButton(
                                            onClick = { onSelectionChanged(emptySet()) },
                                            enabled = !inProgress,
                                            colors = IconButtonDefaults.filledIconButtonColors(
                                                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                                contentColor = MaterialTheme.colorScheme.onSurface
                                            )
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.baseline_deselect_24),
                                                contentDescription = stringResource(R.string.cd_clear_selection)
                                            )
                                        }
                                    }

                                    ExtendedFloatingActionButton(
                                        onClick = { closeDialog(onConfirm) },
                                        modifier = Modifier
                                            .padding(end = 6.dp)
                                            .height(48.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        containerColor = if (operation == BackupOperationType.EXPORT) {
                                            MaterialTheme.colorScheme.tertiaryContainer
                                        } else {
                                            MaterialTheme.colorScheme.primaryContainer
                                        },
                                        contentColor = if (operation == BackupOperationType.EXPORT) {
                                            MaterialTheme.colorScheme.onTertiaryContainer
                                        } else {
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                        }
                                    ) {
                                        if (inProgress) {
                                            LoadingIndicator(modifier = Modifier.height(20.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = if (operation == BackupOperationType.EXPORT) "Exporting" else "Importing",
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        } else {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    painter = painterResource(
                                                        if (operation == BackupOperationType.EXPORT) {
                                                            R.drawable.outline_save_24
                                                        } else {
                                                            R.drawable.rounded_upload_file_24
                                                        }
                                                    ),
                                                    contentDescription = confirmLabel
                                                )

                                                Text(
                                                    text = if (operation == BackupOperationType.EXPORT) {
                                                        "Export Backup"
                                                    } else {
                                                        "Import Backup"
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .padding(horizontal = 18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.62f),
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = supportingText,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "$selectedCount of $totalCount sections selected",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    if (inProgress) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            LoadingIndicator(modifier = Modifier.height(24.dp))
                                            Text(
                                                text = "Transfer in progress...",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }

                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 18.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(BackupSection.entries, key = { it.key }) { section ->
                                    val isSelected = section in selectedSections
                                    BackupSectionSelectableCard(
                                        section = section,
                                        selected = isSelected,
                                        enabled = !inProgress,
                                        onToggle = {
                                            onSelectionChanged(
                                                if (isSelected) selectedSections - section else selectedSections + section
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun BackupSectionSelectableCard(
    section: BackupSection,
    selected: Boolean,
    enabled: Boolean,
    detail: ModuleRestoreDetail? = null,
    onToggle: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        } else {
            Color.Transparent
        },
        label = "backup_section_border"
    )
    val borderWidth by animateDpAsState(
        targetValue = if (selected) 2.5.dp else 1.dp,
        label = "backup_section_border_width"
    )
    val iconContainerColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerHighest
        },
        label = "backup_section_icon_bg"
    )
    val iconTint by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "backup_section_icon_tint"
    )

    Surface(
        onClick = onToggle,
        enabled = enabled,
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(width = borderWidth, color = borderColor),
        tonalElevation = if (selected) 2.dp else 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    color = iconContainerColor,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(backupSectionIconRes(section)),
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = section.label,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = GoogleSansRounded
                        ),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = section.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (detail != null && detail.entryCount > 0) {
                        Text(
                            text = "${detail.entryCount} entries · Will replace current data",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

                Switch(
                    checked = selected,
                    onCheckedChange = { onToggle() },
                    enabled = enabled,
                    thumbContent = {
                        AnimatedContent(
                            targetState = selected,
                            transitionSpec = { fadeIn(tween(100)) togetherWith fadeOut(tween(100)) },
                            label = "switch_thumb_icon"
                        ) { isSelected ->
                            Icon(
                                imageVector = if (isSelected) Icons.Rounded.Check else Icons.Rounded.Close,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize)
                            )
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        checkedIconColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                        uncheckedIconColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }
        }
    }
}

private const val BackupTransferDialogMinimumVisibilityMs = 1500L

@Composable
private fun BackupTransferProgressDialogHost(progress: BackupTransferProgressUpdate?) {
    var visibleProgress by remember { mutableStateOf<BackupTransferProgressUpdate?>(null) }
    var visibleSinceMs by remember { mutableStateOf(0L) }
    var isHoldingForMinimumTime by remember { mutableStateOf(false) }

    LaunchedEffect(progress) {
        if (progress != null) {
            if (visibleProgress == null || isHoldingForMinimumTime) {
                visibleSinceMs = SystemClock.elapsedRealtime()
            }
            isHoldingForMinimumTime = false
            visibleProgress = progress
            return@LaunchedEffect
        }

        val currentVisibleProgress = visibleProgress ?: return@LaunchedEffect
        isHoldingForMinimumTime = true
        val elapsed = SystemClock.elapsedRealtime() - visibleSinceMs
        val remaining = BackupTransferDialogMinimumVisibilityMs - elapsed
        if (remaining > 0) {
            delay(remaining)
        }
        if (visibleProgress == currentVisibleProgress) {
            visibleProgress = null
            visibleSinceMs = 0L
        }
        isHoldingForMinimumTime = false
    }

    visibleProgress?.let { currentProgress ->
        BackupTransferProgressDialog(progress = currentProgress)
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun BackupTransferProgressDialog(progress: BackupTransferProgressUpdate) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 300),
        label = "BackupTransferProgress"
    )
    val progressPercent = (animatedProgress * 100f).roundToInt().coerceIn(0, 100)
    val statusText = if (progress.operation == BackupOperationType.EXPORT) {
        "Exporting"
    } else {
        "Importing"
    }
    val stepText = "Step ${progress.step.coerceAtLeast(1)} of ${progress.totalSteps}"

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            tonalElevation = 6.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (progress.operation == BackupOperationType.EXPORT) {
                        "Creating Backup"
                    } else {
                        "Restoring Backup"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )

                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(1.84f),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "$progressPercent%",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = MaterialTheme.typography.labelLarge.fontSize * 1.4f
                        ),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                LinearWavyProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(50)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceContainerHighest
                )

                Text(
                    text = progress.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "$statusText • $stepText",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                AnimatedContent(
                    targetState = progress.detail,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "BackupStepDetail"
                ) { animatedDetail ->
                    Text(
                        text = animatedDetail,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }

                progress.section?.let { section ->
                    Text(
                        text = section.label,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ImportFileSelectionDialog(
    backupHistory: List<BackupHistoryEntry>,
    isInspecting: Boolean,
    onDismiss: () -> Unit,
    onBrowseFile: () -> Unit,
    onHistoryItemSelected: (BackupHistoryEntry) -> Unit,
    onRemoveHistoryEntry: (BackupHistoryEntry) -> Unit
) {
    val context = LocalContext.current
    val transitionState = remember { MutableTransitionState(false) }
    var shouldShowDialog by remember { mutableStateOf(true) }
    var onDialogHiddenAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    transitionState.targetState = shouldShowDialog

    fun closeDialog(afterClose: () -> Unit) {
        if (!shouldShowDialog) return
        onDialogHiddenAction = afterClose
        shouldShowDialog = false
    }

    LaunchedEffect(transitionState.currentState, transitionState.targetState) {
        if (!transitionState.currentState && !transitionState.targetState) {
            onDialogHiddenAction?.let { action ->
                onDialogHiddenAction = null
                action()
            }
        }
    }

    if (transitionState.currentState || transitionState.targetState) {
        Dialog(
            onDismissRequest = { closeDialog(onDismiss) },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                decorFitsSystemWindows = false
            )
        ) {
            AnimatedVisibility(
                visibleState = transitionState,
                enter = slideInVertically(initialOffsetY = { it / 6 }) + fadeIn(animationSpec = tween(220)),
                exit = slideOutVertically(targetOffsetY = { it / 6 }) + fadeOut(animationSpec = tween(200)),
                label = "import_file_selection_dialog"
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surfaceContainerLowest
                ) {
                    Scaffold(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        contentWindowInsets = WindowInsets.systemBars,
                        topBar = {
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(
                                        text = "Import Backup",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontSize = 24.sp,
                                            textGeometricTransform = TextGeometricTransform(scaleX = 1.2f),
                                        ),
                                        fontFamily = GoogleSansRounded,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                navigationIcon = {
                                    FilledIconButton(
                                        modifier = Modifier.padding(start = 6.dp),
                                        onClick = { closeDialog(onDismiss) },
                                        colors = IconButtonDefaults.filledIconButtonColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                                            contentColor = MaterialTheme.colorScheme.onSurface
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Close,
                                            contentDescription = stringResource(R.string.cd_close)
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                                )
                            )
                        },
                        bottomBar = {
                            BottomAppBar(
                                windowInsets = WindowInsets.navigationBars,
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ExtendedFloatingActionButton(
                                        onClick = onBrowseFile,
                                        modifier = Modifier.height(48.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    ) {
                                        if (isInspecting) {
                                            LoadingIndicator(modifier = Modifier.height(20.dp))
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Inspecting...",
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        } else {
                                            Icon(
                                                painter = painterResource(R.drawable.rounded_upload_file_24),
                                                contentDescription = null
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Browse for file",
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    ) { innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .padding(horizontal = 18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.62f),
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Select a .pxpl backup file to inspect. You'll choose which sections to restore in the next step.",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            if (backupHistory.isNotEmpty()) {
                                Text(
                                    text = "Recent Backups",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                                )
                            }

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 18.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                if (backupHistory.isEmpty()) {
                                    item {
                                        Surface(
                                            color = MaterialTheme.colorScheme.surfaceContainerLow,
                                            shape = RoundedCornerShape(18.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(24.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Rounded.Restore,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(36.dp),
                                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                                )
                                                Text(
                                                    text = "No recent backups",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Text(
                                                    text = "Previously imported backups will appear here.",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    items(backupHistory, key = { it.uri }) { entry ->
                                        BackupHistoryCard(
                                            entry = entry,
                                            context = context,
                                            onSelect = { onHistoryItemSelected(entry) },
                                            onRemove = { onRemoveHistoryEntry(entry) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BackupHistoryCard(
    entry: BackupHistoryEntry,
    context: android.content.Context,
    onSelect: () -> Unit,
    onRemove: () -> Unit
) {
    val dateText = remember(entry.createdAt) {
        val sdf = java.text.SimpleDateFormat("MMM d, yyyy 'at' h:mm a", java.util.Locale.getDefault())
        sdf.format(java.util.Date(entry.createdAt))
    }
    val sizeText = remember(entry.sizeBytes) {
        if (entry.sizeBytes > 0) Formatter.formatShortFileSize(context, entry.sizeBytes) else ""
    }
    val moduleCount = entry.modules.size

    Surface(
        onClick = onSelect,
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(R.drawable.rounded_upload_file_24),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = entry.displayName,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontFamily = GoogleSansRounded
                        ),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = buildString {
                            append(dateText)
                            if (sizeText.isNotEmpty()) append(" · $sizeText")
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = stringResource(R.string.cd_remove_from_history),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "$moduleCount modules · v${entry.appVersion.ifEmpty { "?" }} · schema v${entry.schemaVersion}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun ImportModuleSelectionDialog(
    plan: RestorePlan,
    inProgress: Boolean,
    onDismiss: () -> Unit,
    onBack: () -> Unit,
    onSelectionChanged: (Set<BackupSection>) -> Unit,
    onConfirm: () -> Unit
) {
    BackupModuleSelectionDialog(
        plan = plan,
        inProgress = inProgress,
        onDismiss = onDismiss,
        onBack = onBack,
        onSelectionChanged = onSelectionChanged,
        onConfirm = onConfirm
    )
}
@Composable
private fun PaletteRegenerateSongSheetContent(
    songs: List<Song>,
    isRunning: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    onSongClick: (Song) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Force Regenerate Album Palette",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Select a song to clear cached theme data and regenerate all palette styles from the album art.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isRunning,
            placeholder = { Text(stringResource(R.string.search_by_title_artist_album)) },
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(
                        onClick = onClearSearch,
                        enabled = !isRunning
                    ) {
                        Icon(Icons.Outlined.ClearAll, contentDescription = stringResource(R.string.cd_clear_search))
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer
            )
        )

        if (isRunning) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp
                )
                Text(
                    text = "Regenerating palette...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp, max = 460.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (songs.isEmpty()) {
                item {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "No songs match your search.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(songs, key = { it.id }) { song ->
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable(enabled = !isRunning) { onSongClick(song) }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = song.title,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = song.displayArtist,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (song.album.isNotBlank()) {
                                Text(
                                    text = song.album,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSubsectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun SettingsSubsection(
    title: String,
    addBottomSpace: Boolean = true,
    content: @Composable () -> Unit
) {
    SettingsSubsectionHeader(title)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        content()
    }
    if (addBottomSpace) {
        Spacer(modifier = Modifier.height(10.dp))
    }
}
