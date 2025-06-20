package com.example.gymbuddy.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.gymbuddy.databinding.ActivitySplashBinding // Import binding untuk layout splash
import com.example.gymbuddy.ui.login.LoginActivity // Import LoginActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val SPLASH_TIME_OUT: Long = 2000 // Durasi splash screen dalam milidetik (2 detik)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Sembunyikan Action Bar untuk tampilan splash penuh
        supportActionBar?.hide()

        // Handler untuk menunda navigasi ke LoginActivity setelah durasi SPLASH_TIME_OUT
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Tutup SplashActivity agar pengguna tidak bisa kembali ke sana dengan tombol 'back'
        }, SPLASH_TIME_OUT)
    }
}