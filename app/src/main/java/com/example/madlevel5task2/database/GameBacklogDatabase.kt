package com.example.madlevel5task2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.madlevel5task2.dao.GameBacklogDao
import com.example.madlevel5task2.model.Converters
import com.example.madlevel5task2.model.Game

@TypeConverters(Converters::class)
@Database(entities = [Game::class], version = 1, exportSchema = false)
abstract class GameBacklogDatabase: RoomDatabase() {

    companion object {

        private const val DATABASE_NAME = "GAME_BACKLOG_DATABASE"

        @Volatile
        private var INSTANCE: GameBacklogDatabase? = null

        fun getDatabase(context: Context): GameBacklogDatabase? {

            if (INSTANCE == null) {
                synchronized(GameBacklogDatabase::class.java) {

                    if (INSTANCE == null) {
                        INSTANCE = Room
                            .databaseBuilder(
                                context.applicationContext,
                                GameBacklogDatabase::class.java,
                                DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                        .build()
                    }
                }
            }
            return INSTANCE
        }
    }

    abstract fun gameBacklogDao(): GameBacklogDao
}
