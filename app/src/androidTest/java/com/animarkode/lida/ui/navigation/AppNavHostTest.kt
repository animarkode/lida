package com.animarkode.lida.ui.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.animarkode.lida.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppNavHostTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun doubleTapBack_onEditScreen_returnsToList() {
        composeRule.onNodeWithContentDescription("Додати").performClick()
        composeRule.onNodeWithText("Новий запис").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Назад").performTouchInput {
            click()
            click()
        }

        composeRule.onNodeWithText("Ліда").assertIsDisplayed()
    }
}
