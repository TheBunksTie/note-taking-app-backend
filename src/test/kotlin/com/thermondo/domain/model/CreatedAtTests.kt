package com.thermondo.domain.model

import org.junit.Test
import kotlin.test.assertNotNull

class CreatedAtTests {

    @Test
    fun now_returnsCreatedAt(){
        val createAt = CreatedAt.now()
        assertNotNull(createAt)
    }

    @Test
    fun fromDate_returnsCreatedAt(){
        val createAt = CreatedAt.fromDate(2000, 1, 1, 12, 0, 0)
        assertNotNull(createAt)
    }
}
