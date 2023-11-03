package edu.put.boardgamecollector151855

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding

class StatsActivity : AppCompatActivity()  {

    private lateinit var dbHandler : DbHandler

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_stats)

            this.dbHandler = DbHandler(this,null,null,1)

            var mutablelist = mutableListOf<Game>()
            val text = intent.getStringExtra("text")
            when(text){
                "ADDITIONALS" ->{  mutablelist = dbHandler.findAddons() }
                "GAMES" -> {  mutablelist = dbHandler.findGames()  }
            }
            val title = "LIST OF $text"

            showGames(mutablelist , title)
        }

        private fun showGames(mutablelist : MutableList<Game>, siteTitle : String){
            val tableLayout = findViewById<TableLayout>(R.id.tableLayout)
            val rows = mutablelist.count()
            for(i in -1 until rows)
            {
                val tableRow = TableRow(this)
                when (i)
                {
                    //main title of site
                    -1 -> {
                        val textView = TextView(this)
                        textView.text = siteTitle
                        textView.textSize = 16f
                        textView.setPadding(0)
                        textView.setTextColor(Color.RED)
                        tableRow.setBackgroundColor(Color.GRAY)
                        tableRow.gravity = Gravity.CENTER
                        tableRow.addView(textView)
                    }
                    // values of the game
                    else -> {
                        val game = mutablelist[i]
                        val title = game.getName()
                        val year = game.getPubYear()
                        val miniphoto = game.getPhoto()
                        val text_size = 16F

                        val imageView = ImageView(this)
                        if(miniphoto == "standard"){
                            imageView.setImageResource(R.drawable.standard)
                        }
                        else {
                            ImageLoadTask(imageView).execute(miniphoto)
                        }

                        val textView1 = TextView(this)
                        textView1.text = "$title"
                        textView1.setPadding(0, 5, 5, 5)
                        textView1.layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                        )
                        textView1.gravity = Gravity.START
                        textView1.textSize = text_size
                        textView1.isClickable = true
                        textView1.isFocusable = true
                        textView1.setOnClickListener{
                            onButtonClick(textView1.text.toString())
                        }

                        val textView2 = TextView(this)
                        textView2.text = "$year"
                        textView2.setPadding(0, 5, 5, 5)
                        textView2.layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                        )
                        textView2.gravity = Gravity.START
                        textView2.textSize = text_size

                        tableRow.addView(imageView)
                        tableRow.addView(textView1)
                        tableRow.addView(textView2)
                    }
                }
                tableLayout.addView(tableRow)
            }
        }

    private fun onButtonClick( text : String) {
        val intent = Intent(this,GameActivity::class.java)
        intent.putExtra("NameOfGame", "$text")
        startActivity(intent)
    }
}