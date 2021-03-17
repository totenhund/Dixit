package totenhund.com.dixit.peer2peer.GameLogic

import game.peer2peer.GameController
import game.peer2peer.GameState
import peer2peer.GameController.GameStub
import java.io.Serializable

class GameLogic private constructor(): Serializable{
    var playerAlias: String = "playerA"
    private val gameController: GameController = GameController
    private val gameStub = GameStub
    var event: GameEvent = GameEvent.WAIT_START_ROUND

    companion object {
        private val instance: GameLogic by lazy(LazyThreadSafetyMode.PUBLICATION) { GameLogic() }

        fun instance(): GameLogic{
            return instance
        }
    }

    fun sendDescriptionToTheCard(
        description: String,
        cardIndex: Int,
    ) {
        check(event == GameEvent.SEND_DESCRIPTION)
        val gameState: GameState = checkNotNull(gameController.gameState)
        check(gameState.narratorAlias == playerAlias)
        gameStub.broadcastNarratorDescription(
            description = description,
            cardIndex = cardIndex
        )
        event = GameEvent.WAIT_CARDS_TO_DESCRIPTION
    }

    fun sendCardToDescription(
        cardIndex: Int,
    ) {
        check(event == GameEvent.SEND_CARD_TO_DESCRIPTION)
        val gameState: GameState = checkNotNull(gameController.gameState)
        check(gameState.narratorAlias != playerAlias)
        gameStub.broadcastCardToDescription(
            cardIndex = cardIndex
        )
        event = GameEvent.WAIT_CARDS_TO_DESCRIPTION
    }

    fun sendGuess(
        cardIndex: Int,
    ) {
        check(event == GameEvent.SEND_GUESS)
        val gameState: GameState = checkNotNull(gameController.gameState)
        check(gameState.narratorAlias != playerAlias)
        gameStub.broadcastGuess(
            cardIndex = cardIndex
        )
        event = GameEvent.WAIT_GUESSES
    }

    fun startRound() {
        check(event == GameEvent.WAIT_START_ROUND)
        val gameState: GameState = checkNotNull(gameController.gameState)
        if (gameState.narratorAlias == playerAlias) {
            event = GameEvent.SEND_DESCRIPTION
        } else {
            event = GameEvent.WAIT_DESCRIPTION
        }
    }

    fun descriptionReceived() {
        check(event == GameEvent.WAIT_DESCRIPTION)
        val gameState: GameState = checkNotNull(gameController.gameState)
        check(gameState.narratorAlias != playerAlias)
        event = GameEvent.SEND_CARD_TO_DESCRIPTION
    }

    fun cardsToDescriptionReceived() {
        check(event == GameEvent.WAIT_CARDS_TO_DESCRIPTION)
        val gameState: GameState = checkNotNull(gameController.gameState)
        if (playerAlias == gameState.narratorAlias) {
            event = GameEvent.WAIT_GUESSES
        } else {
            event = GameEvent.SEND_GUESS
        }
    }

    fun guessesReceived() {
        check(event == GameEvent.WAIT_GUESSES)
        val gameState: GameState = checkNotNull(gameController.gameState)
        event = GameEvent.WAIT_START_ROUND
        gameStub.waitStartRound()
    }

    fun getGameState(): GameState {
        return gameController.gameState!!
    }
}