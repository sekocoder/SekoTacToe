package com.example.seko_tac_toe.model

class User{
    var winNumber: Int? = null
    var matchNumber: Int? = null
    var uidCheck: String? = null

    constructor(){}

    constructor(winNumber: Int?, matchNumber: Int?, uidCheck: String?){
        this.winNumber = winNumber
        this.matchNumber = matchNumber
        this.uidCheck = uidCheck
    }
}
