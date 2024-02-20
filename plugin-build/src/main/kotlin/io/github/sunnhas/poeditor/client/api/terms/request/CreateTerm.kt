package io.github.sunnhas.poeditor.client.api.terms.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateTerm(
    val term: String,
    val context: String? = null,
    val comment: String? = null,
    val tags: List<String>? = null,
)