package com.github.sdp.tarjetakuna.utils

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * This object contains custom serializers and deserializers for the GSON library.
 */
object JsonAdapters {

    /**
     * Custom json adapter for the LocalDate class.
     */
    class LocalDateAdapter : JsonDeserializer<LocalDate>,
        JsonSerializer<LocalDate> {
        override fun serialize(
            src: LocalDate?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            return JsonPrimitive(src!!.format(DateTimeFormatter.ISO_LOCAL_DATE))
        }

        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): LocalDate {
            return LocalDate.parse(json!!.asString, DateTimeFormatter.ISO_LOCAL_DATE)
        }
    }
}
