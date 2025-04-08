package de.osca.android.press_release.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.osca.android.press_release.data.repository.PressReleaseRepositoryImpl
import de.osca.android.press_release.domain.boundary.PressReleaseRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class PressReleaseRepositoryModule {
    @Binds
    abstract fun providePressReleaseRepository(repositoryImpl: PressReleaseRepositoryImpl): PressReleaseRepository
}