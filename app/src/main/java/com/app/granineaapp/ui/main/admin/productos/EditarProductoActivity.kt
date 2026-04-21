package com.app.granineaapp.ui.main.admin.productos

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.granineaapp.R
import com.app.granineaapp.data.FakeData

class EditarProductoActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etTamanio: EditText
    private lateinit var etSabores: EditText
    private var productoId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_producto)

        productoId = intent.getIntExtra("producto_id", -1)

        etNombre = findViewById(R.id.etEditarNombre)
        etDescripcion = findViewById(R.id.etEditarDescripcion)
        etPrecio = findViewById(R.id.etEditarPrecio)
        etTamanio = findViewById(R.id.etEditarTamanio)
        etSabores = findViewById(R.id.etEditarSabores)

        cargarDatos()

        findViewById<View>(R.id.btnGuardarEdicion).setOnClickListener { guardarCambios() }
    }

    private fun cargarDatos() {
        val producto = FakeData.productos.find { it.id == productoId } ?: return
        etNombre.setText(producto.nombre)
        etDescripcion.setText(producto.descripcion)
        etPrecio.setText(producto.precio.toInt().toString())
        etTamanio.setText(producto.tamanio)
        etSabores.setText(producto.saboresDisponibles.joinToString(", "))
    }

    private fun guardarCambios() {
        val nombre = etNombre.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()
        val precioStr = etPrecio.text.toString().trim()
        val tamanio = etTamanio.text.toString().trim()
        val saboresStr = etSabores.text.toString().trim()

        if (nombre.isBlank() || precioStr.isBlank() || tamanio.isBlank()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val precio = precioStr.toDoubleOrNull()
        if (precio == null || precio <= 0) {
            Toast.makeText(this, "El precio no es válido", Toast.LENGTH_SHORT).show()
            return
        }

        val index = FakeData.productos.indexOfFirst { it.id == productoId }
        if (index != -1) {
            FakeData.productos[index] = FakeData.productos[index].copy(
                nombre = nombre,
                descripcion = descripcion,
                precio = precio,
                tamanio = tamanio,
                saboresDisponibles = saboresStr.split(",").map { it.trim() }.filter { it.isNotBlank() }
            )
            Toast.makeText(this, "El producto se cargó correctamente", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}