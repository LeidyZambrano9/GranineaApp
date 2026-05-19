package com.app.granineaapp.ui.main.admin.productos

import android.net.Uri
import android.os.Bundle
import android.util.Log
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

class CrearProductoActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etSabores: EditText
    private lateinit var spinnerTipo: Spinner
    private lateinit var btnGuardar: Button
    private lateinit var btnSeleccionarImagen: Button
    private lateinit var ivPreviewImagen: ImageView
    private lateinit var tvImagenSeleccionada: TextView
    private lateinit var progressBar: ProgressBar

    private var imagenUri: Uri? = null

    private val selectorImagen =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            imagenUri = uri
            ivPreviewImagen.load(uri)
            ivPreviewImagen.visibility = View.VISIBLE
            tvImagenSeleccionada.text = uri.lastPathSegment ?: "imagen seleccionada"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_producto)

        etNombre             = findViewById(R.id.etCrearNombre)
        etDescripcion        = findViewById(R.id.etCrearDescripcion)
        etPrecio             = findViewById(R.id.etCrearPrecio)
        etSabores            = findViewById(R.id.etCrearSabores)
        spinnerTipo          = findViewById(R.id.spinnerTipo)
        btnGuardar           = findViewById(R.id.btnGuardarProducto)
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen)
        ivPreviewImagen      = findViewById(R.id.ivPreviewImagen)
        tvImagenSeleccionada = findViewById(R.id.tvImagenSeleccionada)
        progressBar          = findViewById(R.id.progressBarCrear)

        val tipos = listOf("sin licor", "con licor", "xl")
        val spinnerAdapter = ArrayAdapter(this, R.layout.spinner_item, tipos)
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item)
        spinnerTipo.adapter = spinnerAdapter

        btnSeleccionarImagen.setOnClickListener { selectorImagen.launch("image/*") }
        btnGuardar.setOnClickListener { guardarProducto() }
    }

    private fun guardarProducto() {
        val nombre      = etNombre.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()
        val precioStr   = etPrecio.text.toString().trim()
        val tipo        = spinnerTipo.selectedItem.toString()
        val sabores     = etSabores.text.toString().trim()

        if (nombre.isBlank()) { etNombre.error = "El nombre es obligatorio"; return }
        val precio = precioStr.toDoubleOrNull()
        if (precio == null || precio <= 0) { etPrecio.error = "Precio válido requerido"; return }

        setLoading(true)

        lifecycleScope.launch {
            try {
                Log.d("CrearProducto", "▶ Iniciando guardado...")

                val imagenUrl = imagenUri?.let { uri ->
                    val bytes = contentResolver.openInputStream(uri)?.readBytes()
                        ?: throw IllegalStateException("No se pudo leer la imagen")
                    val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
                    val extension = mimeType.substringAfterLast("/")
                    val nombreArchivo = "producto_${System.currentTimeMillis()}.$extension"
                    Log.d("CrearProducto", "▶ Subiendo imagen: $nombreArchivo")
                    val url = ProductoRepository.subirImagen(nombreArchivo, bytes, mimeType)
                    Log.d("CrearProducto", "✅ URL: $url")
                    url
                } ?: ""

                val nuevoProducto = Producto(
                    nombre      = nombre,
                    descripcion = descripcion,
                    precio      = precio,
                    tipo        = tipo,
                    sabores     = sabores,
                    imagenUrl   = imagenUrl,
                    activo      = true
                )
                ProductoRepository.crearProducto(nuevoProducto)
                Log.d("CrearProducto", "✅ Producto guardado")

                runOnUiThread {
                    Toast.makeText(this@CrearProductoActivity, "✅ Producto creado", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }
            } catch (e: Exception) {
                Log.e("CrearProducto", "❌ ${e::class.simpleName}: ${e.message}", e)
                runOnUiThread {
                    setLoading(false)
                    Toast.makeText(this@CrearProductoActivity, "❌ ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            } catch (t: Throwable) {
                Log.e("CrearProducto", "❌ Throwable: ${t.message}", t)
                runOnUiThread {
                    setLoading(false)
                    Toast.makeText(this@CrearProductoActivity, "❌ Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        progressBar.visibility         = if (loading) View.VISIBLE else View.GONE
        btnGuardar.isEnabled           = !loading
        btnSeleccionarImagen.isEnabled = !loading
    }
}