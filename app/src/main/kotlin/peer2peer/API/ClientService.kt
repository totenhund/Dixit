package game.peer2peer.API

import game.peer2peer.GameState
import java.net.URL

class ClientService private constructor(connector: String) {
    private val client = JsonRpcHttpClient(URL(connector))

    companion object {
        private val INSTANCES: MutableMap<String, ClientService> = mutableMapOf()

        fun getInstance(connector: String): ClientService {
            if (INSTANCES[connector] == null) {
                INSTANCES[connector] = ClientService(connector)
            }
            return INSTANCES[connector]!!
        }
    }

    fun sendGameState(gameState: GameState) {
        client.invoke("synchronize", listOf(gameState))
    }

    fun reconnect(senderAlias: String, senderConnector: String) {
        client.invoke("reconnect", listOf(senderAlias, senderConnector))
    }

    fun ping() {
        client.invoke("ping", null)
    }

    fun sendNarratorDescription(description: String, cardIndex: Int) {
        client.invoke("receiveNarratorDescription",
            listOf(description, cardIndex))
    }

    fun sendCardToDescription(senderAlias: String, cardIndex: Int) {
        client.invoke("receiveNarratorDescription",
            listOf(senderAlias, cardIndex))
    }

    fun sendGuess(senderAlias: String, cardIndex: Int) {
        client.invoke("receiveNarratorDescription",
            listOf(senderAlias, cardIndex))
    }

}