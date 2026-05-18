package com.app.granineaapp.ui.main.productos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import com.app.granineaapp.R

class CrearProductoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.activity_crear_producto, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etNombre      = view.findViewById<EditText>(R.id.etCrearNombre)
        val etDescripcion = view.findViewById<EditText>(R.id.etCrearDescripcion)
        val etPrecio      = view.findViewById<EditText>(R.id.etCrearPrecio)
        val etTamanio     = view.findViewById<EditText>(R.id.etCrearTamanio)
        val etSabores     = view.findViewById<EditText>(R.id.etCrearSabores)
        val btnGuardar    = view.findViewById<Button>(R.id.btnGuardarProducto)

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val precio = etPrecio.text.toString().toDoubleOrNull()

            // Validación mínima
            if (nombre.isEmpty()) {
                etNombre.error = "El nombre es obligatorio"
                return@setOnClickListener
            }
            if (precio == null) {
                etPrecio.error = "Ingresa un precio válido"
                return@setOnClickListener
            }

            // Construir el bundle con los datos del nuevo producto
            val resultado = bundleOf(
                "nombre"      to nombre,
                "descripcion" to etDescripcion.text.toString().trim(),
                "precio"      to precio,
                "tamanio"     to etTamanio.text.toString().trim(),
                "sabores"     to etSabores.text.toString().trim(),
                // Sin imagen propia: usa un drawable genérico por defecto
                "imagen"      to R.drawable.atomo_imagen_chiki,
                "tipo"        to TipoProducto.SIN_LICOR.name   // tipo por defecto
            )

            // Enviar resultado al fragmento padre (CatalogoFragment lo escucha)
            parentFragmentManager.setFragmentResult("nuevo_producto", resultado)

            Toast.makeText(requireContext(), "Producto creado", Toast.LENGTH_SHORT).show()

            // Volver atrás
            parentFragmentManager.popBackStack()
        }
    }
}