package de.osca.android.press_release.domain.boundary

import de.osca.android.press_release.domain.entity.PressRelease

interface PressReleaseRepository {
    suspend fun getAll(order: String = "-date"): List<PressRelease>
    suspend fun search(query: String): List<PressRelease>
    suspend fun getById(id: String): PressRelease?
}