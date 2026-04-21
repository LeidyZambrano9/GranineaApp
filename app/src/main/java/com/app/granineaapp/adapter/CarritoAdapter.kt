package com.app.granineaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.granineaapp.R
import com.app.granineaapp.ui.main.carrito.CarritoItem

class CarritoAdapter(
    private val items: MutableList<CarritoItem>,
    private val onRemoveClick: (CarritoItem) -> Unit,
    private val onUpdateTotal: () -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    class CarritoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProducto: ImageView = view.findViewById(R.id.img_item_carrito)
        val tvNombre: TextView = view.findViewById(R.id.tv_nombre_item_carrito)
        val tvSabor: TextView = view.findViewById(R.id.tv_sabor_item_carrito)
        val tvPrecio: TextView = view.findViewById(R.id.tv_precio_item_carrito)
        val tvCantidad: TextView = view.findViewById(R.id.tv_cantidad_item_carrito)
        val btnEliminar: ImageButton = view.findViewById(R.id.btn_eliminar_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val item = items[position]
        holder.tvNombre.text = item.nombre
        holder.tvSabor.text = "Sabor: ${item.sabor}"
        holder.tvPrecio.text = "$${(item.precio * item.cantidad).toInt()}"
        holder.tvCantidad.text = "Cant: ${item.cantidad}"
        holder.imgProducto.setImageResource(item.imagenRes)

        holder.btnEliminar.setOnClickListener {
            onRemoveClick(item)
        }
    }

    override fun getItemCount(): Int = items.size
}