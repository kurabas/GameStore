package com.example.madlevel5task2.undo.undoListener

import android.view.View
import com.example.madlevel5task2.model.Game
import com.example.madlevel5task2.model.GameBacklogViewModel
import java.text.SimpleDateFormat

/**
 * Adds back a complete backlog when a User undo's the clearing of a backlog.
 */
class ClearBacklogUndoListener(
    private val gameBacklogViewModel: GameBacklogViewModel,
    private val gameBacklog: List<Game>
): View.OnClickListener {

    override fun onClick(view: View) {

        gameBacklog.forEach {
            val dateFormatter = SimpleDateFormat("dd MM YYYY")

            val dateString = dateFormatter.format(it.releaseDate)
            val dateValues = dateString.split(' ')

            gameBacklogViewModel.addGame(
                it.title,
                it.platform,
                dateValues[2],
                dateValues[1],
                dateValues[0]
            )
        }
    }
}