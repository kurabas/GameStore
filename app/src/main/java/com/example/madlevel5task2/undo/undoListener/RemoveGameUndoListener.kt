package com.example.madlevel5task2.undo.undoListener

import android.view.View
import com.example.madlevel5task2.model.Game
import com.example.madlevel5task2.model.GameBacklogViewModel
import java.text.SimpleDateFormat

/**
 * Adds back a single game to a backlog when a User undos removing a game from a backlog.
 */
class RemoveGameUndoListener(
    private val gameBacklogViewModel: GameBacklogViewModel,
    private val game: Game
): View.OnClickListener {

    override fun onClick(view: View) {
        val dateFormatter = SimpleDateFormat("dd MM YYYY")

        val dateString = dateFormatter.format(game.releaseDate)
        val dateValues = dateString.split(' ')

        gameBacklogViewModel.addGame(
            game.title,
            game.platform,
            dateValues[2],
            dateValues[1],
            dateValues[0]
        )
    }
}