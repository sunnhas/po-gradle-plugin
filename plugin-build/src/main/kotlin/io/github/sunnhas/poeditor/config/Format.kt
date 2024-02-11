package io.github.sunnhas.poeditor.config

enum class Format(
    internal val value: String,
    internal val ext: String,
) {
    JSON("json", "json"),
    PROPERTIES("properties", "properties"),
}