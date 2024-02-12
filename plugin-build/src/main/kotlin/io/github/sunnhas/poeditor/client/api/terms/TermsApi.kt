package io.github.sunnhas.poeditor.client.api.terms

import io.github.sunnhas.poeditor.client.PoeditorClient
import io.github.sunnhas.poeditor.client.api.terms.response.TermsResponse
import io.github.sunnhas.poeditor.client.formData
import io.github.sunnhas.poeditor.client.util.getAndValidateBody
import io.ktor.client.request.*

/**
 * Retrieves the terms for a specific project and language using the Poeditor API.
 *
 * @param projectId The ID of the project.
 * @param languageCode The language code for the terms (optional).
 * @return TermsResponse The response containing the terms for the specified project and language.
 */
internal suspend fun PoeditorClient.getTerms(projectId: Long, languageCode: String? = null): TermsResponse {
    return client.post("terms/list") {
        setBody(
            formData(
                "id" to projectId.toString(),
                languageCode?.let { "language" to it }
            )
        )
    }.getAndValidateBody()
}
