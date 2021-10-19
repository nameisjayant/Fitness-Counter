package com.example.fitnesscounter.utils

import android.content.Context
import android.widget.Toast


fun Context.showMsg(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()