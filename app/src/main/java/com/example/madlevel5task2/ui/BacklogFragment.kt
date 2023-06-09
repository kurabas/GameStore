package com.example.madlevel5task2.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.nonamer777.madlevel5task2.R
import com.nonamer777.madlevel5task2.databinding.FragmentBacklogBinding
import com.example.madlevel5task2.model.Game
import com.example.madlevel5task2.model.GameBacklogViewModel
import com.example.madlevel5task2.undo.undoListener.RemoveGameUndoListener
import com.example.madlevel5task2.undo.viewBinding.IViewBindingHolder
import com.example.madlevel5task2.undo.viewBinding.ViewBindingHolder

/**
 * A [Fragment] subclass that shows the game backlog.
 */
class BacklogFragment: Fragment(), IViewBindingHolder<FragmentBacklogBinding> by ViewBindingHolder() {

    private val gameBacklogViewModel: GameBacklogViewModel by viewModels()

    private val gameAdapter = GameAdapter(MainActivity.gameBacklog)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = initBinding(
        FragmentBacklogBinding.inflate(layoutInflater),
        this
    ) {

        // Updates the actionbar according to the fragment's needs.
        MainActivity.actionBar?.title = getString(R.string.fragment_title_backlog)
        MainActivity.actionBar?.setHomeButtonEnabled(false)
        MainActivity.actionBar?.setDisplayHomeAsUpEnabled(false)

        // Initializes the recycler view.
        gameBacklogRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        gameBacklogRecyclerView.adapter = gameAdapter

        // Handles a touch gestures on the individual game items in the backlog.
        createItemTouchHelper().attachToRecyclerView(gameBacklogRecyclerView)

        btnAddGame.setOnClickListener {
            Navigation.findNavController(root)
                .navigate(R.id.action_backlogFragment_to_addGameFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeBacklogResults()
    }

    /** Updates the game backlog whenever a change is detected. */
    private fun observeBacklogResults() {
        gameBacklogViewModel.backlog.observe(viewLifecycleOwner, { gameBacklog -> gameBacklog?.let {

            MainActivity.gameBacklog.clear()
            MainActivity.gameBacklog.addAll(gameBacklog)
            MainActivity.gameBacklog.sortWith(compareBy { it.releaseDate })

            gameAdapter.notifyDataSetChanged()
        }})
    }

    /** Handles touch gestures on an item in the game backlog recycler view. */
    private fun createItemTouchHelper(): ItemTouchHelper {
        val callback = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Disables dragging an item up - down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            // Handles removing a game from the backlog when an item is swiped left.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val toBeDeletedGame: Game = MainActivity.gameBacklog[viewHolder.adapterPosition]
                val snackbar = binding?.root?.let {
                    Snackbar.make(
                        it,
                        "Successfully remove game from the backlog",
                        Snackbar.LENGTH_LONG
                    )
                }

                // Makes the removing of a game from the backlog undoable.
                snackbar?.setAction(
                    R.string.label_undo,
                    RemoveGameUndoListener(gameBacklogViewModel, toBeDeletedGame)
                )
                snackbar?.setActionTextColor(Color.rgb(63, 149, 255))

                gameBacklogViewModel.removeGame(toBeDeletedGame)

                snackbar?.show()
            }
        }
        return ItemTouchHelper(callback)
    }
}
