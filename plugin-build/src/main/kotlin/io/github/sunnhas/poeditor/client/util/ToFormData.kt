package io.github.sunnhas.poeditor.client.util

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

internal fun Map<String, String>.toFormData() =
    this.map { (key, value) -> "$key=${URLEncoder.encode(value, StandardCharsets.UTF_8)}" }
        .joinToString("&")
