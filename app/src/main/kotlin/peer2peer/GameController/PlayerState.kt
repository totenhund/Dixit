package game.peer2peer

data class PlayerState(
    var hand: MutableList<Int>,
    var score: Int,
    var status: PlayerStatus,
    var cardToDescription: Int?,
    var finishedStage: Boolean,
    var guess: Int?,
    var passedSynchronization: Boolean,
) {
}