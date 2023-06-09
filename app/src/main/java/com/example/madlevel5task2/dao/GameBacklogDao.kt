package com.example.madlevel5task2.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.madlevel5task2.model.Game

@Dao
interface GameBacklogDao {

    @Query("select * from Game")
    fun getBacklog(): LiveData<List<Game>?>

    @Insert
    suspend fun addGame(game: Game)

    @Delete
    suspend fun removeGame(game: Game)

    @Query("delete from Game")
    suspend fun clearBacklog()
}
