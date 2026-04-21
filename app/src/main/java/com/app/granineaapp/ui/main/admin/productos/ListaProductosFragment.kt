package com.app.granineaapp.ui.main.admin.productos

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.granineaapp.R
import com.app.granineaapp.adapter.ProductoAdapter
import com.app.granineaapp.data.FakeData
import com.app.granineaapp.model.Producto

class ListaProductosFragment : Fragment() {

    private lateinit var rvProductos: RecyclerView
    private lateinit var etBuscar: EditText
    private lateinit var adapter: ProductoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_lista_productos, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rvProductos = view.findViewById(R.id.rvListaProductos)
        etBuscar = view.findViewById(R.id.etBuscarProducto)

        configurarAdapter()
        configurarBusqueda()

        view.findViewById<View>(R.id.fabAgregarProducto).setOnClickListener {
            startActivity(Intent(requireContext(), CrearProductoActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.actualizarLista(FakeData.productos)
    }

    private fun configurarAdapter() {
        adapter = ProductoAdapter(
            productos = FakeData.productos,
            onEditar = { producto -> editarProducto(producto) },
            onEliminar = { producto -> confirmarEliminar(producto) }
        )
        rvProductos.layoutManager = GridLayoutManager(requireContext(), 2)
        rvProductos.adapter = adapter
    }

    private fun configurarBusqueda() {
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                adapter.actualizarLista(
                    if (query.isBlank()) FakeData.productos
                    else FakeData.buscarProducto(query)
                )
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun editarProducto(producto: Producto) {
        val intent = Intent(requireContext(), EditarProductoActivity::class.java)
        intent.putExtra("producto_id", producto.id)
        startActivity(intent)
    }

    private fun confirmarEliminar(producto: Producto) {
        AlertDialog.Builder(requireContext())
            .setMessage("¿Está seguro que desea eliminar el producto \"${producto.nombre}\"?")
            .setPositiveButton("Eliminar") { _, _ ->
                FakeData.productos.removeAll { it.id == producto.id }
                adapter.eliminarProducto(producto)
                Toast.makeText(requireContext(), "El producto se eliminó correctamente", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Volver", null)
            .show()
    }
}