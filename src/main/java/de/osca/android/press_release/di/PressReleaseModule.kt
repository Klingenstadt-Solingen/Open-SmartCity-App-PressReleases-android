package de.osca.android.press_release.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.osca.android.essentials.data.client.OSCAHttpClient
import de.osca.android.press_release.data.PressReleaseApiService
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class PressReleaseModule {
    @Singleton
    @Provides
    fun pressReleaseApiService(oscaHttpClient: OSCAHttpClient): PressReleaseApiService =
        oscaHttpClient.create(PressReleaseApiService::class.java)

}
