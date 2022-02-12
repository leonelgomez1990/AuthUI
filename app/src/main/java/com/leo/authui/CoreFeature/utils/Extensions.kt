package com.leo.authui.CoreFeature.utils

import android.view.View

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
