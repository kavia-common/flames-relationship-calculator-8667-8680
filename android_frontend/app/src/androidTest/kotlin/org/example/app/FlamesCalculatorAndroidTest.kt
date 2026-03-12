package org.example.app

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.example.app.flames.FlamesCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FlamesCalculatorAndroidTest {

    @Test
    fun normalizeName_keepsLettersOnly_andLowercases() {
        val normalized = FlamesCalculator.normalizeName("  John-Doe 123 ")
        assertEquals("johndoe", normalized)
    }

    @Test
    fun remainingLetterCount_cancelsCommonLetters_multiset() {
        // "abc" and "bcd" -> cancel 'b' and 'c' => remaining: 'a' + 'd' => 2
        val count = FlamesCalculator.remainingLetterCount("abc", "bcd")
        assertEquals(2, count)
    }

    @Test
    fun calculate_returnsValidRelation_forValidInputs() {
        val relation = FlamesCalculator.calculate("Alice", "Bob")
        assertTrue(relation.label.isNotEmpty())
        assertTrue(relation.description.isNotEmpty())
    }

    @Test(expected = IllegalArgumentException::class)
    fun calculate_throws_forEmptyName() {
        FlamesCalculator.calculate("   ", "Bob")
    }
}
