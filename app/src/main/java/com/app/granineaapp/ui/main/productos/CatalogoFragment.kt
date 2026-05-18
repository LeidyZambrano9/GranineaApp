package com.app.granineaapp.ui.main.productos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.granineaapp.R
import com.app.granineaapp.ui.main.carrito.CarritoFragment

class CatalogoFragment : Fragment() {

    // ─── Lista maestra de productos ───────────────────────────────────────────
    // Agrega el campo "tipo" a cada producto: "sin licor", "con licor" o "xl"
    private val listaProductos = listOf(
        Producto("El chiki", 5000.0, R.drawable.atomo_imagen_chiki, "sin licor"),
        Producto("El neita", 15000.0, R.drawable.atomo_imagen_el_neita, "con licor"),
        Producto("Chocopa", 30000.0, R.drawable.atomo_imagen_chocopa, "sin licor"),
        Producto("litroski", 32000.0, R.drawable.atomo_imagen_litroski, "xl"),
    )

    // ─── Estado de filtro activo ──────────────────────────────────────────────
    private var filtroActivo: String = "todos"   // "todos" | "sin licor" | "con licor" | "xl"
    private var textoBusqueda: String = ""

    // ─── Referencias UI ───────────────────────────────────────────────────────
    private lateinit var adapter: ProductoAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var buscador: EditText

    private lateinit var btnSinLicor: TextView
    private lateinit var btnConLicor: TextView
    private lateinit var btnXL: TextView
    private lateinit var btnCarrito: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalogo, container, false)

        // Referencias
        recyclerView = view.findViewById(R.id.recycler_productos)
        buscador = view.findViewById(R.id.buscador)
        btnSinLicor = view.findViewById(R.id.btn_sin_licor)
        btnConLicor = view.findViewById(R.id.btn_con_licor)
        btnXL = view.findViewById(R.id.btn_xl)
        btnCarrito = view.findViewById(R.id.btn_carrito)

        btnCarrito.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CarritoFragment())
                .addToBackStack(null)
                .commit()
        }



        // RecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ProductoAdapter(listaProductos.toMutableList()) { producto ->
            abrirDetalle(producto)
        }
        recyclerView.adapter = adapter

        // Filtros
        configurarFiltros()

        // Buscador
        configurarBuscador()

        return view
    }



    // ─── Filtros ──────────────────────────────────────────────────────────────
    private fun configurarFiltros() {
        val botones = listOf(btnSinLicor, btnConLicor, btnXL)
        val tipos = listOf("sin licor", "con licor", "xl")

        botones.forEachIndexed { index, boton ->
            boton.setOnClickListener {
                val tipo = tipos[index]
                // Toggle: si ya está activo, desactivar (mostrar todos)
                filtroActivo = if (filtroActivo == tipo) "todos" else tipo
                actualizarEstiloFiltros()
                aplicarFiltros()
            }
        }
    }

    private fun actualizarEstiloFiltros() {
        val botones = listOf(btnSinLicor, btnConLicor, btnXL)
        val tipos = listOf("sin licor", "con licor", "xl")

        botones.forEachIndexed { index, boton ->
            val activo = filtroActivo == tipos[index]
            // Cambia el fondo según el estado (activo / inactivo)
            boton.setBackgroundResource(
                if (activo) R.drawable.bg_filtro_activo else R.drawable.bg_filtro_inactivo
            )
            boton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (activo) R.color.black else R.color.white
                )
            )
        }
    }

    // ─── Buscador ─────────────────────────────────────────────────────────────
    private fun configurarBuscador() {
        buscador.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textoBusqueda = s?.toString()?.trim() ?: ""
                aplicarFiltros()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // ─── Lógica combinada de filtro + búsqueda ────────────────────────────────
    private fun aplicarFiltros() {
        val lista = listaProductos.filter { producto ->
            val coincideTipo = filtroActivo == "todos" || producto.tipo == filtroActivo
            val coincideTexto = textoBusqueda.isEmpty() ||
                    producto.nombre.contains(textoBusqueda, ignoreCase = true)
            coincideTipo && coincideTexto
        }
        adapter.actualizarLista(lista)
    }

    // ─── Navegación al detalle ────────────────────────────────────────────────
    private fun abrirDetalle(producto: Producto) {
        val fragment = DetalleProductoFragment()
        val bundle = Bundle().apply {
            putString("nombre", producto.nombre)
            putDouble("precio", producto.precio)
            putInt("imagen", producto.imagenRes)
        }
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }



}
