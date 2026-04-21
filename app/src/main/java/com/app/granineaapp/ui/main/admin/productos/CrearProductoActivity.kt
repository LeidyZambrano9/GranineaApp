package com.app.granineaapp.ui.main.admin.productos

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.granineaapp.R
import com.app.granineaapp.data.FakeData
import com.app.granineaapp.ui.main.productos.Producto

class CrearProductoActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etTamanio: EditText
    private lateinit var etSabores: EditText  // sabores separados por coma

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_producto)

        etNombre = findViewById(R.id.etCrearNombre)
        etDescripcion = findViewById(R.id.etCrearDescripcion)
        etPrecio = findViewById(R.id.etCrearPrecio)
        etTamanio = findViewById(R.id.etCrearTamanio)
        etSabores = findViewById(R.id.etCrearSabores)

        //findViewById<View>(R.id.btnGuardarProducto).setOnClickListener { guardarProducto() }
    }

    /**private fun guardarProducto() {
        val nombre = etNombre.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()
        val precioStr = etPrecio.text.toString().trim()
        val tamanio = etTamanio.text.toString().trim()
        val saboresStr = etSabores.text.toString().trim()

        if (nombre.isBlank() || precioStr.isBlank() || tamanio.isBlank()) {
            Toast.makeText(this, "Por favor completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val precio = precioStr.toDoubleOrNull()
        if (precio == null || precio <= 0) {
            Toast.makeText(this, "El precio ingresado no es válido", Toast.LENGTH_SHORT).show()
            return
        }

        val sabores = saboresStr.split(",").map { it.trim() }.filter { it.isNotBlank() }
        val nuevoId = (FakeData.productos.maxOfOrNull { it.id } ?: 0) + 1

        val nuevoProducto = Producto(
            id = nuevoId,
            nombre = nombre,
            descripcion = descripcion,
            precio = precio,
            tamanio = tamanio,
            saboresDisponibles = sabores
        )

        FakeData.productos.add(nuevoProducto)
        Toast.makeText(this, "El producto se cargó correctamente", Toast.LENGTH_SHORT).show()
        finish()
    }**/
}