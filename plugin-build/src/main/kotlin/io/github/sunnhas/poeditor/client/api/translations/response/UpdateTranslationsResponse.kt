package io.github.sunnhas.poeditor.client.api.translations.response

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTranslationsResponse(
    val translations: UpdateTranslationsParsed,
) {
    @Serializable
    data class UpdateTranslationsParsed(
        val parsed: Int,
        val updated: Int,
    )
}