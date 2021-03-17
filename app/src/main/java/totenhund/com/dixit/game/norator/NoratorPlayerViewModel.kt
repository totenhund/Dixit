package totenhund.com.dixit.game.norator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import totenhund.com.dixit.peer2peer.GameLogic.GameLogic

class NoratorPlayerViewModel(gameLogic: GameLogic): ViewModel() {

    private val _gameLogic = MutableLiveData<GameLogic>()
    val gameLogic: LiveData<GameLogic>
        get() = _gameLogic

    init{
        _gameLogic.value = gameLogic
    }

}