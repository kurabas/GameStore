package com.example.madlevel5task2.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nonamer777.madlevel5task2.R
import com.nonamer777.madlevel5task2.databinding.ItemGameBinding
import com.example.madlevel5task2.model.Game
import java.text.SimpleDateFormat
import java.util.*

class GameAdapter(private val gameBacklog: List<Game>): RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val binding = ItemGameBinding.bind(itemView)

        fun dataBind(game: Game) {
            val dateFormatter = SimpleDateFormat("dd MMMM YYYY", Locale.getDefault())

            binding.labelGameTitle.text = game.title
            binding.labelGamePlatform.text = game.platform
            binding.labelGameReleaseDate.text = "Release: ${dateFormatter.format(game.releaseDate)}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_game, parent, false))

    override fun getItemCount(): Int = gameBacklog.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.dataBind(gameBacklog[position])
}
