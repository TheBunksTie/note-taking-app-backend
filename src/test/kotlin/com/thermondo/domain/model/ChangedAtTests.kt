package com.thermondo.domain.model

import org.junit.Test

import java.util.*
import kotlin.test.assertNotNull

class ChangedAtTests {

    @Test
    fun now_returnsChangedAt(){
        val changedAt = ChangedAt.now()
        assertNotNull(changedAt)
    }

    @Test
    fun fromDate_returnsChangedAt(){
        val changedAt = ChangedAt.fromDate(2000, 1, 1, 12, 0, 0)
        assertNotNull(changedAt)
    }
}
