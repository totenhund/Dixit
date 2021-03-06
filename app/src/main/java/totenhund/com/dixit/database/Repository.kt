package totenhund.com.dixit.database

import androidx.lifecycle.LiveData

class Repository(private val cardDao: CardDao){

    val readAllCards: LiveData<List<Card>> = cardDao.getAllCards()

    fun insertCard(card: Card){
        cardDao.insertCard(card)
    }

}