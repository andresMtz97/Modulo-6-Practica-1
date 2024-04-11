package martinez.andres.modulo6practica1.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import martinez.andres.modulo6practica1.R

fun Activity.toast(text: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, length).show()
}

fun Activity.sbMessage(
    view: View,
    text: String,
    bgColor: Int = getColor(R.color.violet_700),
    textColor: Int = getColor(R.color.white),
    length: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(view, text, length).setTextColor(textColor).setBackgroundTint(bgColor).show()
}

//fun View.hideKeyboard() {
//    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//    inputManager.hideSoftInputFromWindow(windowToken, 0)
//}