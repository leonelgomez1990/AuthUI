package com.leo.authui.core.utils

import android.app.AlertDialog
import android.view.View
import com.google.android.material.snackbar.Snackbar

val <T> T.exhaustive: T
    get() = this

fun View.setVisible(){
    this.visibility = View.VISIBLE
}

fun View.setInvisible(){
    this.visibility = View.INVISIBLE
}

fun View.setEnabled(){
    this.isEnabled = true
}

fun View.disable(){
    this.isEnabled = false
}

fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, message, duration).show()
}

fun View.showConfirmDialog(title: String, message: String, onYesAction: () -> Unit) {
    AlertDialog.Builder(this.context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("Si") { _, _ -> onYesAction() }
        .setNegativeButton("No") { _, _ -> }
        .show()
}