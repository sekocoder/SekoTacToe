package com.example.seko_tac_toe.activity

import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.seko_tac_toe.R
import com.example.seko_tac_toe.databinding.ActivityGameBoardBinding
import com.example.seko_tac_toe.model.User
import com.example.seko_tac_toe.viewmodel.GameBoardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class GameBoardActivity : AppCompatActivity() {

    private lateinit var user: Map<String, Int>
    private lateinit var binding: ActivityGameBoardBinding
    private lateinit var viewModel: GameBoardViewModel
    private lateinit var mAuth: FirebaseAuth
    private lateinit var currentUser: User
    private lateinit var dbref: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(GameBoardViewModel::class.java)

        viewModel.gameMode = intent.getStringExtra("Game Mode Tag").toString()
        viewModel.symbolSelected = intent.getStringExtra("Symbol Tag").toString()

        dbref =
            FirebaseDatabase.getInstance("https://seko-tac-toe-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .reference

        if (viewModel.gameMode == "Single Player") {

            dbref.child("user").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (postSnapshot in snapshot.children) {
                        currentUser = postSnapshot.getValue(User::class.java)!!
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
            boardGone()

            Handler(Looper.myLooper()!!).postDelayed({

                boardVisible()

                viewModel.calculateWinRate(currentUser.winNumber!!, currentUser.matchNumber!!)


                displayWinRate()

                if (viewModel.roundEndFlag == 1 && viewModel.resetFlag == 0) {
                    retainLine()
                }

                if (viewModel.resetFlag == 1) {
                    resetGame()
                }
            }, 2500)
        } else {
            checkWin()

        }
        binding.btn1.text = viewModel.btn1
        setColor(binding.btn1)
        binding.btn2.text = viewModel.btn2
        setColor(binding.btn2)
        binding.btn3.text = viewModel.btn3
        setColor(binding.btn3)
        binding.btn4.text = viewModel.btn4
        setColor(binding.btn4)
        binding.btn5.text = viewModel.btn5
        setColor(binding.btn5)
        binding.btn6.text = viewModel.btn6
        setColor(binding.btn6)
        binding.btn7.text = viewModel.btn7
        setColor(binding.btn7)
        binding.btn8.text = viewModel.btn8
        setColor(binding.btn8)
        binding.btn9.text = viewModel.btn9
        setColor(binding.btn9)

        if (viewModel.resetFlag == 1) {
            resetGame()
        }


    }

    private fun boardGone() {
        binding.resetGridButton.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun boardVisible() {

        binding.resetGridButton.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun displayWinRate() {
        binding.winRateText.text = "Win Percentage : " + viewModel.winRatedb.toString() + " %"
        binding.winRateText.visibility = View.VISIBLE
    }

    private fun setColor(currentBtn: Button) {
        if (currentBtn.text == viewModel.symbolSelected) {
            currentBtn.background.setColorFilter(
                getColor(android.R.color.holo_green_light),
                PorterDuff.Mode.MULTIPLY
            )
        } else if (currentBtn.text != "") {
            currentBtn.background.setColorFilter(
                getColor(android.R.color.holo_orange_light),
                PorterDuff.Mode.MULTIPLY
            )
        }
    }

    private fun autoMove() {

        if (binding.btn1.text != "") {
            viewModel.emptyTiles.remove(1)
        } else if (binding.btn2.text != "") {
            viewModel.emptyTiles.remove(2)
        } else if (binding.btn3.text != "") {
            viewModel.emptyTiles.remove(3)
        } else if (binding.btn4.text != "") {
            viewModel.emptyTiles.remove(4)
        } else if (binding.btn5.text != "") {
            viewModel.emptyTiles.remove(5)
        } else if (binding.btn6.text != "") {
            viewModel.emptyTiles.remove(6)
        } else if (binding.btn7.text != "") {
            viewModel.emptyTiles.remove(7)
        } else if (binding.btn8.text != "") {
            viewModel.emptyTiles.remove(8)
        } else if (binding.btn9.text != "") {
            viewModel.emptyTiles.remove(9)
        }

        when (viewModel.emptyTiles.random()) {
            1 -> onTileClick(binding.btn1)
            2 -> onTileClick(binding.btn2)
            3 -> onTileClick(binding.btn3)
            4 -> onTileClick(binding.btn4)
            5 -> onTileClick(binding.btn5)
            6 -> onTileClick(binding.btn6)
            7 -> onTileClick(binding.btn7)
            8 -> onTileClick(binding.btn8)
            else -> onTileClick(binding.btn9)
        }
    }

    fun onTileClick(view: View) {
        val currentBtn = view as Button

        if (currentBtn.text == "" && viewModel.roundEndFlag == 0) {

            viewModel.setResetFlagTo0()
            viewModel.countIncrement()

            if (viewModel.flag == 0) {
                currentBtn.background.setColorFilter(
                    getColor(android.R.color.holo_green_light),
                    PorterDuff.Mode.MULTIPLY
                )

                if (viewModel.symbolSelected == "X") {
                    currentBtn.text = "X"
                } else {
                    currentBtn.text = "O"
                }
                viewModel.xSelected()

            } else {
                currentBtn.background.setColorFilter(
                    getColor(android.R.color.holo_orange_light),
                    PorterDuff.Mode.MULTIPLY
                )

                if (viewModel.symbolSelected != "X") {
                    currentBtn.text = "X"
                } else {
                    currentBtn.text = "O"
                }
                viewModel.oSelected()

            }

            val btn1 = binding.btn1.text.toString()
            val btn2 = binding.btn2.text.toString()
            val btn3 = binding.btn3.text.toString()
            val btn4 = binding.btn4.text.toString()
            val btn5 = binding.btn5.text.toString()
            val btn6 = binding.btn6.text.toString()
            val btn7 = binding.btn7.text.toString()
            val btn8 = binding.btn8.text.toString()
            val btn9 = binding.btn9.text.toString()

            viewModel.symbolAssign(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9)
            checkWin()
        }

        if (viewModel.flag == 1 && viewModel.gameMode == "Single Player" && viewModel.roundEndFlag == 0) {
            autoMove()
        }

    }

    private fun retainLine() {
        if (viewModel.btn1 == viewModel.btn2 && viewModel.btn2 == viewModel.btn3 && viewModel.btn3 != "") {
            binding.rowOneLine.visibility = View.VISIBLE

        } else if (viewModel.btn4 == viewModel.btn5 && viewModel.btn5 == viewModel.btn6 && viewModel.btn6 != "") {
            binding.rowTwoLine.visibility = View.VISIBLE
        } else if (viewModel.btn7 == viewModel.btn8 && viewModel.btn8 == viewModel.btn9 && viewModel.btn9 != "") {
            binding.rowThreeLine.visibility = View.VISIBLE
        } else if (viewModel.btn1 == viewModel.btn4 && viewModel.btn4 == viewModel.btn7 && viewModel.btn7 != "") {
            binding.columnOneLine.visibility = View.VISIBLE
        } else if (viewModel.btn2 == viewModel.btn5 && viewModel.btn5 == viewModel.btn8 && viewModel.btn8 != "") {
            binding.columnTwoLine.visibility = View.VISIBLE
        } else if (viewModel.btn3 == viewModel.btn6 && viewModel.btn6 == viewModel.btn9 && viewModel.btn9 != "") {
            binding.columnThreeLine.visibility = View.VISIBLE
        } else if (viewModel.btn1 == viewModel.btn5 && viewModel.btn5 == viewModel.btn9 && viewModel.btn9 != "") {
            binding.diagonalOneLine.visibility = View.VISIBLE
        } else if (viewModel.btn3 == viewModel.btn5 && viewModel.btn5 == viewModel.btn7 && viewModel.btn7 != "") {
            binding.diagonalTwoLine.visibility = View.VISIBLE

        }
    }

    private fun checkWin() {

        if (viewModel.btn1 == viewModel.btn2 && viewModel.btn2 == viewModel.btn3 && viewModel.btn3 != "") {
            binding.rowOneLine.visibility = View.VISIBLE
            displayWinner(viewModel.btn1)
        } else if (viewModel.btn4 == viewModel.btn5 && viewModel.btn5 == viewModel.btn6 && viewModel.btn6 != "") {
            binding.rowTwoLine.visibility = View.VISIBLE
            displayWinner(viewModel.btn4)
        } else if (viewModel.btn7 == viewModel.btn8 && viewModel.btn8 == viewModel.btn9 && viewModel.btn9 != "") {
            binding.rowThreeLine.visibility = View.VISIBLE
            displayWinner(viewModel.btn7)
        } else if (viewModel.btn1 == viewModel.btn4 && viewModel.btn4 == viewModel.btn7 && viewModel.btn7 != "") {
            binding.columnOneLine.visibility = View.VISIBLE
            displayWinner(viewModel.btn1)
        } else if (viewModel.btn2 == viewModel.btn5 && viewModel.btn5 == viewModel.btn8 && viewModel.btn8 != "") {
            binding.columnTwoLine.visibility = View.VISIBLE
            displayWinner(viewModel.btn2)
        } else if (viewModel.btn3 == viewModel.btn6 && viewModel.btn6 == viewModel.btn9 && viewModel.btn9 != "") {
            binding.columnThreeLine.visibility = View.VISIBLE
            displayWinner(viewModel.btn3)
        } else if (viewModel.btn1 == viewModel.btn5 && viewModel.btn5 == viewModel.btn9 && viewModel.btn9 != "") {
            binding.diagonalOneLine.visibility = View.VISIBLE
            displayWinner(viewModel.btn1)
        } else if (viewModel.btn3 == viewModel.btn5 && viewModel.btn5 == viewModel.btn7 && viewModel.btn7 != "") {
            binding.diagonalTwoLine.visibility = View.VISIBLE
            displayWinner(viewModel.btn3)
        } else if (viewModel.count == 9) {
            viewModel.roundEndFlagSetToOne()
            binding.gameResultText.text = getString(R.string.draw)
            binding.gameResultText.visibility = View.VISIBLE
            binding.gameResultText.background.setColorFilter(
                getColor(android.R.color.darker_gray),
                PorterDuff.Mode.MULTIPLY
            )
            if (viewModel.gameMode == "Single Player") {
                updateWinNMatchCount(0)
                boardGone()

                Handler(Looper.myLooper()!!).postDelayed({

                    boardVisible()

                    viewModel.calculateWinRate(currentUser.winNumber!!, currentUser.matchNumber!!)
                    displayWinRate()

                }, 2500)
            }

        }

    }

    private fun updateWinNMatchCount(both: Int) {

        mAuth = FirebaseAuth.getInstance()
        dbref =
            FirebaseDatabase.getInstance("https://seko-tac-toe-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("user")

        if (both == 1) {
            user = mapOf(
                "winNumber" to currentUser.winNumber!! + 1,
                "matchNumber" to currentUser.matchNumber!! + 1
            )
        } else if (both == 0) {
            user = mapOf(
                "matchNumber" to currentUser.matchNumber!! + 1
            )
        }
        dbref.child(mAuth.currentUser!!.uid).updateChildren(user)

    }

    private fun displayWinner(symbol: String) {


        viewModel.roundEndFlagSetToOne()

        if (symbol == viewModel.symbolSelected) {
            if (viewModel.gameMode == "Single Player") {
                binding.gameResultText.text = getString(R.string.you_win)
                updateWinNMatchCount(1)

            } else {
                binding.gameResultText.text = getString(R.string.player_1_win)
            }
            binding.gameResultText.visibility = View.VISIBLE
            binding.gameResultText.background.setColorFilter(
                getColor(android.R.color.holo_green_light),
                PorterDuff.Mode.MULTIPLY
            )

        } else {
            if (viewModel.gameMode == "Single Player") {
                binding.gameResultText.text = getString(R.string.opponent_win)
                updateWinNMatchCount(0)
            } else {
                binding.gameResultText.text = getString(R.string.player_2_win)
            }
            binding.gameResultText.visibility = View.VISIBLE
            binding.gameResultText.background.setColorFilter(
                getColor(android.R.color.holo_orange_light),
                PorterDuff.Mode.MULTIPLY
            )
        }

        if (viewModel.gameMode == "Single Player") {
            boardGone()

            Handler(Looper.myLooper()!!).postDelayed({

                boardVisible()

                viewModel.calculateWinRate(currentUser.winNumber!!, currentUser.matchNumber!!)
                displayWinRate()

            }, 2500)
        }
    }

    fun manualReset(view: View) {
        viewModel.setResetFlagTo1()
        resetGame()
    }

    private fun resetGame() {

        viewModel.resetFlagNCount()

        binding.btn1.text = ""
        binding.btn1.background.setColorFilter(
            getColor(R.color.white),
            PorterDuff.Mode.MULTIPLY
        )
        binding.btn2.text = ""
        binding.btn2.background.setColorFilter(
            getColor(R.color.white),
            PorterDuff.Mode.MULTIPLY
        )
        binding.btn3.text = ""
        binding.btn3.background.setColorFilter(
            getColor(R.color.white),
            PorterDuff.Mode.MULTIPLY
        )
        binding.btn4.text = ""
        binding.btn4.background.setColorFilter(
            getColor(R.color.white),
            PorterDuff.Mode.MULTIPLY
        )
        binding.btn5.text = ""
        binding.btn5.background.setColorFilter(
            getColor(R.color.white),
            PorterDuff.Mode.MULTIPLY
        )
        binding.btn6.text = ""
        binding.btn6.background.setColorFilter(
            getColor(R.color.white),
            PorterDuff.Mode.MULTIPLY
        )
        binding.btn7.text = ""
        binding.btn7.background.setColorFilter(
            getColor(R.color.white),
            PorterDuff.Mode.MULTIPLY
        )
        binding.btn8.text = ""
        binding.btn8.background.setColorFilter(
            getColor(R.color.white),
            PorterDuff.Mode.MULTIPLY
        )
        binding.btn9.text = ""
        binding.btn9.background.setColorFilter(
            getColor(R.color.white),
            PorterDuff.Mode.MULTIPLY
        )

        binding.rowOneLine.visibility = View.GONE
        binding.rowTwoLine.visibility = View.GONE
        binding.rowThreeLine.visibility = View.GONE
        binding.columnOneLine.visibility = View.GONE
        binding.columnTwoLine.visibility = View.GONE
        binding.columnThreeLine.visibility = View.GONE
        binding.diagonalOneLine.visibility = View.GONE
        binding.diagonalTwoLine.visibility = View.GONE

        viewModel.resetEndFlagNTiles()
        binding.gameResultText.visibility = View.GONE
    }

}