package com.animarkode.lida.feature.list

import com.animarkode.lida.data.model.Kind
import com.animarkode.lida.data.model.WatchItem
import com.animarkode.lida.testing.FakeWatchRepository
import com.animarkode.lida.testing.MainDispatcherRule
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private fun ListViewModel.subscribe(scope: kotlinx.coroutines.CoroutineScope) =
        scope.launch(mainDispatcherRule.dispatcher) { uiState.collect() }

    @Test
    fun initialState_isLoading() {
        assertTrue(ListViewModel(FakeWatchRepository()).uiState.value.isLoading)
    }

    @Test
    fun emitsItemsFromRepository() = runTest {
        val vm = ListViewModel(FakeWatchRepository(listOf(WatchItem(title = "Dune", kind = Kind.MOVIE, updatedAt = 1))))
        vm.subscribe(backgroundScope)

        assertEquals(listOf("Dune"), vm.uiState.value.items.map { it.title })
        assertFalse(vm.uiState.value.isLoading)
    }

    @Test
    fun onIncrementEpisode_delegatesToRepository() = runTest {
        val item = WatchItem(title = "Severance", kind = Kind.SERIES, season = 1, episode = 1, updatedAt = 1)
        val vm = ListViewModel(FakeWatchRepository(listOf(item)))
        vm.subscribe(backgroundScope)

        vm.onIncrementEpisode(item.id)

        assertEquals(2, vm.uiState.value.items.single().episode)
    }

    @Test
    fun deletedItems_areNotShown() = runTest {
        val vm = ListViewModel(
            FakeWatchRepository(listOf(WatchItem(title = "Gone", kind = Kind.MOVIE, updatedAt = 1, deleted = true))),
        )
        vm.subscribe(backgroundScope)

        assertTrue(vm.uiState.value.items.isEmpty())
        assertFalse(vm.uiState.value.isLoading)
    }
}
