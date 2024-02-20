package io.github.sunnhas.poeditor.parser

import java.io.File

internal class PropertiesFileParser : FormatFileParser {
    override fun parse(file: File): List<Term> {
        return file.readText().split("\n")
            .filter { it.isNotEmpty() }
            .map { line ->
                val (term, value) = line.split("=")
                Term(term.trim(), value.trim())
            }
    }

}