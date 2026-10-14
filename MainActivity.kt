package com.kioscoerick.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale

data class Product(
    val barcode: String,
    val name: String,
    val price: Double,
    val stock: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KioscoApp(this)
        }
    }
}

@Composable
fun KioscoApp(context: Context) {
    val prefs = remember {
        context.getSharedPreferences("kiosco_erick_data", Context.MODE_PRIVATE)
    }

    var products by remember {
        mutableStateOf(loadProducts(prefs))
    }

    var message by remember {
        mutableStateOf("Listo para vender")
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    var scannedCode by remember {
        mutableStateOf("")
    }

    var productName by remember {
        mutableStateOf("")
    }

    var productPrice by remember {
        mutableStateOf("")
    }

    var productStock by remember {
        mutableStateOf("")
    }

    fun saveProducts(list: List<Product>) {
        products = list

        val array = JSONArray()

        list.forEach { product ->
            array.put(
                JSONObject()
                    .put("barcode", product.barcode)
                    .put("name", product.name)
                    .put("price", product.price)
                    .put("stock", product.stock)
            )
        }

        prefs.edit()
            .putString("products", array.toString())
            .apply()
    }

    val scannerLauncher =
        rememberLauncherForActivityResult(
            contract = ScanContract()
        ) { result ->

            val code = result.contents ?: return@rememberLauncherForActivityResult

            val existingProduct = products.firstOrNull {
                it.barcode == code
            }

            if (existingProduct == null) {
                scannedCode = code
                productName = ""
                productPrice = ""
                productStock = ""
                showDialog = true
            } else {
                message = "Producto encontrado: ${existingProduct.name}"
            }
        }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Kiosco Erick")
                    }
                )
            }
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {

                Button(
                    onClick = {
                        val options = ScanOptions()
                            .setPrompt("Apuntá al código de barras")
                            .setBeepEnabled(true)
                            .setOrientationLocked(true)

                        scannerLauncher.launch(options)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("ESCANEAR PRODUCTO")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(message)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Productos guardados: ${products.size}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn {
                    items(
                        items = products,
                        key = { it.barcode }
                    ) { product ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(product.name)
                                Text("Código: ${product.barcode}")
                                Text("Precio: ${money(product.price)}")
                                Text("Stock: ${product.stock}")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = {
                Text("Registrar producto")
            },
            text = {
                Column {
                    Text("Código: $scannedCode")

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = productName,
                        onValueChange = {
                            productName = it
                        },
                        label = {
                            Text("Nombre")
                        },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = productPrice,
                        onValueChange = {
                            productPrice = it
                        },
                        label = {
                            Text("Precio")
                        },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = productStock,
                        onValueChange = {
                            productStock = it
                        },
                        label = {
                            Text("Stock")
                        },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val price = productPrice
                            .replace(",", ".")
                            .toDoubleOrNull()

                        val stock = productStock.toIntOrNull()

                        if (
                            productName.isNotBlank() &&
                            price != null &&
                            stock != null
                        ) {
                            val newProduct = Product(
                                barcode = scannedCode,
                                name = productName.trim(),
                                price = price,
                                stock = stock
                            )

                            saveProducts(
                                products.filterNot {
                                    it.barcode == scannedCode
                                } + newProduct
                            )

                            message = "Producto guardado correctamente"
                            showDialog = false
                        } else {
                            message = "Completá todos los datos correctamente"
                        }
                    }
                ) {
                    Text("GUARDAR")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("CANCELAR")
                }
            }
        )
    }
}

fun loadProducts(
    prefs: android.content.SharedPreferences
): List<Product> {
    return try {
        val array = JSONArray(
            prefs.getString("products", "[]")
        )

        (0 until array.length()).map { index ->
            val obj = array.getJSONObject(index)

            Product(
                barcode = obj.getString("barcode"),
                name = obj.getString("name"),
                price = obj.getDouble("price"),
                stock = obj.optInt("stock", 0)
            )
        }
    } catch (_: Exception) {
        emptyList()
    }
}

fun money(value: Double): String {
    return "$ " + String.format(
        Locale("es", "AR"),
        "%,.2f",
        value
    )
}
