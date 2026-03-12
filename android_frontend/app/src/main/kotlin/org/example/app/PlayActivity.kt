package org.example.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.example.app.flames.FlamesCalculator
import org.example.app.history.HistoryEntry
import org.example.app.history.HistoryRepository
import org.example.app.history.SharedPrefsHistoryStore

class PlayActivity : Activity() {

    private lateinit var etName1: EditText
    private lateinit var etName2: EditText
    private lateinit var tvResultValue: TextView
    private lateinit var tvResultDetail: TextView

    private lateinit var historyRepo: HistoryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        historyRepo = HistoryRepository(SharedPrefsHistoryStore(this))

        etName1 = findViewById(R.id.etName1)
        etName2 = findViewById(R.id.etName2)
        tvResultValue = findViewById(R.id.tvResultValue)
        tvResultDetail = findViewById(R.id.tvResultDetail)

        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val btnClear = findViewById<Button>(R.id.btnClear)
        val btnOpenHistory = findViewById<TextView>(R.id.btnOpenHistory)

        btnCalculate.setOnClickListener { onCalculate() }
        btnClear.setOnClickListener { onClear() }
        btnOpenHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    private fun onCalculate() {
        val raw1 = etName1.text?.toString() ?: ""
        val raw2 = etName2.text?.toString() ?: ""

        try {
            val relation = FlamesCalculator.calculate(raw1, raw2)
            tvResultValue.text = "${relation.letter} — ${relation.label}"
            tvResultDetail.text = relation.description

            historyRepo.add(
                HistoryEntry(
                    timestampMs = System.currentTimeMillis(),
                    name1 = raw1.trim(),
                    name2 = raw2.trim(),
                    relationLetter = relation.letter,
                    relationLabel = relation.label
                )
            )
        } catch (e: IllegalArgumentException) {
            Toast.makeText(this, getString(R.string.error_enter_names), Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClear() {
        etName1.setText("")
        etName2.setText("")
        tvResultValue.text = "—"
        tvResultDetail.text = ""
        etName1.requestFocus()
    }
}
