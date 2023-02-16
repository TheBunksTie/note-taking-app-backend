package com.thermondo.domain.note

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BodyTests {

    @Test
    fun new_tooLongBody_throwsException() {
        val overlyLongBody = """
an overly long body, describing the content of my note
while being to wordy on doing that, therefor provoking
an exception.

Keep it simple and short!
"""

        assertFailsWith<Body.BodyTooLongException> {
            Body(overlyLongBody)
        }
    }

    @Test
    fun new_validEmptyBody_returnsBody() {
        val expectedBody = ""

        val body = Body(expectedBody)
        assertEquals(expectedBody, body.value)
    }

    @Test
    fun new_validBody_returnsBody() {
        val expectedBody = "a perfectly fitting body for my note"

        val body = Body(expectedBody)
        assertEquals(expectedBody, body.value)
    }
}
