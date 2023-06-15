package com.example.heladosvenecia

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class NumericButtonGroup
constructor(numericButtons: Array<Button>,
            buttonBorrar: Button,
            val numericDisplay: TextView,
            val updateActualDisplayCallback: () -> Unit,
            private val context: Context) {
    init {
        for (i in 0..9) {
            numericButtons[i].setOnClickListener {
                updateNumericDisplayWithNumber(i)
                updateActualDisplayCallback()
            }
        }

        buttonBorrar.setOnClickListener {
            resetNumericDisplay()
            updateActualDisplayCallback()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateNumericDisplayWithNumber(number: Number) {
        val displayText = numericDisplay.text as String

        if (displayText == "0") {
            numericDisplay.text = number.toString()
        } else if (numericDisplay.text.toString().toInt() * 10 < 10000){
            numericDisplay.text = displayText + number.toString()
        } else {
            Toast.makeText(context, "No puede sobrepasar diez mil unidades", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetNumericDisplay() {
        numericDisplay.text = "0"
    }
}