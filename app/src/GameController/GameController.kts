interface GameController {
    fun getNarratorDescription(playerId: Int): String


    fun sendDescriptionToCard(
        playerId: Int,
        cardIndex: Int,
        description: String,
    )

    fun getCardsToDescription(playerId: Int): ArrayList<Int>


    fun sendCardToDescription(
        playerId: Int,
        cardIndex: Int,
        description: String,
        descriptionDeadline: Long,
    )

    fun getGuesses(playerId: Int): Map<Int, Int>

    fun sendGuess(playerId: Int, cardIndex: Int)

    fun getScores(): Map<Int, Int>
    fun getNarrator(): Int
    fun getHand(playerId: Int): ArrayList<Int>
    fun addCard(playerId: Int): Int
    fun deleteCard(playerId: Int, cardIndex: Int)
}