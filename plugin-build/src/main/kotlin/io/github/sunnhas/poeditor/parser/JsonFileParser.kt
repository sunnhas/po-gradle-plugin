package io.github.sunnhas.poeditor.parser

import kotlinx.serialization.json.Json
import java.io.File

internal class JsonFileParser : FormatFileParser {

    private val json = Json { ignoreUnknownKeys = true }
    override fun parse(file: File): List<Term> {
        return json.decodeFromString<List<Term>>(file.readText())
    }
}