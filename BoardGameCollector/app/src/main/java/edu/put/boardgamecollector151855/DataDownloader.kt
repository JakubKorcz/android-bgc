package edu.put.boardgamecollector151855

import android.content.Context
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL

class DataDownloader  {

    private var alert : AlertShower
    private lateinit var fileServicer : FileServicer
    var urlString:String?=null

    constructor(urlString : String){
        this.urlString = urlString
        this.alert = AlertShower()
    }

    fun downloadData(action : () -> Unit, context : Context)  {

        this.fileServicer = FileServicer()
        var line : String
        var append = false

        CoroutineScope(Dispatchers.IO).launch{
            try {
                val url = URL(urlString)
                val reader = url.openStream().bufferedReader()
                while(reader.readLine().also{line=it?.toString() ?: ""} !=null){
                    fileServicer.AddDownloadedLine(line, append)
                    append=true
                }
                reader.close()
                withContext(Dispatchers.Main){
                    action()
                }
            }
            catch(e: Exception)
            {
                withContext(Dispatchers.Main) {
                    when (e) {
                        is MalformedURLException -> {
                            print("Malformed URL")
                        }
                        else -> {
                            print("Error  ${e.message}")
                        }
                    }
                    fileServicer.removeDownloadedfile()
                }
            }
        }
    }
}