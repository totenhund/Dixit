package game.peer2peer

data class PlayerState(
    var connector: String,
    var hand: MutableList<Int>,
    var score: Int,
    var status: PlayerStatus,
    var cardToDescription: Int?,
    var guess: Int?,
) {
}