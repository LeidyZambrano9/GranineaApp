package com.app.granineaapp.ui.main.productos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import coil.load
import com.app.granineaapp.R
import com.app.granineaapp.ui.main.carrito.CarritoItem
import com.app.granineaapp.ui.main.carrito.CartManager

class DetalleProductoFragment : Fragment() {

    private var cantidad = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detalle_producto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nombre     = arguments?.getString("nombre") ?: ""
        val precio     = arguments?.getDouble("precio") ?: 0.0
        val imagenUrl  = arguments?.getString("imagen_url") ?: ""
        val descripcion = arguments?.getString("descripcion") ?: ""
        val sabores    = arguments?.getString("sabores") ?: ""

        val img          = view.findViewById<ImageView>(R.id.img_detalle)
        val tvNombre     = view.findViewById<TextView>(R.id.tv_nombre_detalle)
        val tvPrecio     = view.findViewById<TextView>(R.id.tv_precio_detalle)
        val tvDescripcion = view.findViewById<TextView>(R.id.tv_descripcion)
        val tvCantidad   = view.findViewById<TextView>(R.id.tv_cantidad)
        val btnMas       = view.findViewById<ImageButton>(R.id.btn_mas)
        val btnMenos     = view.findViewById<ImageButton>(R.id.btn_menos)
        val btnAgregar   = view.findViewById<Button>(R.id.btn_agregar)
        val btnCerrar    = view.findViewById<ImageButton>(R.id.btn_cerrar)
        val radioGroup   = view.findViewById<RadioGroup>(R.id.radio_sabores)

        // Datos
        tvNombre?.text  = nombre
        tvPrecio?.text  = "valor = $${precio.toInt()}"
        tvDescripcion?.text = descripcion.ifEmpty { "Sin descripción" }

        // Imagen desde URL de Supabase
        if (imagenUrl.isNotEmpty()) {
            img?.load(imagenUrl) {
                crossfade(true)
                placeholder(R.drawable.logo_graninea)
                error(R.drawable.logo_graninea)
            }
        } else {
            img?.setImageResource(R.drawable.logo_graninea)
        }

        // Sabores dinámicos desde Supabase
        radioGroup.removeAllViews()
        if (sabores.isNotEmpty()) {
            sabores.split(",").map { it.trim() }.filter { it.isNotEmpty() }.forEach { sabor ->
                val rb = RadioButton(requireContext())
                rb.text = sabor
                rb.id = View.generateViewId()
                rb.setTextColor(resources.getColor(R.color.white, null))
                rb.buttonTintList = resources.getColorStateList(R.color.colorverde_letra, null)
                radioGroup.addView(rb)
            }
        } else {
            // Si no hay sabores definidos, mostrar uno por defecto
            val rb = RadioButton(requireContext())
            rb.text = "Normal"
            rb.id = View.generateViewId()
            rb.setTextColor(resources.getColor(R.color.white, null))
            rb.buttonTintList = resources.getColorStateList(R.color.colorverde_letra, null)
            radioGroup.addView(rb)
            radioGroup.check(rb.id)
        }

        // Contador
        btnMas?.setOnClickListener {
            cantidad++
            tvCantidad?.text = cantidad.toString()
        }
        btnMenos?.setOnClickListener {
            if (cantidad > 1) { cantidad--; tvCantidad?.text = cantidad.toString() }
        }

        btnCerrar?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnAgregar?.setOnClickListener {
            val selectedId = radioGroup?.checkedRadioButtonId ?: -1
            if (selectedId == -1) {
                Toast.makeText(requireContext(), "Selecciona un sabor", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val sabor = radioGroup.findViewById<RadioButton>(selectedId)?.text.toString()
            val item = CarritoItem(
                nombre    = nombre,
                precio    = precio,
                imagenUrl = imagenUrl,
                imagenRes = 0,
                cantidad  = cantidad,
                sabor     = sabor
            )
            CartManager.agregarProducto(item)
            Toast.makeText(requireContext(), "$nombre agregado al carrito", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }
}