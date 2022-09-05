package com.example.seko_tac_toe.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.seko_tac_toe.R
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        mAuth = FirebaseAuth.getInstance()

        Handler(Looper.myLooper()!!).postDelayed({
            if (mAuth.currentUser != null) {
                val intent = Intent(this, HomeScreenActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 800)

    }
}