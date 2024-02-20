package io.github.sunnhas.poeditor.client.api.translations

import io.github.sunnhas.poeditor.client.PoeditorClient
import io.github.sunnhas.poeditor.client.api.translations.request.UpdateTranslation
import io.github.sunnhas.poeditor.client.api.translations.response.UpdateTranslationsResponse
import io.github.sunnhas.poeditor.client.formData
import io.github.sunnhas.poeditor.client.util.Json
import io.github.sunnhas.poeditor.client.util.getAndValidateBody
import io.ktor.client.request.*
import kotlinx.serialization.encodeToString

/**
 * Updates the translation for a specific project, language, and terms,
 * and returns the response with parsed and updated translations.
 * [Translations update](https://poeditor.com/docs/api#translations_update)
 *
 * @param projectId The ID of the project.
 * @param language The language code.
 * @param terms The list of terms to update.
 *
 * @return [UpdateTranslationsResponse] The response containing the parsed and updated translations.
 */
internal suspend fun PoeditorClient.updateTranslation(
    projectId: Long,
    language: String,
    terms: List<UpdateTranslation>,
): UpdateTranslationsResponse {
    return client.post("translations/update") {
        setBody(
            formData(
                "id" to projectId.toString(),
                "language" to language,
                "data" to Json.encodeToString(terms)
            )
        )
    }.getAndValidateBody()
}
