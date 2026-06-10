package dam_51606.playedit.data.local

import androidx.room.TypeConverter

// Converters for the game status (for storage in Room DB)
@TypeConverter
fun fromGameStatus(status: GameStatus): String = status.name

@TypeConverter
fun toGameStatus(value: String): GameStatus = GameStatus.valueOf(value)