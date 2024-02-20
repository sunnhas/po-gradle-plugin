package io.github.sunnhas.poeditor.client

import io.github.sunnhas.poeditor.client.plugin.CallLoggerPlugin
import io.github.sunnhas.poeditor.client.util.Json
import io.github.sunnhas.poeditor.client.util.toFormData
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import org.slf4j.Logger
import java.io.File

internal class PoeditorClient(
    internal val apiToken: String,
    private val logger: Logger? = null,
) : Closeable {

    internal val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json)
        }

        install(CallLoggerPlugin) {
            logger = this@PoeditorClient.logger
        }

        defaultRequest {
            url(BASE_URL)
            contentType(ContentType.Application.FormUrlEncoded)
        }

        HttpResponseValidator {
            validateResponse { response ->
                if (!response.status.isSuccess()) {
                    throw PoeditorException(response.status)
                }
            }
        }
    }

    override fun close() {
        client.close()
    }

    companion object {
        private const val BASE_URL = "https://api.poeditor.com/v2/"
    }
}

internal fun PoeditorClient.formData(vararg pairs: Pair<String, String>?): String {
    return pairs.filterNotNull()
        .associate { it.first to it.second }
        .toMutableMap()
        .also {
            it["api_token"] = apiToken
        }
        .toFormData()
}

internal suspend fun PoeditorClient.download(link: String, into: File) {
    client.get(link).bodyAsChannel().copyAndClose(into.writeChannel())
}
