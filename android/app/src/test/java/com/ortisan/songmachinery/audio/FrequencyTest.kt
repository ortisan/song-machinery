package com.ortisan.songmachinery.audio

import junit.framework.TestCase.assertEquals
import org.junit.Test

class FrequencyTest {

    @Test
    fun testFrequencyConversion() {
        val inputFrequency = 440.0
        val actualOutput = Frequency.frequencyToPitch(inputFrequency)
        assertEquals("A4", actualOutput)
    }

}