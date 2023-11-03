package edu.put.boardgamecollector151855

import android.content.Context
import java.io.File
import java.io.FileWriter

class FileServicer {

    companion object {
       var filenameUserdata: String? = null
       var filenameDownloadedData: String? = null
       var fileUserdata: File? = null
       var fileDownloadData: File? = null
   }

    fun initFiles(context : Context) {
        val path = context.getExternalFilesDir(null)
        filenameUserdata = "username.xml"
        filenameDownloadedData = "downloadedData.xml"
        val ExternalFilesDirectory = File(path,"ExternalFiles")
        ExternalFilesDirectory.mkdirs()

        createDownloaddataFile(ExternalFilesDirectory)
        createUserdataFile(ExternalFilesDirectory)
    }

    fun createUserdataFile(ExternalFilesDirectory : File){
        fileUserdata = File(ExternalFilesDirectory, filenameUserdata.toString())
    }

    fun createDownloaddataFile(ExternalFilesDirectory : File){
        fileDownloadData = File(ExternalFilesDirectory, filenameDownloadedData.toString())
    }

    fun removeFiles() {
        removeUser()
        removeDownloadedfile()
    }

    fun removeUser(){
        fileUserdata?.delete()
    }

    fun removeDownloadedfile(){
        fileDownloadData?.delete()
    }

    fun WriteToUsernameFile(nickname : String, countGames : Int, countAddons : Int, context: Context)
    {
        try {

            val date = CurrentDate()
            val currentdate = date.GetCurrentDate()
            initFiles(context)

            val writer = FileWriter(fileUserdata,false)
            writer.write("$nickname \n$currentdate \n$countGames \n$countAddons \n")
            writer.close()
            println("Successfully wrote to the file. ")
        } catch (e: Exception) {
            println("An error occurred: ${e.message}")
        }
    }

    fun AddDownloadedLine(content : String, append : Boolean)
    {
        try {
            val writer = FileWriter(fileDownloadData, append)
            writer.write(content)
            writer.close()
            println("Successfully wrote to the file. + $content")
        } catch (e: Exception) {
            println("An error occurred: ${e.message} + $content")
        }
    }

    fun readUserData() : List<Any>{
        val line= fileUserdata?.readLines()
        return listOf(line?.get(0) ?: String , line?.get(1) ?: String , line?.get(2) ?: Int, line?.get(3) ?: Int )
    }

    fun getNickname(): String {
        val lines = readUserData()
        return lines[0].toString()
    }
}