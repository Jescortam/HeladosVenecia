package com.example.heladosvenecia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import layout.SaleAdapter

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    private val adapter = SaleAdapter()
    private lateinit var numericButtonGroup: NumericButtonGroup
    lateinit var categoryButtonGroup: CategoryButtonGroup
    lateinit var productButtonGroup: ProductButtonGroup
    private lateinit var actualDisplay: TextView
    lateinit var totalDisplay: TextView
    lateinit var buttonAgregar: Button
    lateinit var buttonTerminar: Button
    lateinit var sales: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actualDisplay = findViewById(R.id.actualDisplay)
        totalDisplay = findViewById(R.id.totalDisplay)

        initNumericButtonGroup(::updateActualDisplayCallback)

        initCategoryButtonGroup(::updateActualDisplayCallback)

        initProductButtonGroup(::updateActualDisplayCallback)

        initAddSale()

        initFinish()

        initRecyclerView()
    }

    private fun initNumericButtonGroup(onButtonClick: () -> Unit) {
        val numericButtonArray = arrayOf(
            findViewById<Button>(R.id.button0),
            findViewById(R.id.button1),
            findViewById(R.id.button2),
            findViewById(R.id.button3),
            findViewById(R.id.button4),
            findViewById(R.id.button5),
            findViewById(R.id.button6),
            findViewById(R.id.button7),
            findViewById(R.id.button8),
            findViewById(R.id.button9),
        )

        numericButtonGroup = NumericButtonGroup(
            numericButtonArray,
            findViewById(R.id.resetButton),
            findViewById(R.id.numericDisplay),
            onButtonClick
        )
    }

    private fun initCategoryButtonGroup(updateActualDisplayCallback: () -> Unit) {
        val categoryButtonArray = arrayOf(
            OptionButton("UNIDADES", findViewById(R.id.buttonUnidades)),
            OptionButton("PAQUETES", findViewById(R.id.buttonPaquetes)),
            OptionButton("DINERO", findViewById(R.id.buttonDinero))
        )

        categoryButtonGroup = CategoryButtonGroup(
            categoryButtonArray,
            updateActualDisplayCallback
        )
    }

    private fun initProductButtonGroup(updateActualDisplayCallback: () -> Unit) {
        val productButtonArray = arrayOf(
            ProductButton("BOLIS", "CHEAP", findViewById(R.id.buttonBolis)),
            ProductButton("PALETA", "CHEAP", findViewById(R.id.buttonPaleta)),
            ProductButton("MIXTO PEQUEÑO", "CHEAP", findViewById(R.id.buttonMixtoPequeño)),
            ProductButton("ROMPOPE", "REGULAR", findViewById(R.id.buttonRompope)),
            ProductButton("NIEVE", "REGULAR", findViewById(R.id.buttonNieve)),
            ProductButton("ESQUIMAL", "REGULAR", findViewById(R.id.buttonEsquimal)),
            ProductButton("SANDWICH", "REGULAR", findViewById(R.id.buttonSandwich)),
            ProductButton("MIXTO MEDIANO", "REGULAR", findViewById(R.id.buttonMixtoMediano)),
            ProductButton("JUMBO", "EXPENSIVE", findViewById(R.id.buttonJumbo)),
            ProductButton("LECHE", "EXPENSIVE", findViewById(R.id.buttonLeche)),
            ProductButton("MIXTO GRANDE", "EXPENSIVE", findViewById(R.id.buttonMixtoGrande)),
        )

        productButtonGroup = ProductButtonGroup(
            productButtonArray,
            updateActualDisplayCallback
        )
    }

    private fun updateActualDisplayCallback() {
        if (categoryButtonGroup.activeCategory != null &&
            productButtonGroup.activeProduct != null) {
            val numericValue: Int = (numericButtonGroup.numericDisplay.text as String).toInt()
            val category = categoryButtonGroup.activeCategory.label
            val product = productButtonGroup.activeProduct

            var actualPrice = 0
            var unitPrice = 0

            if (category == "DINERO") {
                actualPrice = numericValue
            } else {
                unitPrice = when (category) {
                    "UNIDADES" -> when (product.type) {
                        "CHEAP" -> 8
                        "REGULAR" -> 17
                        "EXPENSIVE" -> 22
                        else -> 0
                    }
                    "PAQUETES" -> when (product.type) {
                        "CHEAP" -> 65
                        "REGULAR" -> 65
                        "EXPENSIVE" -> 85
                        else -> 0
                    }
                    else -> 0
                }

                actualPrice = unitPrice * numericValue
            }

            actualDisplay.text = actualPrice.toString()
        }
    }

    private fun initAddSale() {
        buttonAgregar = findViewById(R.id.buttonAgregar)
        sales = arrayOf()

        buttonAgregar.setOnClickListener {
            if (numericButtonGroup.numericDisplay.text != "0") {
                updateTotalDisplay()
                addSaleView()
                numericButtonGroup.numericDisplay.text = "0"
                updateActualDisplayCallback()
            }
        }
    }

    private fun updateTotalDisplay() {
        var total = (totalDisplay.text as String).toInt()
        total += (actualDisplay.text as String).toInt()
        totalDisplay.text = total.toString()
    }

    private fun addSaleView() {
        var sale: String = ""
        val numericValueString = numericButtonGroup.numericDisplay.text as String
        val category = categoryButtonGroup.activeCategory.label
        val product = productButtonGroup.activeProduct.label

        if (category == "DINERO") {
            sale += "\n$"
        }

        sale += numericValueString
        sale += "\n"

        if (category == "PAQUETES") {
            sale += "PAQUETES"
        } else {
            sale += "UNIDADES"
        }

        if (category != "DINERO") {
            sale += "\n"
            sale += product
        }

        sales += sale
        adapter.labels = sales
        adapter.notifyItemInserted(adapter.labels.size - 1)
    }

    private fun initFinish() {
        buttonTerminar = findViewById(R.id.buttonTerminar)

        buttonTerminar.setOnClickListener {
            updateTotalDisplay()
            numericButtonGroup.numericDisplay.text = "0"
            updateActualDisplayCallback()
            Toast.makeText(this, totalDisplay.text as String, Toast.LENGTH_SHORT).show()
            totalDisplay.text = "0"

            val size = sales.size
            sales = arrayOf()
            adapter.labels = sales
            adapter.notifyItemRangeRemoved(0, size)
        }
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.saleList)

        adapter.labels = sales
        adapter.notifyItemRangeInserted(0, adapter.labels.size)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }
}
