package com.app.granineaapp.ui.main.admin.productos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.granineaapp.R
import com.app.granineaapp.ui.main.productos.ProductoAdapter
import com.app.granineaapp.ui.main.productos.Producto

class CatalogoAdminFragment : Fragment() {

    // ✅ Lista visual de productos (dummy)
    private val listaProductos = listOf(
        Producto("El chiki", 5000.0, R.drawable.atomo_imagen_chiki),
        Producto("El neita", 15000.0, R.drawable.atomo_imagen_el_neita),
        Producto("Chocopa", 30000.0, R.drawable.atomo_imagen_chocopa),
        Producto("Litroski", 32000.0, R.drawable.atomo_imagen_litroski),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalogo_admin, container, false)

        // ✅ Botón CREAR PRODUCTO (navegación visual)
        view.findViewById<View>(R.id.btnCrearProductoAdmin).setOnClickListener {
            val intent = Intent(requireContext(), CrearProductoActivity::class.java)
            startActivity(intent)
        }

        // ✅ Botón EDITAR PRODUCTO (genérico - abre con datos dummy)
        view.findViewById<View>(R.id.btnEditarProductoAdmin).setOnClickListener {
            val intent = Intent(requireContext(), EditarProductoActivity::class.java)
            // ✅ Pasamos datos dummy para que se vea algo al abrir
            intent.putExtra("producto_id", -1)  // -1 = modo genérico
            intent.putExtra("producto_nombre", "Producto Ejemplo")
            intent.putExtra("producto_precio", 10000.0)
            startActivity(intent)
        }

        // ✅ RecyclerView con productos
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_productos_admin)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // ✅ Adapter: click en producto → editar ESE producto
        recyclerView.adapter = ProductoAdapter(listaProductos) { producto ->
            val intent = Intent(requireContext(), EditarProductoActivity::class.java)
            intent.putExtra("producto_id", producto.hashCode())  // ID visual dummy
            intent.putExtra("producto_nombre", producto.nombre)
            intent.putExtra("producto_precio", producto.precio)
            intent.putExtra("producto_imagen", producto.imagenRes)
            startActivity(intent)
        }

        return view
    }
}