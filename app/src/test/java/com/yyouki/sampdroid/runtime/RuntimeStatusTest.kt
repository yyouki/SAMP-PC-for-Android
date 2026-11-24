package com.yyouki.sampdroid.runtime

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RuntimeStatusTest {
    @Test
    fun reportsThatNoExternalRuntimeIsIntegrated() {
        val status = currentRuntimeStatus()

        assertEquals(RuntimeIntegrationState.NOT_INTEGRATED, status.state)
        assertEquals("Not integrated", status.title)
        assertTrue(status.description.contains("no external runtime"))
    }
}
