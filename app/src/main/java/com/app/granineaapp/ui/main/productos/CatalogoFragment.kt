package com.app.granineaapp.ui.main.productos

import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.granineaapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CatalogoFragment : Fragment() {

    // Lista mutable para poder agregar productos nuevos en tiempo real
    private val todosLosProductos = mutableListOf(
        Producto("El chiki",  5000.0,  R.drawable.atomo_imagen_chiki,    TipoProducto.SIN_LICOR),
        Producto("El neita",  15000.0, R.drawable.atomo_imagen_chocopa,  TipoProducto.CON_LICOR),
        Producto("Chocopa",   30000.0, R.drawable.atomo_imagen_chocopa,  TipoProducto.XL),
        Producto("Litroski",  32000.0, R.drawable.atomo_imagen_litroski, TipoProducto.XL),
    )

    private lateinit var adapter: ProductoAdapter
    private var filtroActivo: TipoProducto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Escuchar el resultado que envía CrearProductoFragment
        parentFragmentManager.setFragmentResultListener("nuevo_producto", this) { _, bundle ->
            val nombre = bundle.getString("nombre") ?: return@setFragmentResultListener
            val precio = bundle.getDouble("precio")
            val imagen = bundle.getInt("imagen")
            val tipoStr = bundle.getString("tipo") ?: TipoProducto.SIN_LICOR.name
            val tipo   = TipoProducto.valueOf(tipoStr)

            val nuevoProducto = Producto(nombre, precio, imagen, tipo)
            todosLosProductos.add(nuevoProducto)

            // Refrescar lista respetando el filtro activo
            aplicarFiltro(null, emptyList())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalogo, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_productos)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        adapter = ProductoAdapter(todosLosProductos.toMutableList()) { producto ->
            val fragment = DetalleProductoFragment()
            fragment.arguments = Bundle().apply {
                putString("nombre", producto.nombre)
                putDouble("precio", producto.precio)
                putInt("imagen", producto.imagenRes)
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = adapter

        // Filtros
        val filtroSin = view.findViewById<TextView>(R.id.filtro_sin_licor)
        val filtroCon = view.findViewById<TextView>(R.id.filtro_con_licor)
        val filtroXl  = view.findViewById<TextView>(R.id.filtro_xl)
        val filtros   = listOf(filtroSin, filtroCon, filtroXl)

        listOf(
            filtroSin to TipoProducto.SIN_LICOR,
            filtroCon to TipoProducto.CON_LICOR,
            filtroXl  to TipoProducto.XL
        ).forEach { (tv, tipo) ->
            tv.setOnClickListener {
                filtroActivo = if (filtroActivo == tipo) null else tipo
                aplicarFiltro(if (filtroActivo == tipo) tv else null, filtros)
            }
        }

        // FAB o botón para ir a CrearProductoFragment
        // Asegúrate de tener un fab_agregar en fragment_catalogo.xml,
        // o cambia esto por el id de tu botón "+" existente.
        view.findViewById<FloatingActionButton>(R.id.fab_agregar)?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CrearProductoFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun aplicarFiltro(seleccionado: TextView?, todos: List<TextView>) {
        todos.forEach { tv ->
            val activo = tv == seleccionado
            tv.setBackgroundResource(
                if (activo) R.drawable.bg_filtro_activo else R.drawable.bg_filtro_inactivo
            )
            tv.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (activo) android.R.color.white else R.color.color_verde_oscuro
                )
            )
        }
        val lista = if (filtroActivo == null) todosLosProductos
        else todosLosProductos.filter { it.tipo == filtroActivo }
        adapter.actualizarLista(lista)
    }
}