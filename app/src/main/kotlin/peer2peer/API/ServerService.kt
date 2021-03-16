package game.peer2peer.API

import game.peer2peer.GameController
import game.peer2peer.GameState

class ServerService(val playerAlias: String) {
    var gameController: GameController = GameController

    fun createGameState(playersConnectors: Map<String, String>) {
        GameController.createGameState(playersConnectors)
    }

    fun synchronize(gameState: GameState) {
        GameController.updateGameState(gameState, playerAlias)
    }

    fun reconnect(playerAlias: String, playerConnector: String) {
        GameController.gameState
    }

    fun ping() {}

    fun receiveNarratorDescription(
        description: String,
        cardIndex: Int,
    ) {
        GameController.updateNarratorDescription(description, cardIndex)
    }

    fun receiveCardToDescription(
        senderAlias: String,
        cardIndex: Int,
    ) {
        GameController.updatePlayerCardToDescription(senderAlias, cardIndex)
    }

    fun receiveGuess(
        senderAlias: String,
        cardIndex: Int,
    ) {
        GameController.updatePlayerGuess(senderAlias, cardIndex)
    }
}