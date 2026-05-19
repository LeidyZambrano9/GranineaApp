package com.app.granineaapp.ui.main.productos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.granineaapp.R
import com.app.granineaapp.data.ProductoRepository
import com.app.granineaapp.ui.main.carrito.CarritoFragment
import kotlinx.coroutines.launch

/**
 * Catálogo de productos para el cliente — carga la lista desde Supabase.
 * Los filtros y el buscador trabajan sobre la lista ya descargada (sin
 * consultas adicionales).
 */
class CatalogoFragment : Fragment() {

    // ── Lista maestra descargada de Supabase ──────────────────────────────────
    private var listaProductos: List<Producto> = emptyList()

    // ── Estado de filtros ─────────────────────────────────────────────────────
    private var filtroActivo: String = "todos"
    private var textoBusqueda: String = ""

    // ── Referencias UI ────────────────────────────────────────────────────────
    private lateinit var adapter: ProductoAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var buscador: EditText
    private lateinit var progressBar: ProgressBar

    private lateinit var btnSinLicor: TextView
    private lateinit var btnConLicor: TextView
    private lateinit var btnXL: TextView
    private lateinit var btnCarrito: ImageView

    // ─────────────────────────────────────────────────────────────────────────
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalogo, container, false)

        // Referencias
        recyclerView = view.findViewById(R.id.recycler_productos)
        buscador     = view.findViewById(R.id.buscador)
        btnSinLicor  = view.findViewById(R.id.btn_sin_licor)
        btnConLicor  = view.findViewById(R.id.btn_con_licor)
        btnXL        = view.findViewById(R.id.btn_xl)
        btnCarrito   = view.findViewById(R.id.btn_carrito)
        progressBar  = view.findViewById(R.id.progressBarCatalogo)  // añadir al layout

        btnCarrito.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CarritoFragment())
                .addToBackStack(null)
                .commit()
        }

        // RecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ProductoAdapter(emptyList()) { producto -> abrirDetalle(producto) }
        recyclerView.adapter = adapter

        configurarFiltros()
        configurarBuscador()

        // Cargar desde Supabase
        cargarProductos()

        return view
    }

    // ── Recargar cuando el fragment vuelve a ser visible ──────────────────────
    override fun onResume() {
        super.onResume()
        cargarProductos()
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  CARGA DESDE SUPABASE
    // ─────────────────────────────────────────────────────────────────────────
    private fun cargarProductos() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                listaProductos = ProductoRepository.obtenerProductos()
                aplicarFiltros()
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Error al cargar el catálogo: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  FILTROS
    // ─────────────────────────────────────────────────────────────────────────
    private fun configurarFiltros() {
        val botones = listOf(btnSinLicor, btnConLicor, btnXL)
        val tipos   = listOf("sin licor", "con licor", "xl")

        botones.forEachIndexed { index, boton ->
            boton.setOnClickListener {
                val tipo = tipos[index]
                filtroActivo = if (filtroActivo == tipo) "todos" else tipo
                actualizarEstiloFiltros()
                aplicarFiltros()
            }
        }
    }

    private fun actualizarEstiloFiltros() {
        val botones = listOf(btnSinLicor, btnConLicor, btnXL)
        val tipos   = listOf("sin licor", "con licor", "xl")

        botones.forEachIndexed { index, boton ->
            val activo = filtroActivo == tipos[index]
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

    // ─────────────────────────────────────────────────────────────────────────
    //  BUSCADOR
    // ─────────────────────────────────────────────────────────────────────────
    private fun configurarBuscador() {
        buscador.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textoBusqueda = s?.toString()?.trim() ?: ""
                aplicarFiltros()
            }
        })
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  FILTRO + BÚSQUEDA COMBINADOS (opera sobre la lista en memoria)
    // ─────────────────────────────────────────────────────────────────────────
    private fun aplicarFiltros() {
        val lista = listaProductos.filter { producto ->
            val coincideTipo   = filtroActivo == "todos" || producto.tipo == filtroActivo
            val coincideTexto  = textoBusqueda.isEmpty() ||
                    producto.nombre.contains(textoBusqueda, ignoreCase = true)
            coincideTipo && coincideTexto
        }
        adapter.actualizarLista(lista)
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  DETALLE
    // ─────────────────────────────────────────────────────────────────────────
    private fun abrirDetalle(producto: Producto) {
        val fragment = DetalleProductoFragment()
        fragment.arguments = Bundle().apply {
            putString("nombre",      producto.nombre)
            putDouble("precio",      producto.precio)
            putString("imagen_url",  producto.imagenUrl)
            putString("descripcion", producto.descripcion)
            putString("sabores",     producto.sabores)
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}