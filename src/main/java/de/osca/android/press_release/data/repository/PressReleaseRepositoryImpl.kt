package de.osca.android.press_release.data.repository

import de.osca.android.essentials.data.remote.EssentialsApiService
import de.osca.android.essentials.domain.entity.elastic_search.ElasticSearchRequest
import de.osca.android.networkservice.utils.RequestHandler
import de.osca.android.press_release.data.PressReleaseApiService
import de.osca.android.press_release.domain.boundary.PressReleaseRepository
import de.osca.android.press_release.domain.entity.PressRelease
import javax.inject.Inject

class PressReleaseRepositoryImpl @Inject constructor(
    private val requestHandler: RequestHandler,
    private val pressReleaseApiService: PressReleaseApiService,
    private val essentialsApiService: EssentialsApiService
) : PressReleaseRepository {
    override suspend fun getAll(order: String): List<PressRelease> {
        return requestHandler
            .makeRequest {
                pressReleaseApiService.getPressRelease(order = order)
            }
            ?.sortedByDescending { it.releaseDate } ?: emptyList()
    }

    override suspend fun search(query: String): List<PressRelease> {
        val request = ElasticSearchRequest(
            index = ES_PRESS_RELEASE_INDEX,
            query = query,
            raw = false
        )
        return requestHandler.makeRequest {
            pressReleaseApiService.elasticSearch(request)
        } ?: emptyList()
    }

    override suspend fun getById(id: String): PressRelease? {
        return requestHandler
            .makeRequest {
                pressReleaseApiService.getPressReleaseById(id = id)
            }
    }

    companion object {
        const val ES_PRESS_RELEASE_INDEX = "press_releases"
    }
}