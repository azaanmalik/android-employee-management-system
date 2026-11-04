package com.example.employeemanager

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.employeemanager.ui.MainActivity
import android.widget.TextView

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_EmployeeManager_Splash)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.logoImage)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val appName = findViewById<TextView>(R.id.splash_app_name)

        // Animate logo
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.logo_fade_in_scale)
        logo.startAnimation(fadeIn)
        logo.animate().alpha(1f).setDuration(800).start()

        // Animate app name fade-in (after logo)
        appName.alpha = 0f
        appName.postDelayed({
            appName.animate().alpha(1f).setDuration(600).start()
        }, 700)

        // Animate progress bar fill over 2 seconds
        val duration = 2300L
        val handler = Handler(Looper.getMainLooper())
        val startTime = System.currentTimeMillis()
        val update = object : Runnable {
            override fun run() {
                val elapsed = System.currentTimeMillis() - startTime
                val progress = (elapsed * 100 / duration).toInt().coerceAtMost(100)
                progressBar.progress = progress
                if (progress < 100) {
                    handler.postDelayed(this, 16)
                }
            }
        }
        handler.post(update)

        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, duration)
    }
}