package com.example.madlevel5task2.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.madlevel5task2.dao.GameBacklogDao
import com.example.madlevel5task2.database.GameBacklogDatabase
import com.example.madlevel5task2.model.Game

class GameBacklogRepository(context: Context) {

    private val gameBacklogDao: GameBacklogDao

    init {
       val database = GameBacklogDatabase.getDatabase(context)

       gameBacklogDao = database!!.gameBacklogDao()
    }

    fun getBacklog(): LiveData<List<Game>?> = gameBacklogDao.getBacklog()

    suspend fun addGame(game: Game) = gameBacklogDao.addGame(game)

    suspend fun removeGame(game: Game) = gameBacklogDao.removeGame(game)

    suspend fun clearBacklog() = gameBacklogDao.clearBacklog()
}
