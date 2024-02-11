package io.github.sunnhas.poeditor.client.api.projects

import io.github.sunnhas.poeditor.client.*
import io.github.sunnhas.poeditor.client.PoeditorClient
import io.github.sunnhas.poeditor.client.api.projects.request.ExportType
import io.github.sunnhas.poeditor.client.api.projects.response.ExportResponse
import io.github.sunnhas.poeditor.client.api.projects.response.ProjectResponse
import io.github.sunnhas.poeditor.client.util.getAndValidateBody
import io.ktor.client.request.*

/**
 * Retrieves a list of projects for the current user.
 *
 * @return A ProjectResponse object containing the list of projects.
 */
internal suspend fun PoeditorClient.getProjects(): ProjectResponse {
    return client.post("projects/list") {
        setBody(
            formData()
        )
    }.getAndValidateBody()
}

/**
 * Retrieves an export link for a given project ID, language code, export type, and optional tags.
 *
 * @param projectId The ID of the project.
 * @param languageCode The language code.
 * @param exportType The type of export.
 * @param tags Optional list of tags.
 * @return An ExportResponse object containing the export link.
 */
internal suspend fun PoeditorClient.getExportLink(projectId: Long, languageCode: String, exportType: ExportType, tags: List<String>? = null): ExportResponse {
    return client.post("projects/export") {
        setBody(
            formData(
                "id" to projectId.toString(),
                "language" to languageCode,
                "type" to exportType.value,
                tags?.let { "tags" to "[${tags.joinToString(",") { "\"$it\"" }}]" },
            )
        )
    }.getAndValidateBody()
}