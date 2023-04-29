package com.example.heladosvenecia

class CategoryButtonGroup
constructor(
    private val categoryButtonArray: Array<OptionButton>, private val updateActualDisplayCallback: () -> Unit) {
    var activeCategory: OptionButton

    init {
        activeCategory = categoryButtonArray[0]
        selectButton(categoryButtonArray[0])

        for (i in categoryButtonArray.indices) {
            categoryButtonArray[i].button.setOnClickListener {
                selectButton(categoryButtonArray[i])
                updateActualDisplayCallback
            }
        }
    }

    private fun selectButton(optionButton: OptionButton) {
        activeCategory.button.isSelected = false
        optionButton.button.isSelected = true
        activeCategory = optionButton
    }
}