package io.github.sunnhas.poeditor.client.api.terms

import io.github.sunnhas.poeditor.client.PoeditorClient
import io.github.sunnhas.poeditor.client.api.terms.request.CreateTerm
import io.github.sunnhas.poeditor.client.api.terms.response.CreateTermsResponse
import io.github.sunnhas.poeditor.client.api.terms.response.TermsResponse
import io.github.sunnhas.poeditor.client.formData
import io.github.sunnhas.poeditor.client.util.Json
import io.github.sunnhas.poeditor.client.util.getAndValidateBody
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.encodeToString

/**
 * Retrieves the terms for a specific project and language using the Poeditor API.
 * [Terms list](https://poeditor.com/docs/api#terms_list)
 *
 * @param projectId The ID of the project.
 * @param languageCode The language code for the terms (optional).
 * @return [TermsResponse] The response containing the terms for the specified project and language.
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

/**
 * A function to create terms for a given project using the PoeditorClient.
 * It takes the project ID and a list of terms to create, and returns a CreateTermsResponse.
 * [Terms add](https://poeditor.com/docs/api#terms_add)
 *
 * @param projectId The ID of the project.
 * @param terms The list of terms to create.
 *
 * @return [CreateTermsResponse] The response containing the created terms.
 */
internal suspend fun PoeditorClient.createTerms(projectId: Long, terms: List<CreateTerm>): CreateTermsResponse {
    return client.post("terms/add") {
        setBody(
            formData(
                "id" to projectId.toString(),
                "data" to Json.encodeToString(terms)
            )
        )
    }.getAndValidateBody()
}
