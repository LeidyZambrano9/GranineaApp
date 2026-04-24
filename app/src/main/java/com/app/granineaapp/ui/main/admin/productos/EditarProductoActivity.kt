package com.app.granineaapp.ui.main.admin.productos

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.granineaapp.R

class EditarProductoActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etTamanio: EditText
    private lateinit var etSabores: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_producto)

        // ✅ Inicializar views
        etNombre = findViewById(R.id.etEditarNombre)
        etDescripcion = findViewById(R.id.etEditarDescripcion)
        etPrecio = findViewById(R.id.etEditarPrecio)
        etTamanio = findViewById(R.id.etEditarTamanio)
        etSabores = findViewById(R.id.etEditarSabores)

        // ✅ Cargar datos recibidos del intent (modo visual)
        cargarDatosVisuales()

        // ✅ Botón Guardar (solo visual)
        findViewById<View>(R.id.btnGuardarEdicion).setOnClickListener {
            guardarCambios()
        }
    }

    // ✅ Muestra los datos en los campos (solo visual, sin lógica real)
    private fun cargarDatosVisuales() {
        etNombre.setText(intent.getStringExtra("producto_nombre") ?: "Producto Sin Nombre")
        etDescripcion.setText(intent.getStringExtra("producto_descripcion") ?: "Sin descripción")

        val precio = intent.getDoubleExtra("producto_precio", 0.0)
        etPrecio.setText(if (precio > 0) "$${precio.toInt()}" else "0")

        etTamanio.setText(intent.getStringExtra("producto_tamanio") ?: "500ml")
        etSabores.setText(intent.getStringExtra("producto_sabores") ?: "Chocolate, Vainilla")
    }

    // ✅ Validación visual + Toast + regresar
    private fun guardarCambios() {
        val nombre = etNombre.text.toString().trim()
        val precioStr = etPrecio.text.toString().trim()

        if (nombre.isBlank() || precioStr.isBlank()) {
            Toast.makeText(this, "⚠️ Completa nombre y precio", Toast.LENGTH_SHORT).show()
            return
        }

        // ✅ Mensaje visual de éxito
        Toast.makeText(this, "✅ Producto actualizado (modo visual)", Toast.LENGTH_SHORT).show()
        finish()  // Regresa al catálogo
    }
}