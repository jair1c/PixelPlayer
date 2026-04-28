package com.theveloper.pixelplay.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.theveloper.pixelplay.R
import com.theveloper.pixelplay.data.model.LibraryTabId
import com.theveloper.pixelplay.data.model.StorageFilter
import com.theveloper.pixelplay.presentation.components.MiniPlayerHeight
import com.theveloper.pixelplay.ui.theme.GoogleSansRounded
import androidx.compose.ui.res.stringResource

private data class LibraryEmptySpec(
    val iconRes: Int,
    val title: String,
    val subtitle: String
)

private fun libraryEmptySpec(
    tabId: LibraryTabId,
    storageFilter: StorageFilter
): LibraryEmptySpec {
    return when (tabId) {
        LibraryTabId.SONGS -> when (storageFilter) {
            StorageFilter.ALL -> LibraryEmptySpec(
                iconRes = R.drawable.rounded_music_off_24,
                title = stringResource(R.string.no_songs_yet),
                subtitle = stringResource(R.string.add_music_prompt)
            )
            StorageFilter.OFFLINE -> LibraryEmptySpec(
                iconRes = R.drawable.rounded_music_off_24,
                title = stringResource(R.string.no_local_songs),
                subtitle = stringResource(R.string.try_rescan_library)
            )
            StorageFilter.ONLINE -> LibraryEmptySpec(
                iconRes = R.drawable.rounded_music_off_24,
                title = stringResource(R.string.no_cloud_songs),
                subtitle = stringResource(R.string.sync_cloud_netease)
            )
        }

        LibraryTabId.ALBUMS -> when (storageFilter) {
            StorageFilter.ALL -> LibraryEmptySpec(
                iconRes = R.drawable.rounded_album_24,
                title = stringResource(R.string.no_albums_available),
                subtitle = stringResource(R.string.albums_appear_after_index)
            )
            StorageFilter.OFFLINE -> LibraryEmptySpec(
                iconRes = R.drawable.rounded_album_24,
                title = stringResource(R.string.no_local_albums),
                subtitle = stringResource(R.string.local_songs_required)
            )
            StorageFilter.ONLINE -> LibraryEmptySpec(
                iconRes = R.drawable.rounded_album_24,
                title = stringResource(R.string.no_cloud_albums),
                subtitle = stringResource(R.string.cloud_albums_appear)
            )
        }

        LibraryTabId.ARTISTS -> when (storageFilter) {
            StorageFilter.ALL -> LibraryEmptySpec(
                iconRes = R.drawable.rounded_artist_24,
                title = stringResource(R.string.no_artists_available),
                subtitle = stringResource(R.string.artists_appear_after_index)
            )
            StorageFilter.OFFLINE -> LibraryEmptySpec(
                iconRes = R.drawable.rounded_artist_24,
                title = stringResource(R.string.no_local_artists),
                subtitle = stringResource(R.string.no_local_artist_meta)
            )
            StorageFilter.ONLINE -> LibraryEmptySpec(
                iconRes = R.drawable.rounded_artist_24,
                title = stringResource(R.string.no_cloud_artists),
                subtitle = stringResource(R.string.cloud_artists_appear)
            )
        }

        LibraryTabId.LIKED -> when (storageFilter) {
            StorageFilter.ALL -> LibraryEmptySpec(
                iconRes = R.drawable.rounded_favorite_24,
                title = stringResource(R.string.no_liked_songs_yet),
                subtitle = stringResource(R.string.liked_songs_prompt)
            )
            StorageFilter.OFFLINE -> LibraryEmptySpec(
                iconRes = R.drawable.rounded_favorite_24,
                title = stringResource(R.string.no_liked_local_songs),
                subtitle = stringResource(R.string.switch_source_or_like)
            )
            StorageFilter.ONLINE -> LibraryEmptySpec(
                iconRes = R.drawable.rounded_favorite_24,
                title = stringResource(R.string.no_liked_cloud_songs),
                subtitle = stringResource(R.string.like_telegram_netease)
            )
        }

        LibraryTabId.FOLDERS -> LibraryEmptySpec(
            iconRes = R.drawable.ic_folder,
            title = stringResource(R.string.no_folders_found),
            subtitle = stringResource(R.string.folders_appear_here)
        )

        LibraryTabId.PLAYLISTS -> LibraryEmptySpec(
            iconRes = R.drawable.rounded_playlist_play_24,
            title = stringResource(R.string.no_playlists_yet),
            subtitle = stringResource(R.string.create_first_playlist)
        )
    }
}

@Composable
internal fun LibraryExpressiveEmptyState(
    tabId: LibraryTabId,
    storageFilter: StorageFilter,
    bottomBarHeight: Dp,
    modifier: Modifier = Modifier
) {
    val spec = remember(tabId, storageFilter) { libraryEmptySpec(tabId, storageFilter) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(
                start = 28.dp,
                end = 28.dp,
                bottom = bottomBarHeight + MiniPlayerHeight + 24.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.55f),
                tonalElevation = 2.dp
            ) {
                Box(
                    modifier = Modifier.size(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = spec.iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = spec.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = GoogleSansRounded,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = spec.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
