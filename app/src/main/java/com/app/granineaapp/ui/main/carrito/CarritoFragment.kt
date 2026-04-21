package com.app.granineaapp.ui.main.carrito

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.granineaapp.R
import com.app.granineaapp.adapter.CarritoAdapter

class CarritoFragment : Fragment() {

    private lateinit var adapter: CarritoAdapter
    private lateinit var tvTotal: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_carrito, container, false)

        val recycler = view.findViewById<RecyclerView>(R.id.recycler_carrito)
        tvTotal = view.findViewById(R.id.tv_total_carrito)
        val btnPagar = view.findViewById<View>(R.id.btn_pagar)

        val items = CartManager.obtenerItems().toMutableList()

        adapter = CarritoAdapter(items, { item ->
            CartManager.eliminarItem(item)
            items.remove(item)
            adapter.notifyDataSetChanged()
            actualizarTotal()
        }, {
            actualizarTotal()
        })

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        actualizarTotal()

        btnPagar.setOnClickListener {
            if (items.isEmpty()) {
                Toast.makeText(requireContext(), "El carrito está vacío", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Procesando pedido...", Toast.LENGTH_SHORT).show()
                // Aquí podrías limpiar el carrito después de la compra
                // CartManager.limpiarCarrito()
                // parentFragmentManager.popBackStack()
            }
        }

        return view
    }

    private fun actualizarTotal() {
        val total = CartManager.obtenerTotal()
        tvTotal.text = "$ ${total.toInt()}"
    }
}