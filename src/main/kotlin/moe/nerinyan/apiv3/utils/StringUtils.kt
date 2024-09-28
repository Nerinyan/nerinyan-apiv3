package moe.nerinyan.apiv3.utils

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

private val objectMapper = jacksonObjectMapper()

fun toJsonString(obj: Any): String = objectMapper.writeValueAsString(obj)


