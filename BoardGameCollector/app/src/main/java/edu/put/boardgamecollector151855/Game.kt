package edu.put.boardgamecollector151855

class Game {
    var id:Int=0
    var title: String? = null
    var year:Int=0
    var miniphoto: String? = null

    constructor(id : Int, title : String, year : Int, miniphoto : String)
    {
        this.id = id
        this.title = title
        this.year = year
        this.miniphoto = miniphoto
    }

    fun getID() : Int{
        return id
    }


    fun getName() : String? {
        return title
    }

    fun getPhoto() : String? {
        return miniphoto
    }

    fun getPubYear() : Int {
        return year
    }
}