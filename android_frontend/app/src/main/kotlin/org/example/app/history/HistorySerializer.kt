package org.example.app.history

/**
 * Tiny JSON-ish serializer to avoid adding dependencies under the Gradle .dcl constraints.
 *
 * Format:
 * [
 *  {"t":123,"n1":"A","n2":"B","l":"F","lbl":"Friends"}
 * ]
 */
object HistorySerializer {

    // PUBLIC_INTERFACE
    fun serialize(entries: List<HistoryEntry>): String {
        /** Serializes history entries into a compact JSON string. */
        return entries.joinToString(prefix = "[", postfix = "]", separator = ",") { e ->
            val n1 = escape(e.name1)
            val n2 = escape(e.name2)
            val lbl = escape(e.relationLabel)
            """{"t":${e.timestampMs},"n1":"$n1","n2":"$n2","l":"${e.relationLetter}","lbl":"$lbl"}"""
        }
    }

    // PUBLIC_INTERFACE
    fun deserialize(raw: String): List<HistoryEntry> {
        /** Parses the serialized history string. Returns empty list for blank/invalid inputs. */
        val s = raw.trim()
        if (s.isEmpty() || s == "[]") return emptyList()
        if (!s.startsWith("[") || !s.endsWith("]")) return emptyList()

        val body = s.substring(1, s.length - 1).trim()
        if (body.isEmpty()) return emptyList()

        // Split objects by top-level commas between {...},{...}
        val objects = splitTopLevelObjects(body)
        val out = ArrayList<HistoryEntry>(objects.size)
        for (obj in objects) {
            parseObject(obj)?.let { out.add(it) }
        }
        return out
    }

    private fun parseObject(obj: String): HistoryEntry? {
        val t = readLong(obj, """"t":""") ?: return null
        val n1 = readString(obj, """"n1":""") ?: return null
        val n2 = readString(obj, """"n2":""") ?: return null
        val lStr = readString(obj, """"l":""") ?: return null
        val lbl = readString(obj, """"lbl":""") ?: return null
        if (lStr.length != 1) return null

        return HistoryEntry(
            timestampMs = t,
            name1 = unescape(n1),
            name2 = unescape(n2),
            relationLetter = lStr[0],
            relationLabel = unescape(lbl)
        )
    }

    private fun readLong(obj: String, key: String): Long? {
        val idx = obj.indexOf(key)
        if (idx < 0) return null
        var i = idx + key.length
        // key already includes ":"; read until comma or }
        val end = findNumberEnd(obj, i)
        return obj.substring(i, end).toLongOrNull()
    }

    private fun findNumberEnd(s: String, start: Int): Int {
        var i = start
        while (i < s.length && (s[i].isDigit() || s[i] == '-')) i++
        return i
    }

    private fun readString(obj: String, key: String): String? {
        val idx = obj.indexOf(key)
        if (idx < 0) return null
        var i = idx + key.length
        if (i >= obj.length || obj[i] != '"') return null
        i++
        val sb = StringBuilder()
        var escaped = false
        while (i < obj.length) {
            val c = obj[i]
            if (escaped) {
                sb.append(c)
                escaped = false
            } else if (c == '\\') {
                escaped = true
            } else if (c == '"') {
                return sb.toString()
            } else {
                sb.append(c)
            }
            i++
        }
        return null
    }

    private fun splitTopLevelObjects(body: String): List<String> {
        val result = ArrayList<String>()
        var depth = 0
        var start = 0
        for (i in body.indices) {
            val c = body[i]
            if (c == '{') depth++
            if (c == '}') depth--
            if (c == ',' && depth == 0) {
                result.add(body.substring(start, i).trim())
                start = i + 1
            }
        }
        result.add(body.substring(start).trim())
        return result.filter { it.isNotEmpty() }
    }

    private fun escape(s: String): String {
        return s
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
    }

    private fun unescape(s: String): String {
        // Minimal unescape for our own escape() format.
        val out = StringBuilder()
        var escaped = false
        for (c in s) {
            if (escaped) {
                out.append(c)
                escaped = false
            } else if (c == '\\') {
                escaped = true
            } else {
                out.append(c)
            }
        }
        return out.toString()
    }
}
