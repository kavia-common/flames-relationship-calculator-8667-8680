package org.example.app.flames

/**
 * Pure Kotlin FLAMES calculator.
 *
 * Classic method:
 * 1) Normalize names (trim, lowercase, remove non-letters).
 * 2) Remove common letters (multiset cancellation).
 * 3) Count remaining letters.
 * 4) Use count mod 6 to select from FLAMES.
 */
object FlamesCalculator {

    enum class Relation(val letter: Char, val label: String, val description: String) {
        FRIENDS('F', "Friends", "Strong friendship vibes and good compatibility."),
        LOVE('L', "Love", "A romantic connection is on the cards."),
        AFFECTION('A', "Affection", "Warmth, care, and fondness between you two."),
        MARRIAGE('M', "Marriage", "A long-term commitment potential is indicated."),
        ENEMIES('E', "Enemies", "Clashing personalities—handle with patience."),
        SIBLINGS('S', "Siblings", "Comfortable bond like family and close companions.")
    }

    // PUBLIC_INTERFACE
    fun normalizeName(input: String): String {
        /** Normalizes a name by trimming, lowercasing, and keeping letters only. */
        return input
            .trim()
            .lowercase()
            .filter { it.isLetter() }
    }

    // PUBLIC_INTERFACE
    fun remainingLetterCount(name1: String, name2: String): Int {
        /** Returns the letter count after removing common letters (multiset cancellation). */
        val a = normalizeName(name1)
        val b = normalizeName(name2)

        val counts = IntArray(26)
        for (c in a) {
            val idx = (c.code - 'a'.code)
            if (idx in 0..25) counts[idx]++
        }

        var remainingB = 0
        for (c in b) {
            val idx = (c.code - 'a'.code)
            if (idx !in 0..25) continue
            if (counts[idx] > 0) {
                counts[idx]--
            } else {
                remainingB++
            }
        }

        var remainingA = 0
        for (i in counts.indices) remainingA += counts[i]

        return remainingA + remainingB
    }

    // PUBLIC_INTERFACE
    fun calculate(name1: String, name2: String): Relation {
        /** Calculates the FLAMES relation. Throws IllegalArgumentException for invalid inputs. */
        val n1 = normalizeName(name1)
        val n2 = normalizeName(name2)
        if (n1.isEmpty() || n2.isEmpty()) {
            throw IllegalArgumentException("Both names must be non-empty after normalization.")
        }

        val count = remainingLetterCount(n1, n2)
        // Traditional: use count % 6; if remainder 0 => last letter (S).
        val remainder = count % 6
        val index = if (remainder == 0) 5 else remainder - 1
        return Relation.entries[index]
    }
}
