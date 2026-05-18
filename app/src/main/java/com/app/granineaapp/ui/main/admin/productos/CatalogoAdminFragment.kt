package com.app.granineaapp.ui.main.admin.productos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.granineaapp.R
import com.app.granineaapp.ui.main.productos.Producto
import com.app.granineaapp.ui.main.productos.ProductoAdapter
import com.app.granineaapp.ui.main.productos.TipoProducto

class CatalogoAdminFragment : Fragment() {

    private val listaProductos = mutableListOf(
        Producto("El chiki",  5000.0,  R.drawable.atomo_imagen_chiki,    TipoProducto.SIN_LICOR),
        Producto("El neita",  15000.0, R.drawable.atomo_imagen_el_neita, TipoProducto.CON_LICOR),
        Producto("Chocopa",   30000.0, R.drawable.atomo_imagen_chocopa,  TipoProducto.XL),
        Producto("Litroski",  32000.0, R.drawable.atomo_imagen_litroski, TipoProducto.XL),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalogo_admin, container, false)

        // Botón CREAR PRODUCTO
        view.findViewById<View>(R.id.btnCrearProductoAdmin).setOnClickListener {
            val intent = Intent(requireContext(), CrearProductoActivity::class.java)
            startActivity(intent)
        }

        // Botón EDITAR PRODUCTO (genérico)
        view.findViewById<View>(R.id.btnEditarProductoAdmin).setOnClickListener {
            val intent = Intent(requireContext(), EditarProductoActivity::class.java)
            intent.putExtra("producto_id", -1)
            intent.putExtra("producto_nombre", "Producto Ejemplo")
            intent.putExtra("producto_precio", 10000.0)
            startActivity(intent)
        }

        // RecyclerView con productos
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_productos_admin)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // Click en producto → editar ESE producto
        recyclerView.adapter = ProductoAdapter(listaProductos) { producto ->
            val intent = Intent(requireContext(), EditarProductoActivity::class.java)
            intent.putExtra("producto_id",     producto.hashCode())
            intent.putExtra("producto_nombre", producto.nombre)
            intent.putExtra("producto_precio", producto.precio)
            intent.putExtra("producto_imagen", producto.imagenRes)
            intent.putExtra("producto_tipo",   producto.tipo.name) // enum → String
            startActivity(intent)
        }

        return view
    }
}