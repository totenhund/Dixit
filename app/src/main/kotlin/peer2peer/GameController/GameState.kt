package game.peer2peer

data class GameState(
    var numberOfPlayers: Int,
    var players: MutableMap<String, PlayerState>,
    var narratorAlias: String,
    var gameStage: GameStage,
    val handSize: Int,
    var cardsTaken: Int,
    var narratorDescription: String?,
    var descriptionDeadline: Long?,
    val descriptionTime: Int,
    var guessDeadline: Long?,
    val guessTime: Int,
) {
}