package game.peer2peer.API

import com.googlecode.jsonrpc4j.JsonRpcService
import com.googlecode.jsonrpc4j.JsonRpcParam
import game.peer2peer.GameController
import game.peer2peer.GameState

@JsonRpcService("/API/")
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
        @JsonRpcParam(value = "description") description: String,
        @JsonRpcParam(value = "cardIndex") cardIndex: Int,
    ) {
        GameController.updateNarratorDescription(description, cardIndex)
    }

    fun receiveCardToDescription(
        @JsonRpcParam(value = "senderAlias") senderAlias: String,
        @JsonRpcParam(value = "cardIndex") cardIndex: Int,
    ) {
        GameController.updatePlayerCardToDescription(senderAlias, cardIndex)
    }

    fun receiveGuess(
        @JsonRpcParam(value = "senderAlias") senderAlias: String,
        @JsonRpcParam(value = "cardIndex") cardIndex: Int,
    ) {
        GameController.updatePlayerGuess(senderAlias, cardIndex)
    }
}