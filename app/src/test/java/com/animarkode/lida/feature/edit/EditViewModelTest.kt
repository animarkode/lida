package com.animarkode.lida.feature.edit

import androidx.lifecycle.SavedStateHandle
import com.animarkode.lida.data.model.Kind
import com.animarkode.lida.data.model.Status
import com.animarkode.lida.data.model.WatchItem
import com.animarkode.lida.testing.FakeWatchRepository
import com.animarkode.lida.testing.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class EditViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val severance = WatchItem(
        title = "Severance", kind = Kind.SERIES, status = Status.FINISHED,
        season = 1, episode = 3, updatedAt = 1,
    )

    private fun newVm(repo: FakeWatchRepository) = EditViewModel(SavedStateHandle(), repo)
    private fun editVm(repo: FakeWatchRepository, id: String) = EditViewModel(SavedStateHandle(mapOf("id" to id)), repo)

    @Test
    fun save_blankTitle_setsErrorAndDoesNotSave() {
        val repo = FakeWatchRepository()
        val vm = newVm(repo)
        vm.onTitleChange("   ")
        vm.save()

        assertTrue(vm.uiState.value.titleError)
        assertFalse(vm.uiState.value.isSaved)
        assertTrue(repo.rows.isEmpty())
    }

    @Test
    fun save_trimsTitle() {
        val repo = FakeWatchRepository()
        val vm = newVm(repo)
        vm.onTitleChange("  Dune ")
        vm.save()

        assertEquals("Dune", repo.rows.single().title)
        assertTrue(vm.uiState.value.isSaved)
    }

    @Test
    fun save_twiceOnNewItem_createsOneRecord() {
        val repo = FakeWatchRepository()
        val vm = newVm(repo)
        vm.onTitleChange("Dune")
        vm.save()
        vm.save()

        assertEquals(1, repo.rows.size)
    }

    @Test
    fun save_invalidTimestamp_setsErrorAndDoesNotSave() {
        val repo = FakeWatchRepository()
        val vm = newVm(repo)
        vm.onTitleChange("Dune")
        vm.onTimestampChange("90:00:00x")
        vm.save()

        assertTrue(vm.uiState.value.timestampError)
        assertTrue(repo.rows.isEmpty())
    }

    @Test
    fun save_blankTimestamp_storesNull() {
        val repo = FakeWatchRepository()
        val vm = newVm(repo)
        vm.onTitleChange("Dune")
        vm.onTimestampChange("")
        vm.save()

        assertNull(repo.rows.single().timestamp)
    }

    @Test
    fun save_movie_clearsSeasonAndEpisode() {
        val repo = FakeWatchRepository(listOf(severance))
        val vm = editVm(repo, severance.id)
        vm.onKindChange(Kind.MOVIE)
        vm.save()

        val saved = repo.rows.single()
        assertEquals(Kind.MOVIE, saved.kind)
        assertNull(saved.season)
        assertNull(saved.episode)
    }

    @Test
    fun existingId_loadsFields() {
        val vm = editVm(FakeWatchRepository(listOf(severance)), severance.id)
        val state = vm.uiState.value

        assertFalse(state.isNew)
        assertEquals("Severance", state.title)
        assertEquals(Kind.SERIES, state.kind)
        assertEquals("1", state.season)
        assertEquals("3", state.episode)
    }

    @Test
    fun existingEdit_keepsIdAndStatus() {
        val repo = FakeWatchRepository(listOf(severance))
        val vm = editVm(repo, severance.id)
        vm.onTitleChange("Severance S2")
        vm.save()

        val saved = repo.rows.single()
        assertEquals(severance.id, saved.id)
        assertEquals(Status.FINISHED, saved.status)
        assertEquals("Severance S2", saved.title)
    }

    @Test
    fun onTimestampChange_turnsNumericKeypadSeparatorsIntoColons() {
        val vm = newVm(FakeWatchRepository())
        vm.onTimestampChange("1.23,45")
        assertEquals("1:23:45", vm.uiState.value.timestamp)

        vm.onTimestampChange("23-45")
        assertEquals("23:45", vm.uiState.value.timestamp)

        vm.onTimestampChange("1 23 45")
        assertEquals("1:23:45", vm.uiState.value.timestamp)
    }

    @Test
    fun onEpisodeChange_ignoresNonDigits() {
        val vm = newVm(FakeWatchRepository())
        vm.onEpisodeChange("12")
        vm.onEpisodeChange("12a")

        assertEquals("12", vm.uiState.value.episode)
    }

    @Test
    fun delete_marksSaved() = runTest {
        val repo = FakeWatchRepository(listOf(severance))
        val vm = editVm(repo, severance.id)
        vm.delete()

        assertTrue(vm.uiState.value.isSaved)
        assertTrue(repo.observeItems().first().isEmpty())
    }
}
