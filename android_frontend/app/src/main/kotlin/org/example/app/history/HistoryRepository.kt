package org.example.app.history

/**
 * Repository managing history list operations.
 */
class HistoryRepository(
    private val store: HistoryStore,
    private val maxItems: Int = 50
) {
    // PUBLIC_INTERFACE
    fun list(): List<HistoryEntry> {
        /** Returns the current history (newest first). */
        return store.load().sortedByDescending { it.timestampMs }
    }

    // PUBLIC_INTERFACE
    fun add(entry: HistoryEntry) {
        /** Adds an entry and persists, keeping newest items and applying maxItems cap. */
        val existing = store.load().toMutableList()
        existing.add(entry)
        val trimmed = existing
            .sortedByDescending { it.timestampMs }
            .take(maxItems)
        store.save(trimmed)
    }

    // PUBLIC_INTERFACE
    fun clear() {
        /** Clears persisted history. */
        store.save(emptyList())
    }
}
