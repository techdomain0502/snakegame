package com.srin.snakegame

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DialogFactory{

    companion object{
        fun getLostDialog(ctxt: Context): AlertDialog {
            val dialogBuilder:androidx.appcompat.app.AlertDialog.Builder
            = androidx.appcompat.app.AlertDialog.Builder(ctxt)
            dialogBuilder.setMessage("Sorry you lost")
            dialogBuilder.setPositiveButton("restart"
            ) { dialog, which ->
                (ctxt as AppCompatActivity).finish()
            }
            dialogBuilder.setNegativeButton("finish"
            ) { dialog, which -> (ctxt as AppCompatActivity).finish()}

           return dialogBuilder.create()
        }
    }



}