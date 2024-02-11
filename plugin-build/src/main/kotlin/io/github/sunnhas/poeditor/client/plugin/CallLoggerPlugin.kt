package io.github.sunnhas.poeditor.client.plugin

import io.ktor.client.plugins.api.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.slf4j.Logger

val CallLoggerPlugin = createClientPlugin("CallLogger", ::CallLoggerConfig) {
    val logger = pluginConfig.logger

    onRequest { request, _ ->
        logger?.info("Doing request: ${request.method.value} ${request.url.buildString()}")
    }

    ResponseObserver.install(
        ResponseObserver.prepare {
            onResponse { response ->
                logger?.debug(
                    """
                    Request: ${response.request.method.value} ${response.request.url}
                    Request body: ${response.request.getBodyAsString()}
                    
                    Response: ${response.status.value}
                    Response body: ${response.getBodyAsString()}
                """.trimIndent()
                )
            }
        },
        client,
    )
}

class CallLoggerConfig {
    var logger: Logger? = null
}

private fun HttpRequest.getBodyAsString(): String =
    when (val requestBody = content) {
        is FormDataContent -> requestBody.formData.formUrlEncode()
        else -> "$requestBody"
    }

private suspend fun HttpResponse.getBodyAsString(): String? = bodyAsText().ifBlank {
    null
}
