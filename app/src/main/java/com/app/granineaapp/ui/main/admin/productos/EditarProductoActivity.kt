package com.app.granineaapp.ui.main.admin.productos

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.app.granineaapp.R
import com.app.granineaapp.data.ProductoRepository
import com.app.granineaapp.ui.main.productos.Producto
import kotlinx.coroutines.launch

/**
 * Pantalla para editar un producto existente.
 *
 * Recibe por Intent:
 *  - "producto_id"          Long
 *  - "producto_nombre"      String
 *  - "producto_descripcion" String
 *  - "producto_precio"      Double
 *  - "producto_tipo"        String ("sin licor" | "con licor" | "xl")
 *  - "producto_imagen_url"  String  ← URL actual guardada en Supabase
 */
class EditarProductoActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etPrecio: EditText
    private lateinit var spinnerTipo: Spinner
    private lateinit var btnGuardar: Button
    private lateinit var btnSeleccionarImagen: Button
    private lateinit var ivPreviewImagen: ImageView
    private lateinit var tvImagenSeleccionada: TextView
    private lateinit var progressBar: ProgressBar

    private var productoId: Long = -1L
    // URL actual en base de datos (se usa si el usuario NO cambia la imagen)
    private var imagenUrlActual: String = ""
    // Uri nueva elegida del dispositivo (null = no cambió)
    private var nuevaImagenUri: Uri? = null

    // ── Lanzador del selector de imagen ──────────────────────────────────────
    private val selectorImagen =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            nuevaImagenUri = uri
            ivPreviewImagen.load(uri)
            ivPreviewImagen.visibility = View.VISIBLE
            tvImagenSeleccionada.text = "Nueva imagen seleccionada"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_producto)

        etNombre             = findViewById(R.id.etEditarNombre)
        etDescripcion        = findViewById(R.id.etEditarDescripcion)
        etPrecio             = findViewById(R.id.etEditarPrecio)
        spinnerTipo          = findViewById(R.id.spinnerEditarTipo)
        btnGuardar           = findViewById(R.id.btnGuardarEdicion)
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen)
        ivPreviewImagen      = findViewById(R.id.ivPreviewImagen)
        tvImagenSeleccionada = findViewById(R.id.tvImagenSeleccionada)
        progressBar          = findViewById(R.id.progressBarEditar)

        val tipos = listOf("sin licor", "con licor", "xl")
        val adapterSpinner = ArrayAdapter(this, R.layout.spinner_item, tipos)
        adapterSpinner.setDropDownViewResource(R.layout.spinner_item)
        spinnerTipo.adapter = adapterSpinner

        productoId = intent.getLongExtra("producto_id", -1L)
        if (productoId == -1L) {
            Toast.makeText(this, "Error: producto no identificado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        cargarDatosEnFormulario(tipos, adapterSpinner)

        btnSeleccionarImagen.setOnClickListener {
            selectorImagen.launch("image/*")
        }
        btnGuardar.setOnClickListener { guardarCambios() }
    }

    private fun cargarDatosEnFormulario(tipos: List<String>, adapterSpinner: ArrayAdapter<String>) {
        etNombre.setText(intent.getStringExtra("producto_nombre") ?: "")
        etDescripcion.setText(intent.getStringExtra("producto_descripcion") ?: "")

        val precio = intent.getDoubleExtra("producto_precio", 0.0)
        etPrecio.setText(if (precio > 0) precio.toInt().toString() else "")

        imagenUrlActual = intent.getStringExtra("producto_imagen_url") ?: ""
        if (imagenUrlActual.isNotBlank()) {
            ivPreviewImagen.load(imagenUrlActual)
            ivPreviewImagen.visibility = View.VISIBLE
            tvImagenSeleccionada.text = "Imagen actual"
        }

        val tipoActual = intent.getStringExtra("producto_tipo") ?: "sin licor"
        spinnerTipo.setSelection(tipos.indexOf(tipoActual).coerceAtLeast(0))
    }

    private fun guardarCambios() {
        val nombre      = etNombre.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()
        val precioStr   = etPrecio.text.toString().trim()
        val tipo        = spinnerTipo.selectedItem.toString()

        if (nombre.isBlank()) { etNombre.error = "El nombre es obligatorio"; etNombre.requestFocus(); return }
        if (precioStr.isBlank()) { etPrecio.error = "El precio es obligatorio"; etPrecio.requestFocus(); return }
        val precio = precioStr.toDoubleOrNull()
        if (precio == null || precio <= 0) { etPrecio.error = "Precio válido mayor a 0"; etPrecio.requestFocus(); return }

        setLoading(true)

        lifecycleScope.launch {
            try {
                // 1. Si el usuario eligió una imagen nueva, subirla; si no, conservar la URL actual
                val imagenUrl = nuevaImagenUri?.let { uri ->
                    val bytes = contentResolver.openInputStream(uri)?.readBytes()
                        ?: throw IllegalStateException("No se pudo leer la imagen")
                    val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
                    val extension = mimeType.substringAfterLast("/")
                    val nombreArchivo = "producto_${productoId}_${System.currentTimeMillis()}.$extension"
                    ProductoRepository.subirImagen(nombreArchivo, bytes, mimeType)
                } ?: imagenUrlActual

                // 2. Actualizar producto en base de datos
                val productoActualizado = Producto(
                    id          = productoId,
                    nombre      = nombre,
                    descripcion = descripcion,
                    precio      = precio,
                    tipo        = tipo,
                    imagenUrl   = imagenUrl
                )
                ProductoRepository.actualizarProducto(productoId, productoActualizado)

                runOnUiThread {
                    Toast.makeText(this@EditarProductoActivity, "✅ Producto actualizado", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    setLoading(false)
                    Toast.makeText(this@EditarProductoActivity, "❌ Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        btnGuardar.isEnabled   = !loading
        btnSeleccionarImagen.isEnabled = !loading
    }
}