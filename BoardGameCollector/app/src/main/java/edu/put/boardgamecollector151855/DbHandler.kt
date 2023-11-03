package edu.put.boardgamecollector151855

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHandler(context: Context,name:String?,factory:SQLiteDatabase.CursorFactory?,version:Int) : SQLiteOpenHelper(context,
    DATABASE_NAME,factory, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_VERSION=1
        private const val DATABASE_NAME ="gamesDB.db"
        const val TABLE_GAMES="games"
        const val COLUMN_ID="_id"
        const val COLUMN_TITLE="title"
        const val COLUMN_YEAR="year"
        const val COLUMN_MINIPHOTO="miniphoto"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createGamesTable=("CREATE TABLE $TABLE_GAMES($COLUMN_ID INTEGER PRIMARY KEY,$COLUMN_TITLE TEXT,$COLUMN_YEAR INTEGER,$COLUMN_MINIPHOTO TEXT)")
        db.execSQL(createGamesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES")
        onCreate(db)
    }

    fun addGame(game:Game) {
        val values=ContentValues()
        values.put(COLUMN_ID,game.id)
        values.put(COLUMN_TITLE,game.title)
        values.put(COLUMN_YEAR,game.year)
        values.put(COLUMN_MINIPHOTO,game.miniphoto)

        val db=this.writableDatabase
        db.insert(TABLE_GAMES,null,values)
        db.close()
    }

    fun findGames() : MutableList<Game> {
        return findResults("SELECT * FROM $TABLE_GAMES WHERE $COLUMN_TITLE NOT LIKE '%:%' ORDER BY $COLUMN_TITLE")
    }

    fun findAddons() : MutableList<Game> {
        return findResults("SELECT * FROM $TABLE_GAMES WHERE $COLUMN_TITLE LIKE '%:%' ORDER BY $COLUMN_TITLE")
    }

    fun findResultByName(gameName : String) : Game? {
        return findResult("SELECT * FROM $TABLE_GAMES WHERE $COLUMN_TITLE like '$gameName'")
    }

    private fun findResult(query : String) : Game? {
        val db=this.writableDatabase

        val cursor= db.rawQuery(query,null)
        var game : Game? = null
        if(cursor.moveToFirst()){
            game = getResultFromRow(cursor)
            cursor.close()
        }
        db.close()
        return game
    }

    private fun findResults(query: String): MutableList<Game> {
        val list = mutableListOf<Game>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        if(cursor.moveToFirst()){
            var game = getResultFromRow(cursor)
            list.add(game)
        }
        while (cursor.moveToNext())
        {
            var game = getResultFromRow(cursor)
            list.add(game)
        }
        cursor.close()
        db.close()

        return list
    }

    private fun getResultFromRow(cursor : Cursor) : Game {
        val id = Integer.parseInt(cursor.getString(0))
        val title = cursor.getString(1)
        val year = Integer.parseInt(cursor.getString(2))
        val miniphoto = cursor.getString(3)
        return Game(id,title,year,miniphoto)
    }

    fun deleteResult(gameName : String) : Boolean {
        var result = false
        val query = "SELECT * FROM $TABLE_GAMES WHERE $COLUMN_TITLE like '$gameName'"
        val db=this.writableDatabase

        val cursor = db.rawQuery(query,null)
        if(cursor.moveToFirst())
        {
            val id = cursor.getInt(0)
            db.delete(TABLE_GAMES, "$COLUMN_ID = ?", arrayOf(id.toString()))
            cursor.close()
            result = true
        }
        db.close()
        return result
    }

    fun deleteAll(): Boolean {
        var result = false
        val query = "SELECT * FROM $TABLE_GAMES "
        val db=this.writableDatabase


        val cursor = db.rawQuery(query,null)
        if(cursor.moveToFirst())
        {
            val id = cursor.getInt(0)
            db.delete(TABLE_GAMES, null,null)
            cursor.close()
            result = true
        }
        db.close()
        return result
    }

    fun countAddons(): Int {
        val query = "SELECT COUNT(*) FROM $TABLE_GAMES WHERE $COLUMN_TITLE LIKE '%:%'"
        return count(query)
    }

    fun countGames(): Int {
        val query = "SELECT COUNT(*) FROM $TABLE_GAMES WHERE $COLUMN_TITLE NOT LIKE '%:%'"
        return count(query)
    }

    private fun count(query : String) : Int {
         val db=this.writableDatabase
         var amount : Int = 0
         val cursor = db.rawQuery(query,null)
         if(cursor.moveToFirst()){
             amount=Integer.parseInt(cursor.getString(0))
             cursor.close()
         }
         db.close()
         return amount
     }
}
