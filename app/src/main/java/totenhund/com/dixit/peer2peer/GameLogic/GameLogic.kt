package peer2peer.GameLogic

import game.peer2peer.GameController
import game.peer2peer.GameState
import peer2peer.GameController.GameStub

object GameLogic {
    var playerAlias: String
        get() {
            TODO()
        }
        set(value) {}
    private val gameController: GameController = GameController
    private val gameStub = GameStub
    var event: GameEvent = GameEvent.WAIT_START_ROUND

    fun sendDescriptionToTheCard(
        description: String,
        cardIndex: Int,
    ) {
        check(this.event == GameEvent.SEND_DESCRIPTION)
        val gameState: GameState = checkNotNull(this.gameController.gameState)
        check(gameState.narratorAlias == playerAlias)
        this.gameStub.broadcastNarratorDescription(
            description = description,
            cardIndex = cardIndex
        )
        this.event = GameEvent.WAIT_CARDS_TO_DESCRIPTION
    }

    fun sendCardToDescription(
        cardIndex: Int,
    ) {
        check(this.event == GameEvent.SEND_CARD_TO_DESCRIPTION)
        val gameState: GameState = checkNotNull(this.gameController.gameState)
        check(gameState.narratorAlias != playerAlias)
        this.gameStub.broadcastCardToDescription(
            cardIndex = cardIndex
        )
        this.event = GameEvent.WAIT_CARDS_TO_DESCRIPTION
    }

    fun sendGuess(
        cardIndex: Int,
    ) {
        check(this.event == GameEvent.SEND_GUESS)
        val gameState: GameState = checkNotNull(this.gameController.gameState)
        check(gameState.narratorAlias != playerAlias)
        this.gameStub.broadcastGuess(
            cardIndex = cardIndex
        )
        this.event = GameEvent.WAIT_GUESSES
    }

    fun startRound() {
        check(this.event == GameEvent.WAIT_START_ROUND)
        val gameState: GameState = checkNotNull(this.gameController.gameState)
        if (gameState.narratorAlias == playerAlias) {
            this.event = GameEvent.SEND_DESCRIPTION
        } else {
            this.event = GameEvent.WAIT_DESCRIPTION
        }
    }

    fun descriptionReceived() {
        check(this.event == GameEvent.WAIT_DESCRIPTION)
        val gameState: GameState = checkNotNull(this.gameController.gameState)
        check(gameState.narratorAlias != playerAlias)
        this.event = GameEvent.SEND_CARD_TO_DESCRIPTION
    }

    fun cardsToDescriptionReceived() {
        check(this.event == GameEvent.WAIT_CARDS_TO_DESCRIPTION)
        val gameState: GameState = checkNotNull(this.gameController.gameState)
        if (playerAlias == gameState.narratorAlias) {
            this.event = GameEvent.WAIT_GUESSES
        } else {
            this.event = GameEvent.SEND_GUESS
        }
    }

    fun guessesReceived() {
        check(this.event == GameEvent.WAIT_GUESSES)
        val gameState: GameState = checkNotNull(this.gameController.gameState)
        this.event = GameEvent.WAIT_START_ROUND
        this.gameStub.waitStartRound()
    }

    fun getGameState(): GameState {
        return this.gameController.gameState!!
    }
}