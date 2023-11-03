package edu.put.boardgamecollector151855

import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class GameActivity :  AppCompatActivity() {

    private lateinit var downloader : DataDownloader
    private lateinit var parser : Parser
    private lateinit var dbHandler: DbHandler
    var game : Game? = null
    private var helpNumber : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val text = intent.getStringExtra("NameOfGame")
        this.title = text.toString()

        this.game = getGamefromDB(text.toString())
        val id = game?.getID()

        downloader = DataDownloader("https://www.boardgamegeek.com/xmlapi2/thing?id=$id&stats=1")
        downloader.downloadData(::afterDownload,this)
        serviceBttn()
    }

    private fun afterDownload()  {
        this.parser = Parser()
        var des = "Error in downloading the description"
        var img = "standard"
        val (list, result) = parser.parseGame(this)
        if(result == 1)
        {
                des = list[0]
                img = list[1]
        }
        setVal(des, img)
        serviceBttn()
    }

    private fun serviceBttn() {
        val button = findViewById<Button>(R.id.gameActivityButton)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val mGetContent = registerForActivityResult(ActivityResultContracts.GetContent()) { result: Uri? ->
            if (result != null) {
                imageView.setImageURI(result)
            }
        }
        button.setOnClickListener(){
            when(helpNumber){
                //add photo
                0 -> {
                    mGetContent.launch("image/*")
                    helpNumber = 1
                    button.text = "Delete photo"
                }
                //delete photo
                1 -> {
                    imageView.setImageResource(R.drawable.standard)
                    helpNumber = 0
                    button.text = "Add photo"
                }
            }
        }
    }

    private fun setVal(des : String, img : String) {
        val year_pub = game?.getPubYear()

        val text = findViewById<TextView>(R.id.title)
        text.text = title
        text.gravity =  Gravity.CENTER
        text.textSize = 30f

        val description = findViewById<TextView>(R.id.description)
        description.text = des

        val year = findViewById<TextView>(R.id.year)
        year.text = "The game was published in $year_pub"

        val imageView = findViewById<ImageView>(R.id.imageView)
        val button = findViewById<Button>(R.id.gameActivityButton)
        if(img=="standard"){
            imageView.setImageResource(R.drawable.standard)
            button.text = "Add photo"
            this.helpNumber = 0
        }else{
            ImageLoadTask(imageView).execute(img)
            button.text = "Delete photo"
            this.helpNumber = 1
        }
    }

    private fun getGamefromDB(text: String): Game? {
        dbHandler = DbHandler(this, null, null, 1)
        return dbHandler.findResultByName(text)
    }
}