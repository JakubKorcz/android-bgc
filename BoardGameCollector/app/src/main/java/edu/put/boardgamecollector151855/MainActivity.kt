package edu.put.boardgamecollector151855

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class   MainActivity : AppCompatActivity() {

    private lateinit var Alert: AlertShower
    private lateinit var internetCheck: InternetChecker
    private lateinit var fileServicer: FileServicer
    private lateinit var dbHandler: DbHandler
    private lateinit var downloader : DataDownloader
    private lateinit var parser : Parser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.internetCheck = InternetChecker()
        this.Alert = AlertShower()
        this.fileServicer = FileServicer()
        this.dbHandler = DbHandler(this,null,null,1)

        if(internetCheck.checkForInternet(this)){
            showConfigurationScreen()
        }
        else{
           Alert.CreateCommonAlert("This app requires internet connection.\nTry again.",this@MainActivity,true)
        }
    }

    private fun showMainScreen() {
        setContentView(R.layout.activity_main)
        setVal()
        val nickname = fileServicer.getNickname()
        downloader = DataDownloader("https://boardgamegeek.com/xmlapi2/collection?username=$nickname")
        downloader.downloadData(::afterDownload,this)
        mainServiceBttn()
    }

    private fun showConfigurationScreen(){
        setContentView(R.layout.activity_configure)

        fileServicer.removeFiles()
        dbHandler.deleteAll()

        val configuretext = findViewById<TextView>(R.id.configureEditText)
        val configureBttn = findViewById<Button>(R.id.configureAcceptBtn)

        configureBttn.setOnClickListener{
            val nickname=configuretext.text.toString()
            fileServicer.WriteToUsernameFile(nickname, 0, 0,this)
            showMainScreen()
        }
    }

    private fun setVal() {
        val lines = fileServicer.readUserData()
        val nickname = lines[0]
        val date = lines[1]
        val countGames = lines[2]
        val countAdd = lines[3]

        val nicknameText=findViewById<TextView>(R.id.nicknameTextView)
        nicknameText.text = "Nick : $nickname"

        val currentDateText=findViewById<TextView>(R.id.dateLastSynTextView)
        currentDateText.text = "Last Synchronization : $date"

        val gamesText=findViewById<TextView>(R.id.amoutOfGamesTextView)
        gamesText.text = "Games : $countGames"

        val addonsText=findViewById<TextView>(R.id.amountOfAdditionalTextView)
        addonsText.text = "Addons : $countAdd"
    }

    private fun mainServiceBttn(){
        val clrbttn = findViewById<Button>(R.id.clearBttn)
        val synchrbttn = findViewById<Button>(R.id.synBttn)
        val gamesbttn = findViewById<Button>(R.id.gamesBttn)
        val additionalbttn = findViewById<Button>(R.id.additionalGamesBttn)
        val nickname = fileServicer.getNickname()

        clrbttn.setOnClickListener(){
            showConfigurationScreen()
        }

        synchrbttn.setOnClickListener(){
            val synConfirmed = Alert.CreateSynchrAlert(this@MainActivity)
            if(synConfirmed){
                downloader = DataDownloader("https://boardgamegeek.com/xmlapi2/collection?username=$nickname")
                downloader.downloadData(::afterDownload,this)
            }
        }

        gamesbttn.setOnClickListener(){
            val intent = Intent(this,StatsActivity::class.java)
            intent.putExtra("text", "GAMES")
            startActivity(intent)
        }

        additionalbttn.setOnClickListener(){
            val intent = Intent(this,StatsActivity::class.java)
            intent.putExtra("text", "ADDITIONALS")
            startActivity(intent)
        }
    }

    private fun afterDownload(){
        this.parser = Parser()
        val (list, result) = parser.parseGames(this)
        when (result) {
            0 -> {
                Alert.CreateCommonAlert("Invalid username specified. Please change your nickname.",this@MainActivity,false)
            }
            1 -> {
                list.forEach { element -> dbHandler.addGame(element)}
                fileServicer.WriteToUsernameFile(fileServicer.getNickname(), dbHandler.countGames(), dbHandler.countAddons(),this)
                setVal()
            }
            2 -> {
                Alert.CreateCommonAlert("Your request for this collection has been accepted and will be processed. Please try again later for access. ",this@MainActivity,false)
            }
        }
    }

}













