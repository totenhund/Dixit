package main

class GameLogic private constructor(
    private val playerId: Int,
    numberOfPlayers: Int,
) {
    val gameState: GameState = GameState.getInstance(numberOfPlayers)
        get() = field
    var playerHand: ArrayList<Int> = arrayListOf()

    fun sendDescriptionToCard(cardNumber: Int, description: String): ArrayList<Int> {
        //toDo broadcast association and wait for response
        val cards: ArrayList<Int> = arrayListOf()
        cards.add(cardNumber) // add own card
        cards.shuffle()
        return cards
    }

    fun sendCardToDescription(cardNumber: Int) {
        //toDo Game Controller send association
    }

    fun getGuesses(): Map<Int, Int> {
        //toDo Game Controller give guesses
        val guesses: Map<Int, Int> = mapOf() // temp
        gameState.gameStage = GameStage.EVALUATION
        return guesses
    }

    fun sendGuess(cardNumber: Int) {
        //toDo Game Controller send guess
    }

    fun updateScores(): Map<Int, Int> {
        // toDo ask to update scores of the game
        val scores: Map<Int, Int> = mapOf()
        if (isNarrator()) {
            finishTurn()
        }
        return scores
    }

    fun isNarrator(): Boolean {
        return gameState.isNarrator(playerId)
    }

    private fun finishTurn() {
        //toDo
    }

    private fun getCards() {

    }
}