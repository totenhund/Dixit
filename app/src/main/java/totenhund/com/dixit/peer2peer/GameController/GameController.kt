package game.peer2peer

import totenhund.com.dixit.peer2peer.GameLogic.GameLogic

object GameController {
    var gameState: GameState? = null
        get() = field
        private set(value) {
            field = value
        }

    init {
        createGameState(listOf("playerA", "playerB", "playerC"))
    }

    fun createGameState(playerAliases: List<String>) { //toDo
        val numberOfPlayers: Int = playerAliases.size
        val players: MutableMap<String, PlayerState> = mutableMapOf()
        val narratorAlias: String = playerAliases[0]
        val gameStage: GameStage = GameStage.SYNCHRONIZATION
        val handSize = 4
        var cardsTaken = 0
        val descriptionTime = 180
        val guessTime = 120
        for (alias in playerAliases) {
            val playerState = PlayerState(
                hand = mutableListOf(),
                score = 0,
                status = PlayerStatus.ALIVE,
                cardToDescription = null,
                guess = null,
                finishedStage = false,
                passedSynchronization = false,
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

    fun finishStage() {
        val gameState: GameState = checkNotNull(this.gameState)
        val gameLogic = GameLogic.getInstance()
        when (gameState.gameStage) {
            GameStage.SYNCHRONIZATION -> {
                gameState.gameStage = GameStage.ASSOCIATION
                for ((alias, playerState) in gameState.players) {
                    playerState.status = PlayerStatus.SYNCHRONIZED
                }
                gameLogic.startRound()
            }
            GameStage.ASSOCIATION -> {
                gameLogic.cardsToDescriptionReceived()
            }
            GameStage.GUESS -> {
                gameState.gameStage = GameStage.ASSOCIATION
                for ((alias, playerState) in gameState.players) {
                    playerState.finishedStage = false
                    playerState.passedSynchronization = false
                    playerState.cardToDescription = null
                    playerState.guess = null
                    while (playerState.hand.size != gameState.handSize) {
                        playerState.hand.add(gameState.cardsTaken++)
                    }
                }
                var narratorFound = false
                var narratorChanged = false
                for ((alias, playerState) in gameState.players) {
                    if (narratorFound) {
                        gameState.narratorAlias = alias
                        narratorChanged = true
                        break
                    }
                    if (alias == gameState.narratorAlias) {
                        narratorFound = true
                    }
                }
                if (!narratorChanged) {
                    gameState.narratorAlias = gameState.players.keys.first()
                }
                gameLogic.guessesReceived()
                this.broadcastGameState()
            }
        }
    }

    fun receiveFinishStage(senderAlias: String) {
        val gameState: GameState = checkNotNull(this.gameState)
        gameState.players[senderAlias]!!.finishedStage = true
        var stageFinished = true
        for ((alias, playerState) in gameState.players) {
            if (playerState.passedSynchronization == false) {
                stageFinished = false;
            }
        }
        if (stageFinished) {
            this.finishStage()
        }
    }

    fun updateGameState(gameState: GameState, senderAlias: String) {
        val playerAlias: String = GameLogic.getInstance().playerAlias
        var curGameState: GameState = checkNotNull(this.gameState)
        if (curGameState.gameStage != GameStage.SYNCHRONIZATION)
            throw Exception()
        check(
            curGameState != gameState
                    && curGameState.players[playerAlias]!!.status == PlayerStatus.SYNCHRONIZED
                    && curGameState.players[senderAlias]!!.status == PlayerStatus.SYNCHRONIZED
        ) { "Desynchronization" }
        this.gameState = gameState
        curGameState = checkNotNull(this.gameState)
        curGameState.players[playerAlias]!!.passedSynchronization = true
        curGameState.players[senderAlias]!!.passedSynchronization = true
        var stageFinished = true
        for ((alias, playerState) in gameState.players) {
            if (playerState.passedSynchronization == false) {
                stageFinished = false;
            }
        }
        if (stageFinished) {
            this.broadcastFinishedStage(playerAlias)
        }
    }

    fun updateNarratorDescription(
        description: String,
        cardIndex: Int,
    ) {
        val gameState: GameState = checkNotNull(this.gameState)
        if (gameState.gameStage == GameStage.ASSOCIATION)
            throw Exception()
        gameState.narratorDescription = description
        gameState.players[gameState.narratorAlias]!!.cardToDescription = cardIndex
        val gameLogic = GameLogic.getInstance()
        gameLogic.descriptionReceived()
    }

    fun updatePlayerCardToDescription(
        senderAlias: String,
        cardIndex: Int,
    ) {
        val playerAlias: String = GameLogic.getInstance().playerAlias
        val gameState: GameState = checkNotNull(this.gameState)
        if (gameState.gameStage == GameStage.ASSOCIATION)
            throw Exception()
        gameState.players[senderAlias]!!.cardToDescription = cardIndex
        gameState.players[senderAlias]!!.hand.remove(cardIndex)
        var stageFinished = true
        for ((alias, playerState) in gameState.players) {
            if (playerState.cardToDescription == null) {
                stageFinished = false;
            }
        }
        if (stageFinished) {
            this.broadcastFinishedStage(playerAlias)
        }
    }


    fun updatePlayerGuess(
        senderAlias: String,
        cardIndex: Int,
    ) {
        val playerAlias: String = GameLogic.getInstance().playerAlias
        val gameState: GameState = checkNotNull(this.gameState)
        gameState.players[senderAlias]!!.guess = cardIndex
        var stageFinished = true
        for ((alias, playerState) in gameState.players) {
            if (alias == gameState.narratorAlias)
                continue
            if (playerState.guess == null) {
                stageFinished = false;
            }
        }
        if (stageFinished) {
            this.calculateScores()
            this.broadcastFinishedStage(playerAlias)
        }
    }

    fun broadcastFinishedStage(
        senderAlias: String,
    ) {
        val gameState: GameState = checkNotNull(this.gameState)
        /*for ((playerAlias, playerState) in gameState.players) {
            if (playerAlias == senderAlias || playerState.status == PlayerStatus.DEAD)
                continue
            //val clientService: ClientService = ClientService.getInstance(playerState.connector)
            //clientService.sendGameState(gameState)
        }*/
        for ((playerAlias, playerState) in gameState.players) {
            playerState.finishedStage = true
            this.receiveFinishStage(playerAlias)
        }
    }

    fun broadcastGameState(
    ) {
        val playerAlias: String = GameLogic.getInstance().playerAlias
        val gameState: GameState = checkNotNull(this.gameState)
        if (gameState.gameStage != GameStage.SYNCHRONIZATION)
            throw Exception()
        if (gameState.players[playerAlias]!!.status != PlayerStatus.SYNCHRONIZED)
            throw Exception()
        for ((playerAlias, playerState) in gameState.players) {
            if (playerAlias == playerAlias || playerState.status == PlayerStatus.DEAD)
                continue
            //val clientService: ClientService = ClientService.getInstance(playerState.connector)
            //clientService.sendGameState(gameState)
        }
    }

    fun broadcastNarratorDescription(
        senderAlias: String,
        description: String,
        cardIndex: Int,
    ) {
        val gameState = checkNotNull(this.gameState)
        if (gameState.gameStage != GameStage.ASSOCIATION)
            throw Exception()
        check(senderAlias == gameState.narratorAlias)
        for ((playerAlias, playerState) in gameState.players) {
            if (playerAlias == senderAlias || playerState.status != PlayerStatus.SYNCHRONIZED)
                continue
            //val clientService: ClientService = ClientService.getInstance(playerState.connector)
            //clientService.sendNarratorDescription(description, cardIndex)
        }
    }

    fun broadcastCardToDescription(
        senderAlias: String,
        cardIndex: Int,
    ) {
        val gameState = checkNotNull(this.gameState)
        if (gameState.gameStage != GameStage.ASSOCIATION)
            throw Exception()
        check(senderAlias != gameState.narratorAlias)
        for ((playerAlias, playerState) in gameState.players) {
            if (playerAlias == senderAlias || playerState.status != PlayerStatus.SYNCHRONIZED)
                continue
            //val clientService: ClientService = ClientService.getInstance(playerState.connector)
            //clientService.sendCardToDescription(senderAlias, cardIndex)
        }
    }

    fun broadcastGuess(
        senderAlias: String,
        cardIndex: Int,
    ) {
        val gameState = checkNotNull(this.gameState)
        if (gameState.gameStage != GameStage.GUESS)
            throw Exception()
        for ((playerAlias, playerState) in gameState.players) {
            if (playerAlias == senderAlias || playerState.status != PlayerStatus.SYNCHRONIZED)
                continue
            //val clientService: ClientService = ClientService.getInstance(playerState.connector)
            //clientService.sendGuess(senderAlias, cardIndex)
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