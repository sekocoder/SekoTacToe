package com.example.seko_tac_toe.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.seko_tac_toe.R
import com.example.seko_tac_toe.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

const val REQUEST_CODE_SIGN_IN = 0

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var btnGoogleSignIn: Button
    private lateinit var dbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()
        dbref =
            FirebaseDatabase.getInstance("https://seko-tac-toe-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .reference

        btnGoogleSignIn = findViewById(R.id.signInButton)

        btnGoogleSignIn.setOnClickListener {
            val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.webclient_id))
                .requestEmail()
                .build()
            val signInClient = GoogleSignIn.getClient(this, options)
            signInClient.signInIntent.also {
                startActivityForResult(it, REQUEST_CODE_SIGN_IN)
            }
        }

    }

    private fun addUsertodb() {
        Handler(Looper.myLooper()!!).postDelayed({
            val winNumber = 0
            val matchNumber = 0
            val uidCheck = auth.currentUser?.uid.toString()
            Toast.makeText(this, uidCheck, Toast.LENGTH_LONG).show()
            dbref =
                FirebaseDatabase.getInstance("https://seko-tac-toe-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
            dbref.child("user").child(auth.currentUser!!.uid)
                .setValue(User(winNumber, matchNumber, uidCheck))

        }, 2500)

    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        try {
            auth.signInWithCredential(credentials)
            addUsertodb()
            Toast.makeText(this, "Signed In", Toast.LENGTH_LONG).show()
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let {
                googleAuthForFirebase(it)
            }
        }
    }


}