package peer2peer.API

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface RpcServer {
    @POST("/API")
    fun <R, T> handleRequest(@Query("request") request: R): Call<T>
}