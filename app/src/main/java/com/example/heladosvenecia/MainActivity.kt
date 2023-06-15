package com.example.heladosvenecia

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heladosvenecia.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import layout.SaleAdapter
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val adapter = SaleAdapter()
    private lateinit var numericButtonGroup: NumericButtonGroup
    lateinit var categoryButtonGroup: CategoryButtonGroup
    lateinit var productButtonGroup: ProductButtonGroup
    private var sales: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initNumericButtonGroup(::updateActualDisplayCallback)
        initCategoryButtonGroup(::updateActualDisplayCallback)
        initProductButtonGroup(::updateActualDisplayCallback)

        binding.buttonAgregar.setOnClickListener { onButtonAgregarClick() }

        initFinish()

        initRecyclerView()
    }

    private fun initNumericButtonGroup(updateActualDisplayCallback: () -> Unit) {
        val numericButtonArray = arrayOf(
            binding.button0,
            binding.button1,
            binding.button2,
            binding.button3,
            binding.button4,
            binding.button5,
            binding.button6,
            binding.button7,
            binding.button8,
            binding.button9,
        )

        numericButtonGroup = NumericButtonGroup(
            numericButtonArray,
            binding.buttonBorrar,
            binding.numericDisplay,
            updateActualDisplayCallback,
            this
        )
    }

    private fun initCategoryButtonGroup(updateActualDisplayCallback: () -> Unit) {
        val categoryButtonArray = arrayOf(
            OptionButton(SaleCategory.PIEZAS, binding.buttonPiezas),
            OptionButton(SaleCategory.COMBOS, binding.buttonCombos),
            OptionButton(SaleCategory.CARRITO, binding.buttonCarrito)
        )

        categoryButtonGroup = CategoryButtonGroup(
            categoryButtonArray,
            updateActualDisplayCallback
        )
    }

    private fun initProductButtonGroup(updateActualDisplayCallback: () -> Unit) {
        val productButtonArray = arrayOf(
            ProductButton(ProductLabel.BOLIS, ProductType.SMALL, binding.buttonBolis),
            ProductButton(ProductLabel.PALETA, ProductType.SMALL, binding.buttonPaleta),
            ProductButton(ProductLabel.MIXTO_CHICO, ProductType.SMALL, binding.buttonMixtoChico),
            ProductButton(ProductLabel.ROMPOPE, ProductType.MEDIUM, binding.buttonRompope),
            ProductButton(ProductLabel.NIEVE, ProductType.MEDIUM, binding.buttonNieve),
            ProductButton(ProductLabel.ESQUIMAL, ProductType.MEDIUM, binding.buttonEsquimal),
            ProductButton(ProductLabel.SANDWICH, ProductType.MEDIUM, binding.buttonSandwich),
            ProductButton(ProductLabel.MIXTO_MEDIANO, ProductType.MEDIUM, binding.buttonMixtoMediano),
            ProductButton(ProductLabel.JUMBO, ProductType.LARGE, binding.buttonJumbo),
            ProductButton(ProductLabel.LECHE, ProductType.LARGE, binding.buttonLeche),
        )

        productButtonGroup = ProductButtonGroup(
            productButtonArray,
            updateActualDisplayCallback
        )
    }

    private fun updateActualDisplayCallback() {
        if (categoryButtonGroup.activeCategory !== null &&
            productButtonGroup.activeProduct !== null) {
            val numericValue: Int = (numericButtonGroup.numericDisplay.text as String).toInt()
            val category = categoryButtonGroup.activeCategory.value
            val product = productButtonGroup.activeProduct

            Log.d(TAG, "Numeric Value: $numericValue")
            Log.d(TAG, "Category: $category")
            Log.d(TAG, "Product: ${product.label}")

            var actualPrice = 0
            var unitPrice = 0

            if (category == SaleCategory.CARRITO) {
                actualPrice = numericValue
            } else {
                unitPrice = when (category) {
                    SaleCategory.PIEZAS -> when (product.type) {
                        ProductType.SMALL -> 8
                        ProductType.MEDIUM -> 17
                        ProductType.LARGE -> 22
                    }
                    SaleCategory.COMBOS -> when (product.type) {
                        ProductType.SMALL -> 65
                        ProductType.MEDIUM -> 65
                        ProductType.LARGE -> 85
                    }
                    else -> 0
                }

                actualPrice = unitPrice * numericValue
            }

            binding.actualDisplay.text = actualPrice.toString()
        }
    }

    private fun onButtonAgregarClick() {
        if (numericButtonGroup.numericDisplay.text !== "0") {
            addSale()
            addSaleView()

            updateTotalDisplay()
            numericButtonGroup.numericDisplay.text = "0"
            updateActualDisplayCallback()
        }
    }

    private fun addSale() {
        lifecycleScope.launch {
            val quantity = (numericButtonGroup.numericDisplay.text as String).toInt()
            val category = categoryButtonGroup.activeCategory.value
            val product = productButtonGroup.activeProduct

            val sale = Sale(quantity, category, product.label, product.type)

            val saleList = loadSalesFromStorage()

            Log.d(TAG, saleList.toString())

            saleList.add(sale)
            val saleListJson = Gson().toJson(saleList)

            saveSalesToStorage(saleListJson)
        }
    }

    private suspend fun loadSalesFromStorage(): ArrayList<Sale> {
        var saleListList: List<ArrayList<Sale>>?
        withContext(Dispatchers.IO) {
            val files = filesDir.listFiles()
            saleListList = (files?.filter { it.isFile && it.startsWith("helados-venecia-sales.json") }?.map { file ->
                val jsonString = file.bufferedReader().use { it.readText() }

                val saleListType = object : TypeToken<ArrayList<Sale>>() {}.type
                Gson().fromJson(jsonString, saleListType)
            })
        }

        if (saleListList != null) {
            if (saleListList!!.isNotEmpty()) {
                return saleListList!![0]
            }
        }

        return arrayListOf()
    }

        private fun saveSalesToStorage(saleListJson: String): Boolean {
            return try {
                openFileOutput("helados-venecia-sales.json", MODE_PRIVATE).use { stream ->
                    stream.write(saleListJson.encodeToByteArray())
                }
                true
            } catch(e: IOException) {
                e.printStackTrace()
                false
            }
        }

    private fun updateTotalDisplay() {
        var total = (binding.totalDisplay.text as String).toInt()
        total += (binding.actualDisplay.text as String).toInt()
        binding.totalDisplay.text = total.toString()
    }

    private fun addSaleView() {
        Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show()
//        var sale = ""
//        val numericValueString = numericButtonGroup.numericDisplay.text as String
//        val category = categoryButtonGroup.activeCategory.value
//        val product = productButtonGroup.activeProduct.label
//
//        if (category === SaleCategory.CARRITO) {
//            sale += "\n$"
//        }
//
//        sale += numericValueString
//        sale += "\n"
//
//        if (category === SaleCategory.COMBOS) {
//            sale += "PAQUETES"
//        } else {
//            sale += "UNIDADES"
//        }
//
//        if (category !== SaleCategory.CARRITO) {
//            sale += "\n"
//            sale += product
//        }
//
//        sales += sale
//        adapter.labels = sales
//        adapter.notifyItemInserted(adapter.labels.size - 1)
    }

    private fun initFinish() {
        binding.buttonTerminar.setOnClickListener {
            updateTotalDisplay()
            numericButtonGroup.numericDisplay.text = "0"
            updateActualDisplayCallback()
            Toast.makeText(this, binding.totalDisplay.text as String, Toast.LENGTH_SHORT).show()
            binding.totalDisplay.text = "0"

            val size = sales.size
            sales = arrayListOf()
            adapter.labels = sales
            adapter.notifyItemRangeRemoved(0, size)
        }
    }

    private fun initRecyclerView() {
        adapter.labels = sales
        adapter.notifyItemRangeInserted(0, adapter.labels.size)

        binding.saleList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.saleList.adapter = adapter
    }
}
