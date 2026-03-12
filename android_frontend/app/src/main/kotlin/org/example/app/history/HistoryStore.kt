package org.example.app.history

/**
 * Simple storage abstraction to allow unit testing without Android framework.
 */
interface HistoryStore {
    // PUBLIC_INTERFACE
    fun load(): List<HistoryEntry>
    // PUBLIC_INTERFACE
    fun save(entries: List<HistoryEntry>)
}
