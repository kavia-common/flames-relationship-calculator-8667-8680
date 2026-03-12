package org.example.app

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.example.app.history.HistoryEntry
import org.example.app.history.HistoryRepository
import org.example.app.history.HistoryStore
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HistoryRepositoryAndroidTest {

    private class InMemoryStore : HistoryStore {
        private var data: List<HistoryEntry> = emptyList()

        override fun load(): List<HistoryEntry> = data
        override fun save(entries: List<HistoryEntry>) {
            data = entries
        }
    }

    @Test
    fun addAndList_returnsNewestFirst_andCaps() {
        val store = InMemoryStore()
        val repo = HistoryRepository(store, maxItems = 2)

        repo.add(HistoryEntry(1L, "A", "B", 'F', "Friends"))
        repo.add(HistoryEntry(3L, "C", "D", 'L', "Love"))
        repo.add(HistoryEntry(2L, "E", "F", 'S', "Siblings"))

        val listed = repo.list()
        // maxItems=2, keep newest by timestamp: 3 then 2
        assertEquals(2, listed.size)
        assertEquals(3L, listed[0].timestampMs)
        assertEquals(2L, listed[1].timestampMs)
    }

    @Test
    fun clear_emptiesStore() {
        val store = InMemoryStore()
        val repo = HistoryRepository(store)

        repo.add(HistoryEntry(1L, "A", "B", 'F', "Friends"))
        repo.clear()

        assertEquals(emptyList<HistoryEntry>(), repo.list())
    }
}
