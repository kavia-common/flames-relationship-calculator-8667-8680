package org.example.app

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import org.example.app.history.HistoryEntry
import org.example.app.history.HistoryRepository
import org.example.app.history.SharedPrefsHistoryStore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryActivity : Activity() {

    private lateinit var listView: ListView
    private lateinit var tvEmpty: TextView
    private lateinit var btnClearHistory: TextView

    private lateinit var repo: HistoryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        repo = HistoryRepository(SharedPrefsHistoryStore(this))

        listView = findViewById(R.id.listHistory)
        tvEmpty = findViewById(R.id.tvEmpty)
        btnClearHistory = findViewById(R.id.btnClearHistory)

        btnClearHistory.setOnClickListener {
            repo.clear()
            render()
        }
    }

    override fun onResume() {
        super.onResume()
        render()
    }

    private fun render() {
        val items = repo.list()
        if (items.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
            listView.visibility = View.GONE
        } else {
            tvEmpty.visibility = View.GONE
            listView.visibility = View.VISIBLE
            listView.adapter = HistoryAdapter(items)
        }
    }

    private class HistoryAdapter(private val items: List<HistoryEntry>) : BaseAdapter() {

        private val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        override fun getCount(): Int = items.size
        override fun getItem(position: Int): Any = items[position]
        override fun getItemId(position: Int): Long = items[position].timestampMs

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val inflater = LayoutInflater.from(parent?.context)
            val view = convertView ?: inflater.inflate(R.layout.item_history, parent, false)

            val entry = items[position]
            val tvNames = view.findViewById<TextView>(R.id.tvNames)
            val tvMeta = view.findViewById<TextView>(R.id.tvMeta)

            tvNames.text = "${entry.name1} × ${entry.name2}"
            tvMeta.text = "${entry.relationLetter} — ${entry.relationLabel} • ${fmt.format(Date(entry.timestampMs))}"

            return view
        }
    }
}
