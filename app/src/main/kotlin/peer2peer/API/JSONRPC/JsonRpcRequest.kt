package peer2peer.API.JSONRPC

class JsonRpcRequest(
    val method: String,
    val params: Map<String, Any?> = emptyMap(),
) {
}