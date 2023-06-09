package com.example.madlevel5task2.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.madlevel5task2.repository.GameBacklogRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class GameBacklogViewModel(application: Application): AndroidViewModel(application) {

    private val gameBacklogRepo = GameBacklogRepository(application.applicationContext)

    private val mainScope = CoroutineScope(Dispatchers.Main)

    val backlog = gameBacklogRepo.getBacklog()

    val error = MutableLiveData<String>()

    val success = MutableLiveData<Boolean>()

    /** Adds a game to the backlog. */
    fun addGame(title: String, platform: String, year: String, month: String, dayOfTheMonth: String) {
        // Checks whether the release date parameters are valid.
        if (!isValidDate(year, month, dayOfTheMonth)) {
            success.value = false

            return
        }
        val calender = Calendar.getInstance()

        /*
         * Sets the calender to a given release date. `month.toInt() - 1` is necessary
         * because months are 0-11 based.
         */
        calender.set(year.toInt(), month.toInt() - 1, dayOfTheMonth.toInt())

        val game = Game(null, title, platform, calender.time)

        // Checks if the other parameters of the game are also valid.
        if (!isValidGame(game)) {
            success.value = false

            return
        }

        // Adds the game to the backlog.
        mainScope.launch {
            withContext(Dispatchers.IO) { gameBacklogRepo.addGame(game) }

            success.value = true
        }
    }

    /** Removes a single gme from the backlog. */
    fun removeGame(game: Game) = mainScope.launch {
        withContext(Dispatchers.IO) { gameBacklogRepo.removeGame(game) }

        success.value = true
    }

    /** Removes all games from the backlog. */
    fun clearBacklog() = mainScope.launch {
        withContext(Dispatchers.IO) { gameBacklogRepo.clearBacklog() }

        success.value = true
    }

    /** Checks whether the provided date values are valid. */
    private fun isValidDate(yearStr: String, monthStr: String, dayOfTheMonthStr: String): Boolean {
        // Checks if the provided values have not been forgotten to be filled in.
        if (yearStr.isBlank() || monthStr.isBlank() || dayOfTheMonthStr.isBlank()) {
            error.value = "Please fill in a valid release date."

            return false
        }
        // Converts the provided string values to integers, so that they're more easily to check.
        val year = yearStr.toInt()
        val month = monthStr.toInt()
        val dayOfTheMonth = dayOfTheMonthStr.toInt()

        val calender = GregorianCalendar.getInstance()
        val leapYear = isLeapYear(calender, year)

        return when {
            /*
             * Checks if the provided year is higher or equal to release year of the first game.
             *
             * @source https://en.wikipedia.org/wiki/History_of_video_games
             */
            year < 1950 -> {
                error.value = "Please fill in a valid year."

                false
            }
            // Checks whether the provided month is valid.
            month !in 1..12 -> {
                error.value = "Please fill in a valid month."

                false
            }
            // Checks whether the provided day of the month is valid.
            !isValidDayOfTheMonth(leapYear, month, dayOfTheMonth) -> {
                error.value = "Please fill in a valid day."

                false
            }
            else -> true
        }
    }

    /** Checks whether the provided year was / is a leap year. */
    private fun isLeapYear(calendar: Calendar, year: Int): Boolean {
        calendar.set(Calendar.YEAR, year)

        return calendar.getMaximum(Calendar.DAY_OF_YEAR) > 365
    }

    /**
     * Checks if the provided day of the month is valid against the actual number of days
     * that could occur in that month.
     */
    private fun isValidDayOfTheMonth(leapYear: Boolean, month: Int, dayOfTheMonth: Int): Boolean = when (month) {
        2 -> (leapYear && dayOfTheMonth in 1..29) || (!leapYear && dayOfTheMonth in 1..28)
        1, 3, 5, 7, 8, 10, 12 -> dayOfTheMonth in 1..31
        4, 6, 9, 11 -> dayOfTheMonth in 1..30
        else -> false
    }

    /** Checks whether the game's title and platform are valid. */
    private fun isValidGame(game: Game): Boolean = when {
        game.title.isBlank() -> {
            error.value = "Please fill in the title of the game."

            false
        }
        game.platform.isBlank() -> {
            error.value = "Please fill in on what platform you'd like to play the game."

            false
        }
        else -> true
    }
}