package peer2peer.API.JSONRPC

import peer2peer.API.RpcServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JsonRpcServer(
    val host: String,
    val port: Int,
) {
    fun handleRequest(request: JsonRpcRequest): JsonRpcResponse {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://${this.host}:${this.port}/API")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RpcServer::class.java)
        val call = service.handleRequest<JsonRpcRequest, JsonRpcResponse>(request)
        call.enqueue(
            object: Callback<JsonRpcResponse> {
                override fun onResponse(
                    call: Call<JsonRpcResponse>,
                    response: Response<JsonRpcResponse>
                ) {

                }

                override fun onFailure(call: Call<JsonRpcResponse>?, t: Throwable?) {
                    TODO("Not yet implemented")
                }
            }
        )
    }
}