import main.GameStage

class PeerGameController private constructor(numberOfPlayers: Int, peersIps: MutableMap<Int, Int>) : GameController {
    val numberOfPlayers: Int = numberOfPlayers
        get() = field
    val playersHands: MutableMap<Int, MutableList<Int>> = mutableMapOf()
    val playersScores: MutableMap<Int, Int> = mutableMapOf()
    val handSize = 4
        get() = field
    private var narratorId: Int = 1
    private var cardsTaken = numberOfPlayers * handSize
    var gameStage = GameStage.ASSOCIATION
        get() = field
        set(value) {
            field = value
        }

    //time limits
    val descriptionTime = 300
        get() = field
    val guessTime = 120
        get() = field
    var guessDeadline: Long? = null
    var descriptionDeadline: Long? = null

    //Peer related
    private val peersIps: Map<Int, Int> = peersIps
    private var cardsToDescription: MutableMap<Int, Int> = mutableMapOf()
    private var guesses: MutableMap<Int, Int> = mutableMapOf()
    private var narratorDescription: String = ""

    //toDo Exceptions
    override fun getNarratorDescription(playerId: Int): String {
        //toDo wait till descriptionDeadline == null
        return this.narratorDescription
    }

    override fun sendDescriptionToCard(
        playerId: Int,
        cardIndex: Int,
        description: String,
    ) {
        this.descriptionDeadline = System.currentTimeMillis() + this.descriptionTime
        for ((peerId, peerIp) in this.peersIps.entries) {
            when (playerId != peerId) {
                //toDo send to peerIp: description, cardIndex and deadline
            }
        }
        this.guessDeadline = this.descriptionDeadline!! + this.guessTime
    }

    fun receiveNarratorDescription(
        peerId: Int,
        cardIndex: Int,
        description: String,
        descriptionDeadline: Long,
    ) {
        this.cardsToDescription[peerId] = cardIndex
        this.narratorDescription = description
        this.gameStage = GameStage.ASSOCIATION
        this.descriptionDeadline = descriptionDeadline
        this.guessDeadline = descriptionDeadline + this.guessTime
    }

    override fun getCardsToDescription(playerId: Int): ArrayList<Int> {
        //toDo wait until descriptionDeadline
        for (peerId in this.peersIps.keys) {
            if (this.cardsToDescription[peerId] == null) {
                val firstCard: Int = this.playersHands[peerId]?.get(0)!!
                this.cardsToDescription[peerId] = firstCard
                //toDo mark as dead
            }
            this.deleteCard(peerId, this.cardsToDescription[peerId]!!)
        }
        this.cardsToDescription.values
        val cardsToShow: ArrayList<Int> = this.cardsToDescription.values.shuffled() as ArrayList<Int>
        this.cardsToDescription.clear()
        this.gameStage = GameStage.GUESS
        return cardsToShow
    }

    override fun sendCardToDescription(
        playerId: Int,
        cardIndex: Int,
        description: String,
        descriptionDeadline: Long,
    ){
        for ((peerId, peerIp) in this.peersIps.entries) {
            when (playerId != peerId) {
                //toDo send to peerIp: description, cardIndex
            }
        }
    }

    fun receiveCardToDescription(playerId: Int, peerId: Int, cardIndex: Int) {
        if (this.gameStage.ordinal > GameStage.ASSOCIATION.ordinal) {
            //toDo send that you're late
        } else {
            this.cardsToDescription[peerId] = cardIndex
            //toDo send gotcha
        }
    }

    override fun getGuesses(playerId: Int): Map<Int, Int> {
        //toDo wait until guessDeadline
        this.gameStage = GameStage.EVALUATION
        return this.guesses
    }

    override fun sendGuess(playerId: Int, cardIndex: Int) {
        for ((peerId, peerIp) in this.peersIps.entries) {
            when (playerId != peerId) {
                //toDo send to peerIp: description, cardIndex
            }
        }
    }

    override fun getScores(): Map<Int, Int> {
        TODO("Not yet implemented")
    }

    override fun getNarrator(): Int {
        TODO("Not yet implemented")
    }

    override fun getHand(playerId: Int): ArrayList<Int> {
        TODO("Not yet implemented")
    }

    override fun addCard(playerId: Int): Int {
        TODO("Not yet implemented")
    }

    override fun deleteCard(playerId: Int, cardIndex: Int) {
        TODO("Not yet implemented")
    }

    fun receiveGuess(playerId: Int, peerId: Int, cardIndex: Int) {
        if (this.gameStage.ordinal > GameStage.GUESS.ordinal) {
            //toDo send that you're late
        } else {
            this.guesses[peerId] = cardIndex
            //toDo send gotcha
        }
    }
}