package org.example.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle

/**
 * Legacy entry activity kept for compatibility with earlier template.
 * The real launcher is SplashActivity.
 */
class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, PlayActivity::class.java))
        finish()
    }
}
