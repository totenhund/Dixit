package totenhund.com.dixit.peer2peer.GameLogic

enum class GameEvent {
    SEND_DESCRIPTION,
    WAIT_DESCRIPTION,
    WAIT_CARDS_TO_DESCRIPTION,
    WAIT_GUESSES,
    SEND_CARD_TO_DESCRIPTION,
    SEND_GUESS,
    WAIT_START_ROUND,
}