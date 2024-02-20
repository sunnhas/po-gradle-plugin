package io.github.sunnhas.poeditor.client.api.translations.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTranslation(
    val term: String,
    val translation: Translation,
) {
    @Serializable
    data class Translation(
        val content: String,
    )
}