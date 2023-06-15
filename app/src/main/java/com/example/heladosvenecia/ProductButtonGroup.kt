package com.example.heladosvenecia

class ProductButtonGroup
constructor(
    private val productButtonArray: Array<ProductButton>,
    private val updateActualDisplayCallback: () -> Unit) {
    var activeProduct: ProductButton

    init {
        activeProduct = productButtonArray[0]
        selectButton(productButtonArray[0])

        for (i in productButtonArray.indices) {
            productButtonArray[i].button.setOnClickListener {
                selectButton(productButtonArray[i])
                updateActualDisplayCallback()
            }
        }
    }

    private fun selectButton(productButton: ProductButton) {
        activeProduct.button.isSelected = false
        productButton.button.isSelected = true
        activeProduct = productButton
    }
}