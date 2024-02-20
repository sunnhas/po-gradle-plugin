package io.github.sunnhas.poeditor.client.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

val Json = Json {
    namingStrategy = JsonNamingStrategy.SnakeCase
    ignoreUnknownKeys = true
    explicitNulls = false
}
