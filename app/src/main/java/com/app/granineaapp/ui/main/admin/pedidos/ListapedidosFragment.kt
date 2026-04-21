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
import com.app.granineaapp.model.EstadoPedido
import com.app.granineaapp.model.Pedido

class ListaPedidosFragment : Fragment() {

    private lateinit var rvPedidos: RecyclerView
    private lateinit var adapter: PedidoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_listapedidos, container, false)



}