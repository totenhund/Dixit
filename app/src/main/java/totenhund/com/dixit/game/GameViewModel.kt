package totenhund.com.dixit.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import totenhund.com.dixit.database.Card
import totenhund.com.dixit.database.CardDatabase
import totenhund.com.dixit.database.Repository

class GameViewModel(application: Application): AndroidViewModel(application) {

    private val cardDao = CardDatabase.getInstance(application).cardDao()
    private val repository = Repository(cardDao)

    val readCards: LiveData<List<Card>> = repository.readAllCards

    fun insertCard(card: Card){
        viewModelScope.launch(IO) {
            repository.insertCard(card)
        }
    }

}