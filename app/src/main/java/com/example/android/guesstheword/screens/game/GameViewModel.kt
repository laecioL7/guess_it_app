package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 60000L
    }

    lateinit var timer : CountDownTimer;

    private val _currentTime = MutableLiveData<Long>()
    val currentTime : LiveData<Long>
    get() = _currentTime

    // The String version of the current time
    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    // The current word
    private val _word = MutableLiveData<String>()
    //versão apenas de leitura
    val word: LiveData<String>
    get() = _word

    // The current score
     private val _score = MutableLiveData<Int>()
    //versão apenas de leitura
    val score: LiveData<Int>
        get() = _score

    //evento
    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished : LiveData<Boolean>
        get() = _eventGameFinished

    // The list of words - the front of the list is the next word to guess
     private lateinit var wordList: MutableList<String>

        init {
            Log.i("GameViewModel", "GameViewModel Created")
            resetList()
            nextWord()

            _currentTime.value = 0
            _score.value = 0
            _eventGameFinished.value = false

            //Inicia o timer do jogo:
            timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

                override fun onTick(millisUntilFinished: Long) {
                    // TODO implement what should happen each tick of the timer
                    _currentTime.value = _currentTime.value?.plus(1)
                }

                override fun onFinish() {
                    // TODO implement what should happen when the timer finishes
                    _currentTime.value = 0
                }
            }

            timer.start()
        }

    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "GameViewModel destroyed")
        timer.cancel()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            _eventGameFinished.value = true
            _currentTime.value = 0
        } else {
            _word.value = wordList.removeAt(0)
        }
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    fun onGameFinishedComplete()
    {
        _eventGameFinished.value = true
    }
}