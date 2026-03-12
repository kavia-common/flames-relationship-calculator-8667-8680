package org.example.app.history

/**
 * Represents one FLAMES play result stored in local history.
 */
data class HistoryEntry(
    val timestampMs: Long,
    val name1: String,
    val name2: String,
    val relationLetter: Char,
    val relationLabel: String
)
