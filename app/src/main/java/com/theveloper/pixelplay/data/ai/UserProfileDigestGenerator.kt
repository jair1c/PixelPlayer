import com.theveloper.pixelplay.data.database.EngagementDao
import com.theveloper.pixelplay.data.model.Song
import com.theveloper.pixelplay.data.stats.PlaybackStatsRepository
import com.theveloper.pixelplay.data.stats.StatsTimeRange
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileDigestGenerator @Inject constructor(
    private val engagementDao: EngagementDao,
    private val statsRepository: PlaybackStatsRepository
) {
    /**
     * Computes a highly condensed representation of the user's listening profile.
     * Uses a compact key-value format to minimize token consumption while maximizing signal.
     */
    suspend fun generateDigest(allSongs: List<Song>): String {
        val summary = statsRepository.loadSummary(StatsTimeRange.ALL_TIME, allSongs)
        
        val sb = StringBuilder()
        sb.append("USER_VIBE_PROFILE_V2\n")
        sb.append("TOP_GENRES: ${summary.topGenres.take(5).joinToString(",") { it.genre }}\n")
        sb.append("TOP_ARTISTS: ${summary.topArtists.take(5).joinToString(",") { it.artist }}\n")
        
        // Peak listening times
        summary.dayListeningDistribution?.let { dist ->
            val peakBuckets = dist.buckets.sortedByDescending { it.totalDurationMs }.take(3)
            val peaks = peakBuckets.joinToString(",") { "${it.startMinute/60}h" }
            sb.append("PEAK_HOURS: $peaks\n")
        }
        
        // Session behavior
        val avgSessionMin = summary.averageSessionDurationMs / (1000 * 60)
        sb.append("BEHAVIOR: AvgSession=${avgSessionMin}m, TotalSessions=${summary.totalSessions}, Streak=${summary.longestStreakDays}d\n")
        
        // Favorites vs Variety
        val varietyRatio = if (summary.totalPlayCount > 0) (summary.uniqueSongs.toDouble() / summary.totalPlayCount) else 0.0
        sb.append("VARIETY_SCORE: ${"%.2f".format(varietyRatio)} (1.0=pure variety, 0.1=repeater)\n")
        
        // Recent "Vibe"
        val recentTracks = summary.topSongs.take(5).joinToString(" | ") { "${it.title}-${it.artist}" }
        sb.append("CURRENT_FAVORITES: $recentTracks\n")
        
        return sb.toString()
    }
}
