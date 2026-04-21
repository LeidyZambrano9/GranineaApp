package com.app.granineaapp.ui.main.productos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
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

        // 🔹 Recibir datos
        val nombre = arguments?.getString("nombre") ?: ""
        val precio = arguments?.getDouble("precio") ?: 0.0
        val imagen = arguments?.getInt("imagen") ?: 0

        // 🔹 Referencias UI
        val img = view.findViewById<ImageView>(R.id.img_detalle)
        val tvNombre = view.findViewById<TextView>(R.id.tv_nombre_detalle)
        val tvPrecio = view.findViewById<TextView>(R.id.tv_precio_detalle)
        val tvDescripcion = view.findViewById<TextView>(R.id.tv_descripcion)
        val tvCantidad = view.findViewById<TextView>(R.id.tv_cantidad)

        val btnMas = view.findViewById<ImageButton>(R.id.btn_mas)
        val btnMenos = view.findViewById<ImageButton>(R.id.btn_menos)
        val btnAgregar = view.findViewById<Button>(R.id.btn_agregar)
        val btnCerrar = view.findViewById<ImageButton>(R.id.btn_cerrar)

        val radioGroup = view.findViewById<RadioGroup>(R.id.radio_sabores)

        // 🔹 Setear datos
        tvNombre?.text = nombre
        tvPrecio?.text = "valor = $${precio.toInt()}"

        tvDescripcion?.text = when (nombre) {
            "El chiki" -> "(8 onzas)\nOka loka\n1 banderita\n1 gusanito"
            "El neita" -> "(12 onzas)\nLeche condensada\nChispas\nGalleta"
            "Chocopa" -> "(16 onzas)\nChocolate\nBarquillos\nGomitas"
            "litroski" -> "(1 litro)\nMix frutas\nJarabe especial"
            else -> "Descripción no disponible"
        }

        if (imagen != 0) img?.setImageResource(imagen)

        btnMas?.setOnClickListener {
            cantidad++
            tvCantidad?.text = cantidad.toString()
        }

        btnMenos?.setOnClickListener {
            if (cantidad > 1) {
                cantidad--
                tvCantidad?.text = cantidad.toString()
            }
        }

        btnCerrar?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 🔹 Botón agregar al carrito
        btnAgregar?.setOnClickListener {
            val selectedId = radioGroup?.checkedRadioButtonId ?: -1
            if (selectedId == -1) {
                Toast.makeText(requireContext(), "Selecciona un sabor", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val radioButton = view.findViewById<RadioButton>(selectedId)
            val sabor = radioButton?.text.toString()

            // 1. Crear el item para el carrito
            val item = CarritoItem(
                nombre = nombre,
                precio = precio,
                imagenRes = imagen,
                cantidad = cantidad,
                sabor = sabor
            )

            // 2. Agregar al CartManager
            CartManager.agregarProducto(item)

            Toast.makeText(requireContext(), "$nombre agregado al carrito", Toast.LENGTH_SHORT).show()

            // 3. Redireccionar al catálogo (cerrar el fragmento actual)
            parentFragmentManager.popBackStack()
        }
    }
}