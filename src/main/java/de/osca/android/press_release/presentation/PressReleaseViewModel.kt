package de.osca.android.press_release.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.osca.android.essentials.domain.entity.DateEnvelope
import de.osca.android.essentials.presentation.base.BaseViewModel
import de.osca.android.essentials.utils.extensions.displayContent
import de.osca.android.essentials.utils.extensions.loading
import de.osca.android.essentials.utils.extensions.resetWith
import de.osca.android.press_release.domain.boundary.PressReleaseRepository
import de.osca.android.press_release.domain.entity.PressRelease
import de.osca.android.press_release.presentation.args.PressReleaseDesignArgs
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PressReleaseViewModel @Inject constructor(
    val pressReleaseDesignArgs: PressReleaseDesignArgs,
    private val repository: PressReleaseRepository
) : BaseViewModel() {
    private val allPressReleases = mutableStateListOf<PressRelease>()
    val displayedPressReleases = mutableStateListOf<PressRelease>()
    private var searchJob: Job? = null
    val foundPressRelease = mutableStateOf<PressRelease?>(null)

    /**
     * call this function to initialize all press releases.
     * it sets the screen to loading, fetches the data from parse and when
     * it finished successful then displays the content and when an error
     * occurred it displays an message screen
     */
    fun initializePressReleases() {
        viewModelScope.launch {
            wrapperState.loading()
            val pressReleases = async { fetchPressRelease().join() }
            awaitAll(pressReleases)
        }
    }

    /**
     * fetches all press releases from parse and when successfully loaded then
     * displays the content
     */
    fun fetchPressRelease(): Job = launchDataLoad {
        val result = repository.getAll("-date")
        allPressReleases.resetWith(result)
        displayedPressReleases.resetWith(allPressReleases)

        wrapperState.displayContent()
    }

    fun fetchPressReleaseById(id: String): Job = launchDataLoad {
        val result = repository.getById(id)
        foundPressRelease.value = result

        wrapperState.displayContent()
    }

    /**
     * search through all POIs and set visibility of matching markers
     * @property query the text which the POI must contain
     */
    fun search(query: String) {
        when(query.length){
            in 0..3 -> displayedPressReleases.resetWith(allPressReleases)
            else -> {
                searchJob?.cancel()
                searchJob = elasticSearch(query)
            }
        }
    }

    /**
     * !!! call this function only via search()
     */
    private fun elasticSearch(query: String): Job = launchDataLoad {
        val result = repository.search(query)

        displayedPressReleases.resetWith(result)
    }

    /**
     * if you have no server, you can use this mocked data
     */
    fun getMockedPressReleases() {
        val result = listOf(
            PressRelease(
                title = "Meldungtitel",
                summary = "Kurze Zusammenfassung des Artikels",
                category = "Pressemeldung",
                content = "Das ist der gesamte Content des Artikels!",
                //date = "2022-07-04T00:00:00.000Z"
            ),
            PressRelease(
                title = "Meldungtitel",
                summary = "Kurze Zusammenfassung des Artikels",
                category = "Pressemeldung",
                content = "Das ist der gesamte Content des Artikels!",
                //date = "2022-07-04T00:00:00.000Z"
            ),
            PressRelease(
                title = "Meldungtitel",
                summary = "Kurze Zusammenfassung des Artikels",
                category = "Pressemeldung",
                content = "Das ist der gesamte Content des Artikels!",
                //date = "2022-07-04T00:00:00.000Z"
            ),
            PressRelease(
                title = "Meldungtitel",
                summary = "Kurze Zusammenfassung des Artikels",
                category = "Pressemeldung",
                content = "Das ist der gesamte Content des Artikels!",
                //date = "2022-07-04T00:00:00.000Z"
            ),
            PressRelease(
                title = "Meldungtitel",
                summary = "Kurze Zusammenfassung des Artikels",
                category = "Pressemeldung",
                content = "Das ist der gesamte Content des Artikels!",
                //date = "2022-07-04T00:00:00.000Z"
            ),
            PressRelease(
                title = "Meldungtitel",
                summary = "Kurze Zusammenfassung des Artikels",
                category = "Pressemeldung",
                content = "Das ist der gesamte Content des Artikels!",
                //date = "2022-07-04T00:00:00.000Z"
            ),
            PressRelease(
                title = "Meldungtitel",
                summary = "Kurze Zusammenfassung des Artikels",
                category = "Pressemeldung",
                content = "Das ist der gesamte Content des Artikels!",
                //date = "2022-07-04T00:00:00.000Z"
            ),
            PressRelease(
                title = "Meldungtitel",
                summary = "Kurze Zusammenfassung des Artikels",
                category = "Pressemeldung",
                content = "Das ist der gesamte Content des Artikels!",
                //date = "2022-07-04T00:00:00.000Z"
            ),
            PressRelease(
                title = "Meldungtitel",
                summary = "Kurze Zusammenfassung des Artikels",
                category = "Pressemeldung",
                content = "Das ist der gesamte Content des Artikels!",
                //date = "2022-07-04T00:00:00.000Z"
            ),
            PressRelease(
                title = "Meldungtitel",
                summary = "Kurze Zusammenfassung des Artikels",
                category = "Pressemeldung",
                content = "Das ist der gesamte Content des Artikels!",
                //date = "2022-07-04T00:00:00.000Z"
            )
        )
        displayedPressReleases.resetWith(result.sortedByDescending { it.releaseDate })
    }
}