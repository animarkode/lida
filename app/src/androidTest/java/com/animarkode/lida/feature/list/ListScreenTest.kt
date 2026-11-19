package com.animarkode.lida.feature.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.animarkode.lida.data.model.Kind
import com.animarkode.lida.data.model.WatchItem
import com.animarkode.lida.ui.theme.LidaTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    private fun show(state: ListUiState, onIncrement: (String) -> Unit = {}) = composeRule.setContent {
        LidaTheme {
            ListContent(state = state, onAdd = {}, onOpen = {}, onIncrement = onIncrement)
        }
    }

    @Test
    fun emptyState_showsHint() {
        show(ListUiState(isLoading = false))
        composeRule.onNodeWithText("Тут з'являться", substring = true).assertIsDisplayed()
    }

    @Test
    fun seriesCard_incrementButtonCallsBack() {
        val item = WatchItem(title = "Severance", kind = Kind.SERIES, season = 2, episode = 5, updatedAt = 1)
        var clicked: String? = null
        show(ListUiState(items = listOf(item), isLoading = false), onIncrement = { clicked = it })

        composeRule.onNodeWithText("S2 · E5").assertIsDisplayed()
        composeRule.onNodeWithText("+1 епізод").performClick()
        assertEquals(item.id, clicked)
    }

    @Test
    fun movieCard_hasNoIncrementButton() {
        val item = WatchItem(title = "Dune", kind = Kind.MOVIE, timestamp = "1:23:45", updatedAt = 1)
        show(ListUiState(items = listOf(item), isLoading = false))

        composeRule.onNodeWithText("1:23:45").assertIsDisplayed()
        composeRule.onNodeWithText("+1 епізод").assertDoesNotExist()
    }
}
