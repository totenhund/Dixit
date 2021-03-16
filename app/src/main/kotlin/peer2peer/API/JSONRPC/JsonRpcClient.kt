package peer2peer.API.JSONRPC

import peer2peer.API.RpcClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JsonRpcClient(
    url: String,
) : RpcClient(url) {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}