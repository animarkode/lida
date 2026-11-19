package com.animarkode.lida.feature.edit

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TimestampTest {
    @Test
    fun validFormats() {
        listOf("", "0:00", "23:45", "1:23:45", "12:00:00").forEach { assertTrue(it, isValidTimestamp(it)) }
    }

    @Test
    fun invalidFormats() {
        listOf("abc", "90", "1:2:3", "1:60", "1:23:99", "123:00").forEach { assertFalse(it, isValidTimestamp(it)) }
    }
}
