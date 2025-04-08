package de.osca.android.press_release.data

import androidx.compose.foundation.interaction.PressInteraction
import de.osca.android.essentials.domain.entity.elastic_search.ElasticSearchRequest
import de.osca.android.essentials.domain.entity.elastic_search.ElasticSearchResponse
import de.osca.android.essentials.utils.annotations.UnwrappedResponse
import de.osca.android.press_release.domain.entity.PressRelease
import retrofit2.Response
import retrofit2.http.*

interface PressReleaseApiService {

    @GET("classes/PressRelease")
    suspend fun getPressRelease(@Query("order") order: String = "-date"): Response<List<PressRelease>>

    @GET("classes/PressRelease/{id}")
    @UnwrappedResponse
    suspend fun getPressReleaseById(@Path("id") id: String): Response<PressRelease>

    @POST("functions/elastic-search")
    suspend fun elasticSearch(@Body elasticSearchRequest: ElasticSearchRequest): Response<List<PressRelease>>
}