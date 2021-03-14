package game.peer2peer

import game.peer2peer.API.ClientService

object GameController {
    var gameState: GameState? = null

    fun createGameState(playersConnectors: Map<String, String>) {
        val numberOfPlayers: Int = playersConnectors.size
        val players: MutableMap<String, PlayerState> = mutableMapOf()
        val narratorAlias: String = playersConnectors.keys.first()
        val gameStage: GameStage = GameStage.SYNCHRONISATION
        val handSize = 4
        var cardsTaken = 0
        val descriptionTime = 180
        val guessTime = 120
        for ((alias, connector) in playersConnectors) {
            val playerState = PlayerState(
                connector = connector,
                hand = mutableListOf(),
                score = 0,
                status = PlayerStatus.ALIVE,
                cardToDescription = null,
                guess = null,
            )
            for (i in 0..handSize)
                playerState.hand.add(cardsTaken++)
            players[alias] = playerState
        }
        gameState = GameState(
            numberOfPlayers = numberOfPlayers,
            players = players,
            narratorAlias = narratorAlias,
            gameStage = gameStage,
            handSize = handSize,
            cardsTaken = cardsTaken,
            narratorDescription = null,
            descriptionDeadline = null,
            descriptionTime = descriptionTime,
            guessDeadline = null,
            guessTime = guessTime
        )
    }

    fun updateGameState(gameState: GameState, playerAlias: String) {
        val curGameState = checkNotNull(GameController.gameState)
        assert(curGameState.players[playerAlias]!!.status == PlayerStatus.SYNCHRONIZED
                && curGameState != gameState) { "Desynchronization" }
        GameController.gameState = gameState
    }

    fun reconnect(playerAlias: String, playerConnector: String) {
        val gameState = checkNotNull(gameState)
        assert(gameState.players[playerAlias]!!.status == PlayerStatus.DEAD) {
            "Only dead peer could reconnect"
        }
        gameState.players[playerAlias]!!.connector = playerConnector
        gameState.players[playerAlias]!!.status = PlayerStatus.ALIVE
    }

    fun updateNarratorDescription(
        description: String,
        cardIndex: Int,
    ) {
        val gameState = checkNotNull(gameState)
        if (gameState.gameStage == GameStage.ASSOCIATION)
            throw Exception() // toDo
        gameState.narratorDescription = description
        gameState.players[gameState.narratorAlias]!!.cardToDescription = cardIndex
    }

    fun updatePlayerCardToDescription(
        userAlias: String,
        cardIndex: Int,
    ) {
        val gameState = checkNotNull(gameState)
        if (gameState.gameStage == GameStage.ASSOCIATION)
            throw Exception() // toDo
        gameState.players[userAlias]!!.cardToDescription = cardIndex
    }


    fun updatePlayerGuess(
        userAlias: String,
        cardIndex: Int,
    ) {
        val gameState = checkNotNull(gameState)
        if (gameState.gameStage == GameStage.GUESS)
            throw Exception() // toDo
        gameState.players[userAlias]!!.guess = cardIndex
    }

    fun broadcastGameState(
        senderAlias: String,
    ) {
        val gameState = checkNotNull(gameState)
        for ((playerAlias, playerState) in gameState.players) {
            if (playerAlias == senderAlias || playerState.status == PlayerStatus.DEAD)
                continue
            val clientService: ClientService = ClientService.getInstance(playerState.connector)
            clientService.sendGameState(gameState)
        }
    }

    fun broadcastNarratorDescription(
        senderAlias: String,
        description: String,
        cardIndex: Int,
    ) {
        val gameState = checkNotNull(gameState)
        for ((playerAlias, playerState) in gameState.players) {
            if (playerAlias == senderAlias || playerState.status == PlayerStatus.DEAD)
                continue
            val clientService: ClientService = ClientService.getInstance(playerState.connector)
            clientService.sendNarratorDescription(description, cardIndex)
        }
    }

    fun broadcastCardToDescription(
        senderAlias: String,
        cardIndex: Int,
    ) {
        val gameState = checkNotNull(gameState)
        for ((playerAlias, playerState) in gameState.players) {
            if (playerAlias == senderAlias || playerState.status == PlayerStatus.DEAD)
                continue
            val clientService: ClientService = ClientService.getInstance(playerState.connector)
            clientService.sendCardToDescription(senderAlias, cardIndex)
        }
    }

    fun broadcastGuess(
        senderAlias: String,
        cardIndex: Int,
    ) {
        val gameState = checkNotNull(gameState)
        for ((playerAlias, playerState) in gameState.players) {
            if (playerAlias == senderAlias || playerState.status == PlayerStatus.DEAD)
                continue
            val clientService: ClientService = ClientService.getInstance(playerState.connector)
            clientService.sendGuess(senderAlias, cardIndex)
        }
    }
}