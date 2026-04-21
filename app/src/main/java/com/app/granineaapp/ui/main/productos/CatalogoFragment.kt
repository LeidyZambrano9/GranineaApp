package com.app.granineaapp.ui.main.productos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.granineaapp.R

class CatalogoFragment : Fragment() {

    private val listaProductos = listOf(
        Producto("El chiki", 5000.0, R.drawable.atomo_imagen_chiki),
        Producto("El neita", 15000.0, R.drawable.atomo_imagen_el_neita),
        Producto("Chocopa", 30000.0, R.drawable.atomo_imagen_chocopa),
        Producto("litroski", 32000.0, R.drawable.atomo_imagen_litroski),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_catalogo, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_productos)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        recyclerView.adapter = ProductoAdapter(listaProductos) { producto ->

            val fragment = DetalleProductoFragment()

            val bundle = Bundle()
            bundle.putString("nombre", producto.nombre)
            bundle.putDouble("precio", producto.precio)
            bundle.putInt("imagen", producto.imagenRes)

            fragment.arguments = bundle

            // Cambiamos parentFragmentManager por supportFragmentManager si fuera necesario, 
            // pero en Fragment se usa parentFragmentManager o childFragmentManager.
            // El problema suele ser el ID del contenedor. En Activity_main es fragment_container.
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}