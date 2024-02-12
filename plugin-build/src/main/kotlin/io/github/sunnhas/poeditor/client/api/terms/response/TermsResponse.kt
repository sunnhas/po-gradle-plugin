package io.github.sunnhas.poeditor.client.api.terms.response

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class TermsResponse(
    val terms: List<Term>
) {
    @Serializable
    data class Term(
        val term: String,
        val context: String?,
        val created: String, // todo: Instant
        val updated: String?, // todo: Instant
        val tags: List<String>,
        val comment: String?,
    )
}
