package org.example.app

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.example.app.history.HistoryEntry
import org.example.app.history.HistorySerializer
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistorySerializerAndroidTest {

    @Test
    fun serializeDeserialize_roundTrip_preservesEntries() {
        val entries = listOf(
            HistoryEntry(
                timestampMs = 123L,
                name1 = "A\"lice",
                name2 = "B\\ob",
                relationLetter = 'F',
                relationLabel = "Friends"
            ),
            HistoryEntry(
                timestampMs = 124L,
                name1 = "John",
                name2 = "Jane",
                relationLetter = 'L',
                relationLabel = "Love"
            )
        )

        val raw = HistorySerializer.serialize(entries)
        val parsed = HistorySerializer.deserialize(raw)

        assertEquals(entries, parsed)
    }

    @Test
    fun deserialize_invalidInput_returnsEmpty() {
        assertEquals(emptyList<HistoryEntry>(), HistorySerializer.deserialize("not-json"))
        assertEquals(emptyList<HistoryEntry>(), HistorySerializer.deserialize(""))
        assertEquals(emptyList<HistoryEntry>(), HistorySerializer.deserialize("[]"))
    }
}
