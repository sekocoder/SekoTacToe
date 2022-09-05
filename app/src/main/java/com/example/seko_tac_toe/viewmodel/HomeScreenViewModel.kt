package com.example.seko_tac_toe.viewmodel

import androidx.lifecycle.ViewModel

class HomeScreenViewModel : ViewModel() {

    var symbolSelected = "symbol"

    fun selectCross() {
        symbolSelected = "X"
    }

    fun selectCircle() {
        symbolSelected = "O"
    }
}