package com.example.madlevel5task2.ui

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.nonamer777.madlevel5task2.R
import com.nonamer777.madlevel5task2.databinding.ActivityMainBinding
import com.example.madlevel5task2.model.Game
import com.example.madlevel5task2.model.GameBacklogViewModel
import com.example.madlevel5task2.undo.undoListener.ClearBacklogUndoListener

class MainActivity : AppCompatActivity() {

    companion object {
        var actionBar: ActionBar? = null

        val gameBacklog: ArrayList<Game> = arrayListOf()
    }

    private lateinit var binding: ActivityMainBinding

    private val gameBacklogViewModel: GameBacklogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))

        Companion.actionBar = supportActionBar
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        // Hides the clear backlog icon whenever the User is adding a new game to the backlog.
        findNavController(R.id.nav_host_fragment)
            .addOnDestinationChangedListener { _, destination, _ ->

                when (destination.label == getString(R.string.fragment_label_add_game)) {
                    true -> menu.findItem(R.id.btn_clear_backlog)?.isVisible = false
                    else -> menu.findItem(R.id.btn_clear_backlog)?.isVisible = true
                }
            }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        // Navigates to the backlog whenever the user presses the 'back to home' icon.
        android.R.id.home -> {
            findNavController(R.id.nav_host_fragment)
                .navigate(R.id.action_addGameFragment_to_backlogFragment)

            true
        }
        // Clears the backlog whenever the User presses the 'clear backlog' icon.
        R.id.btn_clear_backlog -> {
            // Makes a copy of the current game backlog.
            val currentBacklog: ArrayList<Game> = arrayListOf()
            currentBacklog.addAll(gameBacklog)

            val snackbar = Snackbar.make(
                binding.root,
                "Successfully cleared backlog",
                Snackbar.LENGTH_LONG
            )

            // Makes it possible to undo the clearing of the backlog, for how long the snackbar is visible.
            snackbar.setAction(
                R.string.label_undo,
                ClearBacklogUndoListener(gameBacklogViewModel, currentBacklog)
            )
            snackbar.setActionTextColor(Color.rgb(63, 149, 255))

            gameBacklogViewModel.clearBacklog()
            snackbar.show()

            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
