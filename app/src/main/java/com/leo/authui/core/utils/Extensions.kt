package com.leo.authui.core.utils

import android.app.AlertDialog
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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

// ------------------------------------------------------------------------------------------------

inline fun <T> MutableList<T>.mapInPlace(mutator: (T)->T) {
    val iterate = this.listIterator()
    while (iterate.hasNext()) {
        val oldValue = iterate.next()
        val newValue = mutator(oldValue)
        if (newValue !== oldValue) {
            iterate.set(newValue)
        }
    }
}

inline fun <T> Array<T>.mapInPlace(mutator: (T)->T) {
    this.forEachIndexed { idx, value ->
        mutator(value).let { newValue ->
            if (newValue !== value) this[idx] = mutator(value)
        }
    }
}

// ------------------------------------------------------------------------------------------------

// Convert a data class to a map
fun <T> T.serializeToMap(): Map<String, Any> {
    return convert()
}

// Convert a map to a data class
inline fun <reified T> Map<String, Any>.toDataClass(): T {
    return convert()
}

// Convert an object of type I to type O
inline fun <I, reified O> I.convert(): O {
    val gson = Gson()
    val json = gson.toJson(this)
    return gson.fromJson(json, object : TypeToken<O>() {}.type)
}

// ------------------------------------------------------------------------------------------------

fun String.isEmailValid(): Boolean{
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isPhoneValid(): Boolean{
    return android.util.Patterns.PHONE.matcher(this).matches()
}

fun String.isUrlValid(): Boolean {
    return android.util.Patterns.WEB_URL.matcher(this).matches()
}

// ------------------------------------------------------------------------------------------------

fun Number.toPx() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics)
