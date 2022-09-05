package com.example.seko_tac_toe.viewmodel

import androidx.lifecycle.ViewModel

class GameBoardViewModel : ViewModel() {

    lateinit var gameMode: String
    lateinit var symbolSelected: String
    var winRatedb :Double = 0.0
    var flag = 0
    var roundEndFlag = 0
    var resetFlag = 0
    var count = 0
    var emptyTiles = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
    var btn1 = ""
    var btn2 = ""
    var btn3 = ""
    var btn4 = ""
    var btn5 = ""
    var btn6 = ""
    var btn7 = ""
    var btn8 = ""
    var btn9 = ""

    fun calculateWinRate(dbWinCount: Int,dbMatchCount: Int) {
        if(dbMatchCount != 0){
            winRatedb = ((dbWinCount.toDouble()/dbMatchCount)*100)
        }
        else{
            winRatedb = 0.0
        }

    }

    fun setResetFlagTo1(){
        resetFlag = 1
    }

    fun setResetFlagTo0(){
        resetFlag = 0
    }

    fun symbolAssign(bt1: String, bt2: String, bt3: String, bt4: String,
        bt5: String, bt6: String, bt7: String, bt8: String, bt9: String
    ) {
        btn1 = bt1
        btn2 = bt2
        btn3 = bt3
        btn4 = bt4
        btn5 = bt5
        btn6 = bt6
        btn7 = bt7
        btn8 = bt8
        btn9 = bt9
    }

    fun countIncrement() {
        count++
    }

    fun xSelected() {
        flag = 1
    }

    fun oSelected() {
        flag = 0
    }

    fun roundEndFlagSetToOne() {
        roundEndFlag = 1
    }

    fun resetFlagNCount() {
        flag = 0
        count = 0
    }

    fun resetEndFlagNTiles() {
        roundEndFlag = 0
        emptyTiles = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
    }

}