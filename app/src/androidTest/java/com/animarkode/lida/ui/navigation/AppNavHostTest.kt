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
import com.animarkode.lida.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppNavHostTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private fun str(id: Int) = composeRule.activity.getString(id)

    @Test
    fun doubleTapBack_onEditScreen_returnsToList() {
        composeRule.onNodeWithContentDescription(str(R.string.add)).performClick()
        composeRule.onNodeWithText(str(R.string.edit_title_new)).assertIsDisplayed()

        composeRule.onNodeWithContentDescription(str(R.string.back)).performTouchInput {
            click()
            click()
        }

        composeRule.onNodeWithText(str(R.string.list_title)).assertIsDisplayed()
    }
}
