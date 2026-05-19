package com.app.granineaapp.ui.main.admin.productos

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.app.granineaapp.R
import com.app.granineaapp.data.ProductoRepository
import com.app.granineaapp.ui.main.productos.Producto
import kotlinx.coroutines.launch

class EditarProductoActivity : AppCompatActivity() {

    private lateinit var imgProducto: ImageView
    private lateinit var tvImagenSeleccionada: TextView
    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etSabores: EditText
    private lateinit var spinnerTipo: Spinner
    private lateinit var switchActivo: Switch
    private lateinit var btnGuardar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnCambiarImagen: Button
    private lateinit var progressBar: ProgressBar

    private var productoId: Long = -1L
    private var imagenUrlActual: String = ""
    private var nuevaImagenUri: Uri? = null

    private val selectorImagen = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            nuevaImagenUri = it
            imgProducto.load(it) { crossfade(true) }
            tvImagenSeleccionada.text = it.lastPathSegment ?: "imagen seleccionada"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_producto)

        inicializarVistas()
        configurarSpinner()
        recibirDatos()
        configurarBotones()
    }

    private fun inicializarVistas() {
        imgProducto          = findViewById(R.id.img_producto_editar)
        tvImagenSeleccionada = findViewById(R.id.tvImagenSeleccionada)
        etNombre             = findViewById(R.id.et_nombre)
        etDescripcion        = findViewById(R.id.et_descripcion)
        etPrecio             = findViewById(R.id.et_precio)
        etSabores            = findViewById(R.id.et_sabores)
        spinnerTipo          = findViewById(R.id.spinner_tipo)
        switchActivo         = findViewById(R.id.switch_activo)
        btnGuardar           = findViewById(R.id.btn_guardar)
        btnEliminar          = findViewById(R.id.btn_eliminar)
        btnCambiarImagen     = findViewById(R.id.btn_cambiar_imagen)
        progressBar          = findViewById(R.id.progress_editar)
    }

    private fun configurarSpinner() {
        val opciones = listOf("sin licor", "con licor")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipo.adapter = adapter
    }

    private fun recibirDatos() {
        with(intent) {
            productoId      = getLongExtra("producto_id", -1L)
            imagenUrlActual = getStringExtra("producto_imagen_url") ?: ""

            etNombre.setText(getStringExtra("producto_nombre") ?: "")
            etDescripcion.setText(getStringExtra("producto_descripcion") ?: "")

            val precio = getDoubleExtra("producto_precio", 0.0)
            etPrecio.setText(if (precio == 0.0) "" else precio.toLong().toString())

            etSabores.setText(getStringExtra("producto_sabores") ?: "")
            switchActivo.isChecked = getBooleanExtra("producto_activo", true)

            val tipo = getStringExtra("producto_tipo") ?: "sin licor"
            spinnerTipo.setSelection(listOf("sin licor", "con licor").indexOf(tipo).coerceAtLeast(0))
        }

        if (imagenUrlActual.isNotEmpty()) {
            imgProducto.load(imagenUrlActual) {
                crossfade(true)
                placeholder(R.drawable.logo_graninea)
                error(R.drawable.logo_graninea)
            }
        } else {
            imgProducto.setImageResource(R.drawable.logo_graninea)
        }
    }

    private fun configurarBotones() {
        btnCambiarImagen.setOnClickListener { selectorImagen.launch("image/*") }
        btnGuardar.setOnClickListener { guardarCambios() }
        btnEliminar.setOnClickListener { confirmarEliminar() }
    }

    private fun guardarCambios() {
        val nombre      = etNombre.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()
        val precioStr   = etPrecio.text.toString().trim()
        val sabores     = etSabores.text.toString().trim()
        val tipo        = spinnerTipo.selectedItem?.toString() ?: "sin licor"
        val activo      = switchActivo.isChecked

        if (nombre.isEmpty()) { etNombre.error = "Obligatorio"; etNombre.requestFocus(); return }
        if (precioStr.isEmpty()) { etPrecio.error = "Obligatorio"; etPrecio.requestFocus(); return }
        val precio = precioStr.toDoubleOrNull()
        if (precio == null || precio < 0) { etPrecio.error = "Precio inválido"; etPrecio.requestFocus(); return }

        setLoading(true)

        lifecycleScope.launch {
            try {
                val imagenUrl = if (nuevaImagenUri != null) {
                    ProductoRepository.subirImagen(nuevaImagenUri!!, contentResolver) ?: imagenUrlActual
                } else {
                    imagenUrlActual
                }

                val producto = Producto(
                    id          = productoId,
                    nombre      = nombre,
                    descripcion = descripcion,
                    precio      = precio,
                    tipo        = tipo,
                    imagenUrl   = imagenUrl,
                    sabores     = sabores,
                    activo      = activo
                )

                ProductoRepository.actualizarProducto(producto)

                Toast.makeText(this@EditarProductoActivity,
                    "Producto actualizado", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()

            } catch (e: Exception) {
                Toast.makeText(this@EditarProductoActivity,
                    "Error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                setLoading(false)
            }
        }
    }

    private fun confirmarEliminar() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar producto")
            .setMessage("¿Seguro que deseas eliminar este producto? Esta acción no se puede deshacer.")
            .setPositiveButton("Eliminar") { _, _ -> eliminarProducto() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarProducto() {
        if (productoId == -1L) { Toast.makeText(this, "ID inválido", Toast.LENGTH_SHORT).show(); return }
        setLoading(true)
        lifecycleScope.launch {
            try {
                ProductoRepository.eliminarProducto(productoId)
                Toast.makeText(this@EditarProductoActivity, "Producto eliminado", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@EditarProductoActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                setLoading(false)
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        btnGuardar.isEnabled   = !loading
        btnEliminar.isEnabled  = !loading
    }
}