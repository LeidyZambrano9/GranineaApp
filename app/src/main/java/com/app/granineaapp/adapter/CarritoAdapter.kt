package com.app.granineaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.granineaapp.R
import com.app.granineaapp.model.DetallePedido

class CarritoAdapter(
    private val detalles: MutableList<DetallePedido>,
    private val onCantidadCambiada: (DetallePedido, Int) -> Unit  // nuevo total
) : RecyclerView.Adapter<CarritoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvReferencia: TextView = view.findViewById(R.id.tvCarritoReferencia)
        val tvSabor: TextView = view.findViewById(R.id.tvCarritoSabor)
        val tvPrecioUnitario: TextView = view.findViewById(R.id.tvCarritoPrecioUnitario)
        val tvPrecioTotal: TextView = view.findViewById(R.id.tvCarritoPrecioTotal)
        val tvCantidad: TextView = view.findViewById(R.id.tvCarritoCantidad)
        val btnMenos: View = view.findViewById(R.id.btnCarritoMenos)
        val btnMas: View = view.findViewById(R.id.btnCarritoMas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val detalle = detalles[position]
        holder.tvReferencia.text = "referencia= ${detalle.producto.nombre}"
        holder.tvSabor.text = "sabor= ${detalle.sabor}"
        holder.tvPrecioUnitario.text = "valor Unitario=$${String.format("%,.0f", detalle.precioUnitario)}"
        holder.tvPrecioTotal.text = "valor Total =$${String.format("%,.0f", detalle.precioTotal)}"
        holder.tvCantidad.text = detalle.cantidad.toString()

        holder.btnMenos.setOnClickListener {
            if (detalle.cantidad > 1) {
                val nuevo = detalle.copy(cantidad = detalle.cantidad - 1)
                detalles[position] = nuevo
                notifyItemChanged(position)
                onCantidadCambiada(nuevo, nuevo.cantidad)
            }
        }
        holder.btnMas.setOnClickListener {
            val nuevo = detalle.copy(cantidad = detalle.cantidad + 1)
            detalles[position] = nuevo
            notifyItemChanged(position)
            onCantidadCambiada(nuevo, nuevo.cantidad)
        }
    }

    override fun getItemCount() = detalles.size

    fun getDetalles(): List<DetallePedido> = detalles.toList()

    fun agregarDetalle(detalle: DetallePedido) {
        val existente = detalles.indexOfFirst {
            it.producto.id == detalle.producto.id && it.sabor == detalle.sabor
        }
        if (existente != -1) {
            detalles[existente] = detalles[existente].copy(
                cantidad = detalles[existente].cantidad + detalle.cantidad
            )
            notifyItemChanged(existente)
        } else {
            detalles.add(detalle)
            notifyItemInserted(detalles.size - 1)
        }
    }
}