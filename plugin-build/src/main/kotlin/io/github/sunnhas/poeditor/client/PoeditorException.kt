package io.github.sunnhas.poeditor.client

import io.ktor.http.*

class PoeditorException(
    val errorCode: Int,
    val errorMessage: String
) : RuntimeException("$errorCode, $errorMessage") {
    constructor(status: HttpStatusCode) : this(status.value, status.description)
}