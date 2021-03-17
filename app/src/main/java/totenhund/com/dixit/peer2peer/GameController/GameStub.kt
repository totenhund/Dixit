package peer2peer.GameController

import game.peer2peer.GameController
import game.peer2peer.GameState
import totenhund.com.dixit.peer2peer.GameLogic.GameLogic
import kotlin.random.Random

object GameStub {
    val gameController = GameController
    val playerAlias = GameLogic.getInstance().playerAlias

    init {
//        this.waitStartRound()
    }

    fun broadcastNarratorDescription(
        description: String,
        cardIndex: Int,
    ) {
        this.gameController.updateNarratorDescription(description, cardIndex)
    }

    fun broadcastCardToDescription(
        cardIndex: Int,
    ) {
        this.gameController.updatePlayerCardToDescription(playerAlias, cardIndex)
    }

    fun broadcastGuess(
        cardIndex: Int,
    ) {
        this.gameController.updatePlayerGuess(playerAlias, cardIndex)
    }

    fun waitCardsToDescription() {
        val gameState: GameState = checkNotNull(this.gameController.gameState)
        for ((alias, playerState) in gameState.players) {
            val cardIndex = playerState.hand[0]
            this.gameController.updatePlayerCardToDescription(alias, cardIndex)
        }
    }

    fun waitGuesses() {
        val gameState: GameState = checkNotNull(this.gameController.gameState)
        for ((alias, playerState) in gameState.players) {
            val guess: Int = Random(System.currentTimeMillis()).nextInt(gameState.numberOfPlayers)
            val cardIndex: Int = gameState.players.toList()[guess].second.cardToDescription!!
            this.gameController.updatePlayerGuess(alias, cardIndex)
        }
    }

    fun waitStartRound() {
        val gameState: GameState = checkNotNull(this.gameController.gameState)
        for ((alias, playerState) in gameState.players) {
            this.gameController.updateGameState(gameState, alias)
        }
    }
}