package com.davidnardya.upractice.pojo

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.TextView
import com.davidnardya.upractice.R

class LoadingDialog(myActivity: Activity) {

    var activity: Activity = myActivity
    lateinit var alertDialog: AlertDialog
    lateinit var textView: TextView

    fun startLoadingDialog() {
        textView = activity.findViewById(R.id.dialog_progress_bar)
        val string = "Loading..."
        textView.text = string

        val builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_dialog_layout, null))
        builder.setCancelable(false)

        alertDialog = builder.create()
        alertDialog.show()
    }

    fun dismissDialog() {
        alertDialog.dismiss()
    }
}