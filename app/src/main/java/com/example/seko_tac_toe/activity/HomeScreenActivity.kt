package com.example.seko_tac_toe.activity

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.seko_tac_toe.databinding.ActivityHomeScreenBinding
import com.example.seko_tac_toe.viewmodel.HomeScreenViewModel
import com.google.firebase.auth.FirebaseAuth

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var gameMode: String
    private lateinit var viewModel: HomeScreenViewModel
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.signOutButton.visibility =View.GONE

        mAuth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(this).get(HomeScreenViewModel::class.java)

        if (viewModel.symbolSelected == "X") {
            selectCrossUI(binding.crossButton)
        } else if (viewModel.symbolSelected == "O") {
            selectCircleUI(binding.circleButton)
        } else {
            binding.singlePlayerButton.visibility = View.GONE
            binding.multiPlayerPlayerButton.visibility = View.GONE
            binding.crossSymbolText.visibility = View.GONE
            binding.crossBorderButton.visibility = View.GONE
            binding.crossButton.visibility = View.GONE
            binding.circleButton.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            Handler(Looper.myLooper()!!).postDelayed({
                binding.singlePlayerButton.visibility = View.VISIBLE
                binding.multiPlayerPlayerButton.visibility = View.VISIBLE
                binding.crossButton.visibility = View.VISIBLE
                binding.circleButton.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }, 2500)

        }

    }

    fun selectCrossUI(view: View) {
        viewModel.selectCross()
        binding.crossButton.background.setColorFilter(
            getColor(android.R.color.holo_green_light),
            PorterDuff.Mode.MULTIPLY
        )
        binding.circleButton.background.setColorFilter(
            getColor(android.R.color.holo_orange_light),
            PorterDuff.Mode.MULTIPLY
        )
        binding.crossBorderButton.visibility = View.VISIBLE
        binding.crossSymbolText.visibility = View.VISIBLE
        binding.circleBorderButton.visibility = View.GONE
        binding.circleSymbolText.visibility = View.GONE
    }

    fun selectCircleUI(view: View) {
        viewModel.selectCircle()
        binding.crossButton.background.setColorFilter(
            getColor(android.R.color.holo_orange_light),
            PorterDuff.Mode.MULTIPLY
        )
        binding.circleButton.background.setColorFilter(
            getColor(android.R.color.holo_green_light),
            PorterDuff.Mode.MULTIPLY
        )
        binding.circleBorderButton.visibility = View.VISIBLE
        binding.circleSymbolText.visibility = View.VISIBLE
        binding.crossBorderButton.visibility = View.GONE
        binding.crossSymbolText.visibility = View.GONE
    }

    fun selectSinglePlayerMode(view: View) {
        if (viewModel.symbolSelected == "symbol") {
            Toast.makeText(this, "Select a Symbol", Toast.LENGTH_LONG).show()
        } else {
            gameMode = "Single Player"
            val intent = Intent(this, GameBoardActivity::class.java)
            intent.putExtra("Game Mode Tag", gameMode)
            intent.putExtra("Symbol Tag", viewModel.symbolSelected)
            startActivity(intent)
        }

    }

    fun selectMultiplePlayerMode(view: View) {
        if (viewModel.symbolSelected == "symbol") {
            Toast.makeText(this, "Select a Symbol", Toast.LENGTH_LONG).show()
        } else {
            gameMode = "Multi Player"
            val intent = Intent(this, GameBoardActivity::class.java)
            intent.putExtra("Game Mode Tag", gameMode)
            intent.putExtra("Symbol Tag", viewModel.symbolSelected)
            startActivity(intent)
        }
    }

    fun signOut(view: View) {
        mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

}