package io.github.sunnhas.poeditor.client.util

import io.github.sunnhas.poeditor.client.PoeditorException
import io.github.sunnhas.poeditor.client.PoeditorRespone
import io.ktor.client.call.*
import io.ktor.client.statement.*

internal suspend inline fun <reified T> HttpResponse.getAndValidateBody(): T {
    val response = body<PoeditorRespone<T>>()
    if (response.isFailure()) {
        throw PoeditorException(response.response.code, response.response.message)
    }

    return response.result!!
}
