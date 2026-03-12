package org.example.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity(Intent(this, PlayActivity::class.java))
                finish()
            },
            SPLASH_DELAY_MS
        )
    }

    companion object {
        private const val SPLASH_DELAY_MS = 700L
    }
}
