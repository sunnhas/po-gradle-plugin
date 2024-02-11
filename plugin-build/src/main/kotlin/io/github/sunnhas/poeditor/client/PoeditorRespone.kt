package io.github.sunnhas.poeditor.client

import kotlinx.serialization.Serializable

@Serializable
data class PoeditorRespone<out T>(
    val response: Response,
    val result: T?,
) {
    fun isFailure(): Boolean {
        return response.status.equals("fail", ignoreCase = true)
                || response.code !in 200..299
                || result == null
    }

    @Serializable
    data class Response(
        val status: String,
        val code: Int,
        val message: String,
    )

}
