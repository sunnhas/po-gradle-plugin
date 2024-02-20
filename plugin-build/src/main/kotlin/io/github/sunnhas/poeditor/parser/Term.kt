package io.github.sunnhas.poeditor.parser

import kotlinx.serialization.Serializable

@Serializable
internal data class Term(
    val term: String,
    val definition: String?,
    val context: String? = null,
    val tags: List<String>? = null,
)