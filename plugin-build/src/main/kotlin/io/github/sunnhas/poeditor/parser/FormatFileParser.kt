package io.github.sunnhas.poeditor.parser

import io.github.sunnhas.poeditor.config.Format
import java.io.File

internal interface FormatFileParser {
    fun parse(file: File): List<Term>

    companion object {
        fun of(format: Format): FormatFileParser = when (format) {
            Format.JSON -> JsonFileParser()
            Format.PROPERTIES -> PropertiesFileParser()
        }
    }
}