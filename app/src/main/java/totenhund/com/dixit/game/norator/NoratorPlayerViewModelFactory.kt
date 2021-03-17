package totenhund.com.dixit.game.norator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import totenhund.com.dixit.peer2peer.GameLogic.GameLogic

/*
* Factory for ScoreFragment
* */
class NoratorPlayerViewModelFactory(private val gameLogic: GameLogic) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoratorPlayerViewModel::class.java)) {
            return NoratorPlayerViewModel(gameLogic) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}