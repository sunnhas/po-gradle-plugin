package io.github.sunnhas.poeditor.client.api.projects.response

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: Long,
    val name: String,
)