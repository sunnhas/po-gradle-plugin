package io.github.sunnhas.poeditor.client.api.terms.response

import kotlinx.serialization.Serializable

@Serializable
data class CreateTermsResponse(
    val terms: CreateTermsParsed,
) {
    @Serializable
    data class CreateTermsParsed(
        val parsed: Int,
        val added: Int,
    )
}