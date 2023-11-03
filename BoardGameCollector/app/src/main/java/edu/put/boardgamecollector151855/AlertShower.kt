package edu.put.boardgamecollector151855

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class AlertShower : AppCompatActivity(){

    fun CreateSynchrAlert(context: Context) : Boolean {
        val builder = AlertDialog.Builder(context)
        var synchronizationConfirmed = true

        builder.setMessage("Data is up to date.\nAre you sure you want to synchronize?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                synchronizationConfirmed=true
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.dismiss()
                synchronizationConfirmed = false
            }
        val alert = builder.create()
        alert.show()
        return synchronizationConfirmed
    }

    fun CreateCommonAlert(info: String,context: Context,finish: Boolean){
        val builder = AlertDialog.Builder(context)
        builder.setMessage(info)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->
                dialog.dismiss()
                if(finish)
                    finish()
            }
        val alert = builder.create()
        alert.show()
    }
}