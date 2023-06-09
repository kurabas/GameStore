package com.example.madlevel5task2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nonamer777.madlevel5task2.R
import com.nonamer777.madlevel5task2.databinding.FragmentAddGameBinding
import com.example.madlevel5task2.model.GameBacklogViewModel
import com.example.madlevel5task2.undo.viewBinding.IViewBindingHolder
import com.example.madlevel5task2.undo.viewBinding.ViewBindingHolder

/**
 * A [Fragment] subclass where a User can add a game to the backlog.
 */
class AddGameFragment : Fragment(), IViewBindingHolder<FragmentAddGameBinding> by ViewBindingHolder() {

    private val gameBacklogViewModel: GameBacklogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = initBinding(FragmentAddGameBinding.inflate(layoutInflater), this) {

        // Updates the actionbar according to the fragment's needs.
        MainActivity.actionBar?.title = getString(R.string.fragment_title_add_game)
        MainActivity.actionBar?.setHomeButtonEnabled(true)
        MainActivity.actionBar?.setDisplayHomeAsUpEnabled(true)

        btnSaveGame.setOnClickListener { onAddGame() }

        observeBacklog()
    }

    /** Handles adding a game to the backlog. */
    private fun onAddGame() = requireBinding {
        gameBacklogViewModel.addGame(
            binding?.inputGameTitle?.editableText.toString(),
            binding?.inputGamePlatform?.editableText.toString(),
            binding?.inputYear?.editableText.toString(),
            binding?.inputMonth?.editableText.toString(),
            binding?.inputDayOfTheMonth?.editableText.toString()
        )
    }

    /** Observes the results of adding a game to the backlog */
    private fun observeBacklog() {
        // Shows an error message in a Toast whenever one comes up.
        gameBacklogViewModel.error.observe(viewLifecycleOwner, { message ->
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        })

        // Only navigates back to the backlog fragment when the addition of the game was successful.
        gameBacklogViewModel.success.observe(viewLifecycleOwner, { success -> run {
            if (success) findNavController().navigate(R.id.action_addGameFragment_to_backlogFragment)
        }})
    }
}
