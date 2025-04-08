package de.osca.android.press_release.domain.entity

import com.google.gson.annotations.SerializedName
import de.osca.android.essentials.domain.entity.DateEnvelope
import de.osca.android.essentials.utils.extensions.toDateString
import de.osca.android.essentials.utils.extensions.toLocalDateTime
import java.io.Serializable
import java.time.LocalDateTime

data class PressRelease(
    @SerializedName("objectId")
    val objectId: String? = null,
    @SerializedName("createdAt")
    val createdAt: String? = null,
    @SerializedName("updatedAt")
    val updatedAt: String? = null,
    @SerializedName("guid", alternate = ["url"])
    val url: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("summary")
    val summary: String? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("content")
    val content: String? = null,
    @SerializedName("deepLink")
    val deepLink: String? = null,
    @SerializedName("imageUrl")
    val imageUrl: String? = "",
    @SerializedName("readingTime")
    val readingTime: Int? = 0,
    @SerializedName("date")
    val date: DateEnvelope? = null,
    @SerializedName("guid-DEPRECATED")
    val guid: String? = "",
    @SerializedName("date_counter")
    val dateCounter: Int? = 0,
) {
    val releaseDate: LocalDateTime get() = date?.value?.toLocalDateTime() ?: LocalDateTime.now()
}