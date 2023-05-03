package com.example.androidcreateprojecttest.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import com.example.androidcreateprojecttest.R
import com.google.android.material.snackbar.Snackbar

fun View.forceHideKeyboard() {
    val inputManager: InputMethodManager =
        this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

fun View.showSnackBar(text: String) {
    Snackbar.make(this.rootView.findViewById(R.id.container), text, Snackbar.LENGTH_SHORT).show()
}

fun View.displaySnackBar(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_SHORT).show()
}

fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun ProgressBar.progressBar(isLoading : Boolean) {
    this.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
}