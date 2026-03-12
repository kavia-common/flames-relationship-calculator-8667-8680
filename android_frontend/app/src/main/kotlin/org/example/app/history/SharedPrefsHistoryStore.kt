package org.example.app.history

import android.content.Context

/**
 * SharedPreferences-backed history store.
 */
class SharedPrefsHistoryStore(context: Context) : HistoryStore {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // PUBLIC_INTERFACE
    override fun load(): List<HistoryEntry> {
        /** Loads persisted history entries. */
        val raw = prefs.getString(KEY_HISTORY, "") ?: ""
        return HistorySerializer.deserialize(raw)
    }

    // PUBLIC_INTERFACE
    override fun save(entries: List<HistoryEntry>) {
        /** Persists the given history entries. */
        prefs.edit().putString(KEY_HISTORY, HistorySerializer.serialize(entries)).apply()
    }

    companion object {
        private const val PREFS_NAME = "flames_prefs"
        private const val KEY_HISTORY = "history_json"
    }
}
