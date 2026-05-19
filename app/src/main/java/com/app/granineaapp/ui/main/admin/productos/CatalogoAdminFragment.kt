package com.app.granineaapp.ui.main.admin.productos

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.granineaapp.R
import com.app.granineaapp.data.ProductoRepository
import com.app.granineaapp.ui.main.productos.Producto
import kotlinx.coroutines.launch

class CatalogoAdminFragment : Fragment() {

    private lateinit var adapter: ProductoAdminAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private val launcherCrear = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { if (it.resultCode == Activity.RESULT_OK) cargarProductos() }

    private val launcherEditar = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { if (it.resultCode == Activity.RESULT_OK) cargarProductos() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_catalogo_admin, container, false)

        progressBar  = view.findViewById(R.id.progressBarAdmin)
        recyclerView = view.findViewById(R.id.recycler_productos_admin)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        adapter = ProductoAdminAdapter(emptyList()) { producto -> abrirEditar(producto) }
        recyclerView.adapter = adapter

        view.findViewById<View>(R.id.btnCrearProductoAdmin).setOnClickListener {
            launcherCrear.launch(Intent(requireContext(), CrearProductoActivity::class.java))
        }

        cargarProductos()
        return view
    }

    private fun cargarProductos() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                // Admin ve TODOS los productos (activos e inactivos)
                val productos = ProductoRepository.obtenerTodosLosProductos()
                adapter.actualizarLista(productos)
            } catch (e: Exception) {
                Toast.makeText(requireContext(),
                    "Error al cargar productos: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun abrirEditar(producto: Producto) {
        val intent = Intent(requireContext(), EditarProductoActivity::class.java).apply {
            putExtra("producto_id",         producto.id ?: -1L)
            putExtra("producto_nombre",      producto.nombre)
            putExtra("producto_descripcion", producto.descripcion)
            putExtra("producto_precio",      producto.precio)
            putExtra("producto_tipo",        producto.tipo)
            putExtra("producto_imagen_url",  producto.imagenUrl)
            putExtra("producto_sabores",     producto.sabores)
            putExtra("producto_activo",      producto.activo)
        }
        launcherEditar.launch(intent)
    }
}