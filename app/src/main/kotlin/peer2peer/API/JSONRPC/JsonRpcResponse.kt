package peer2peer.API.JSONRPC

import com.google.gson.JsonElement

class JsonRpcResponse(
    val result: JsonElement,
    val error: JsonRpcError,
) {
}