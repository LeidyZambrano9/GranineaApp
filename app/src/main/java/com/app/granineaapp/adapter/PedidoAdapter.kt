package com.app.granineaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.granineaapp.R
import com.app.granineaapp.model.Pedido

class PedidoAdapter(
    private var pedidos: List<Pedido>,
    private val onVerDetalle: (Pedido) -> Unit,
    private val onMarcarEntregado: ((Pedido) -> Unit)? = null  // solo para admin
) : RecyclerView.Adapter<PedidoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvPedidoTitulo)
        val tvDetalle: TextView = view.findViewById(R.id.tvPedidoDetalle)
        val btnDetalle: View = view.findViewById(R.id.btnVerDetallePedido)
        val btnMarcar: View? = view.findViewById(R.id.btnMarcarEntregado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = if (onMarcarEntregado != null)
            R.layout.item_pedido_admin
        else
            R.layout.item_pedido_historial
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.tvTitulo.text = "Pedido #${pedido.id}"
        holder.tvDetalle.text = "Detalle del pedido"
        holder.btnDetalle.setOnClickListener { onVerDetalle(pedido) }
        holder.btnMarcar?.setOnClickListener { onMarcarEntregado?.invoke(pedido) }
    }

    override fun getItemCount() = pedidos.size

    fun actualizarLista(nuevaLista: List<Pedido>) {
        pedidos = nuevaLista
        notifyDataSetChanged()
    }
}