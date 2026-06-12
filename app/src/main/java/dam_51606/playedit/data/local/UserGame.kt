package dam_51606.playedit.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

/**
 * Represents a game in the user's library,
 * including the score and review they left.
 */
@Entity(tableName="user_games")
data class UserGame (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // Game data
    val gameId: Int, // RawG API game ID
    val genres: List<String> = emptyList(), // game genre names for stats
    val userId: String,
    //Game status
    val status: GameStatus = GameStatus.NOT_STARTED,
    val isFavorite: Boolean = false,
    // Start and end dates
    val startDate: Long? = null,
    val endDate: Long? = null,
    // Evaluation
    val score: Int? = null,
    val review: String? = null,
)

// Type converters for genre data
@TypeConverter
fun fromGenreList(genres: List<String>): String = genres.joinToString(",")
@TypeConverter
fun toGenreList(value: String): List<String> = if (value.isBlank()) emptyList() else value.split(",")