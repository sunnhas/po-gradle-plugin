package io.github.sunnhas.poeditor.client.api.projects.response

import kotlinx.serialization.Serializable

@Serializable
data class ProjectResponse(
    val projects: List<Project>
)