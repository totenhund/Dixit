package peer2peer.API

abstract class RpcClient(
    url: String,
) {
    fun invoke(method: String, params: Map<String, Any?> = emptyMap()){}
}