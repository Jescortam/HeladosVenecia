package com.example.heladosvenecia

import android.annotation.SuppressLint
import android.widget.Button
import android.widget.TextView

class NumericButtonGroup
constructor(numericButtons: Array<Button>,
            resetButton: Button,
            val numericDisplay: TextView,
            val updateActualDisplayCallback: () -> Unit) {
    init {
        for (i in 0..9) {
            numericButtons[i].setOnClickListener {
                updateNumericDisplayWithNumber(i)
                updateActualDisplayCallback()
            }
        }

        resetButton.setOnClickListener {
            resetNumericDisplay()
            updateActualDisplayCallback()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateNumericDisplayWithNumber(number: Number) {
        val displayText = numericDisplay.text as String

        if (displayText == "0") {
            numericDisplay.text = number.toString()
        } else {
            numericDisplay.text = displayText + number.toString()
        }
    }

    private fun resetNumericDisplay() {
        numericDisplay.text = "0"
    }
}