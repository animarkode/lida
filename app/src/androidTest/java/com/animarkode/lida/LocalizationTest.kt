package com.animarkode.lida

import android.content.Context
import android.content.res.Configuration
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class LocalizationTest {
    private fun inLocale(tag: String): Context {
        val base = InstrumentationRegistry.getInstrumentation().targetContext
        val config = Configuration(base.resources.configuration).apply { setLocale(Locale.forLanguageTag(tag)) }
        return base.createConfigurationContext(config)
    }

    @Test
    fun english_isDefault() {
        assertEquals("Save", inLocale("en").getString(R.string.save))
        assertEquals("+1 episode", inLocale("en").getString(R.string.increment_episode))
        // A language we don't ship falls back to English.
        assertEquals("Save", inLocale("de").getString(R.string.save))
    }

    @Test
    fun ukrainian() {
        assertEquals("Зберегти", inLocale("uk").getString(R.string.save))
        assertEquals("+1 епізод", inLocale("uk").getString(R.string.increment_episode))
    }

    @Test
    fun polish() {
        assertEquals("Zapisz", inLocale("pl").getString(R.string.save))
        assertEquals("+1 odcinek", inLocale("pl").getString(R.string.increment_episode))
    }
}
