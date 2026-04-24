package com.app.granineaapp.ui.main.admin.pedidos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.granineaapp.R
import com.app.granineaapp.adapter.PedidoAdapter
import com.app.granineaapp.data.FakeData
import com.app.granineaapp.model.Pedido

class ListaPedidosFragment : Fragment() {

    private lateinit var rvPedidos: RecyclerView
    private lateinit var adapter: PedidoAdapter

    // ✅ Lista visual de pedidos (dummy data)
    private val pedidosLista = listOf(
        Pedido(1, "Adriana Gutiérrez", 18000.0, "Pendiente"),
        Pedido(2, "Carlos Méndez", 32000.0, "En camino"),
        Pedido(3, "María López", 15000.0, "Entregado"),
        Pedido(4, "Juan Pérez", 45000.0, "Pendiente"),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_listapedidos, container, false)

        rvPedidos = view.findViewById(R.id.rvTodosPedidos)
        rvPedidos.layoutManager = LinearLayoutManager(requireContext())

        // ✅ Adapter con navegación visual a DetallePedidoActivity
        adapter = PedidoAdapter(
            pedidos = pedidosLista,
            onVerDetalle = { pedido ->
                val intent = Intent(requireContext(), DetallePedidoActivity::class.java)
                intent.putExtra("pedido_id", pedido.id)
                intent.putExtra("pedido_cliente", pedido.cliente)
                intent.putExtra("pedido_total", pedido.total)
                intent.putExtra("pedido_estado", pedido.estado)
                startActivity(intent)
            },
            onMarcarEntregado = null // ✅ Visual: no hacemos nada con este callback por ahora
        )

        rvPedidos.adapter = adapter
        return view
    }
}