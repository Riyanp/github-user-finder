package com.tiket.github.userfinder

import android.content.Context
import android.widget.Toast

fun Context.toast(message: String): Toast = Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
    show()
}