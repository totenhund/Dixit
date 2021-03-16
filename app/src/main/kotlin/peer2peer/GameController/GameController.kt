package game.peer2peer

import game.peer2peer.API.ClientService
import peer2peer.GameLogic.GameLogic

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
        this.gameState = GameState(
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
        val curGameState: GameState = checkNotNull(this.gameState)
        if (gameState.gameStage != GameStage.SYNCHRONISATION)
            throw Exception()
        check(
            curGameState.players[playerAlias]!!.status == PlayerStatus.SYNCHRONIZED
                    && curGameState != gameState
        ) { "Desynchronization" }
        if (this.gameState!!.players[playerAlias]!!.status != PlayerStatus.SYNCHRONIZED) {
            this.gameState = gameState
            this.gameState!!.players[playerAlias]!!.status = PlayerStatus.SYNCHRONIZED
            this.gameState!!.gameStage = GameStage.ASSOCIATION
            val gameLogic = GameLogic
            gameLogic.startRound()
        }
    }

    fun reconnect(playerAlias: String, playerConnector: String) {
        val gameState: GameState = checkNotNull(this.gameState)
        check(gameState.players[playerAlias]!!.status == PlayerStatus.DEAD) {
            "Only dead peer could reconnect"
        }
        gameState.players[playerAlias]!!.connector = playerConnector
        gameState.players[playerAlias]!!.status = PlayerStatus.ALIVE
    }

    fun updateNarratorDescription(
        description: String,
        cardIndex: Int,
    ) {
        val gameState: GameState = checkNotNull(this.gameState)
        if (gameState.gameStage == GameStage.ASSOCIATION)
            throw Exception() // toDo
        gameState.narratorDescription = description
        gameState.players[gameState.narratorAlias]!!.cardToDescription = cardIndex
        val gameLogic = GameLogic
        gameLogic.descriptionReceived()
    }

    fun updatePlayerCardToDescription(
        userAlias: String,
        cardIndex: Int,
    ) {
        val gameState: GameState = checkNotNull(this.gameState)
        if (gameState.gameStage == GameStage.ASSOCIATION)
            throw Exception() // toDo
        gameState.players[userAlias]!!.cardToDescription = cardIndex
        var changeState: Boolean = true
        for ((alias, playerState) in gameState.players) {
            if (playerState.cardToDescription == null) {
                changeState = false;
            }
        }
        if (changeState) {
            gameState.gameStage = GameStage.GUESS
            val gameLogic = GameLogic
            gameLogic.cardsToDescriptionReceived()
        }
    }


    fun updatePlayerGuess(
        userAlias: String,
        cardIndex: Int,
    ) {
        val gameState: GameState = checkNotNull(this.gameState)
        if (gameState.gameStage == GameStage.GUESS)
            throw Exception() // toDo
        gameState.players[userAlias]!!.guess = cardIndex
        var changeState: Boolean = true
        for ((alias, playerState) in gameState.players) {
            if (alias == gameState.narratorAlias)
                continue
            if (playerState.guess == null) {
                changeState = false;
            }
        }
        if (changeState) {
            this.calculateScores()
            gameState.gameStage = GameStage.SYNCHRONISATION
            val gameLogic = GameLogic
            gameLogic.guessesReceived()
        }
    }

    fun broadcastGameState(
        senderAlias: String,
    ) {
        val gameState: GameState = checkNotNull(this.gameState)
        if (gameState.gameStage != GameStage.SYNCHRONISATION)
            throw Exception() // toDO
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
        val gameState = checkNotNull(this.gameState)
        if (gameState.gameStage != GameStage.ASSOCIATION)
            throw Exception() // toDO
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
        val gameState = checkNotNull(this.gameState)
        if (gameState.gameStage != GameStage.ASSOCIATION)
            throw Exception() // toDO
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
        val gameState = checkNotNull(this.gameState)
        if (gameState.gameStage != GameStage.GUESS)
            throw Exception() // toDO
        for ((playerAlias, playerState) in gameState.players) {
            if (playerAlias == senderAlias || playerState.status == PlayerStatus.DEAD)
                continue
            val clientService: ClientService = ClientService.getInstance(playerState.connector)
            clientService.sendGuess(senderAlias, cardIndex)
        }
    }

    private fun calculateScores() {
        val gameState: GameState = checkNotNull(gameState)
        val guessedFor: MutableMap<Int, Int> = mutableMapOf()
        //Update score of narrator and check if he or she failed
        val narratorCard: Int = gameState.players[gameState.narratorAlias]!!.cardToDescription!!
        val narratorFailed: Boolean
        if (guessedFor[narratorCard] == null || guessedFor[narratorCard] == gameState.numberOfPlayers - 1) {
            narratorFailed = true
        } else {
            narratorFailed = false
            gameState.players[gameState.narratorAlias]!!.score += 3
        }
        for ((alias, playerState) in gameState.players) {
            if (alias == gameState.narratorAlias)
                continue
            val guess = checkNotNull(playerState.guess)
            if (guessedFor[guess] == null) {
                guessedFor[guess] = 1
            } else {
                guessedFor[guess] = guessedFor[guess]!! + 1
            }

            if (narratorFailed) {
                playerState.score += 2
            } else if (guess == narratorCard) {
                playerState.score += 3
            }
        }
        for ((alias, playerState) in gameState.players) {
            if (alias == gameState.narratorAlias)
                continue
            val cardPlayed = checkNotNull(playerState.cardToDescription)
            if (guessedFor[cardPlayed] != null)
                playerState.score += guessedFor[cardPlayed]!!
        }
    }
}