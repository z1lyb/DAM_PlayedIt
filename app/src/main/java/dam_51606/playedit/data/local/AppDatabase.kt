package dam_51606.playedit.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dam_51606.playedit.data.local.dao.UserGameDAO

@Database(
    entities = [UserGame::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userGameDao(): UserGameDAO

    companion object {
        @Volatile private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "playedit_db")
                    .build().also {INSTANCE = it}
            }
    }
}