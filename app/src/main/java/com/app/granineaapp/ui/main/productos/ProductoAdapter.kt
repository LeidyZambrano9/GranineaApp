package com.app.granineaapp.ui.main.productos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.granineaapp.R

class ProductoAdapter(
    productos: List<Producto>,
    private val onClick: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    private val productos: MutableList<Producto> = productos.toMutableList()

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagen: ImageView = itemView.findViewById(R.id.img_producto)
        val nombre: TextView  = itemView.findViewById(R.id.tv_nombre)
        val precio: TextView  = itemView.findViewById(R.id.tv_precio)
        val btnAgregar: Button = itemView.findViewById(R.id.btn_agregar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_productos, parent, false)
        return ProductoViewHolder(view)
    }

    override fun getItemCount(): Int = productos.size

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]

        // Lógica de Imagen: Prioriza recurso local, luego URL
        if (producto.imagenRes != 0) {
            holder.imagen.setImageResource(producto.imagenRes)
        } else if (!producto.imagenUrl.isNullOrEmpty()) {
            holder.imagen.load(producto.imagenUrl) {
                crossfade(true)
                placeholder(R.drawable.logo_graninea)
                error(R.drawable.logo_graninea)
            }
        } else {
            holder.imagen.setImageResource(R.drawable.logo_graninea) 
        }

        holder.nombre.text = producto.nombre
        holder.precio.text = "$${String.format("%,.0f", producto.precio)}"

        holder.itemView.setOnClickListener { onClick(producto) }
        holder.btnAgregar.setOnClickListener { onClick(producto) }
    }

    fun actualizarLista(nuevaLista: List<Producto>) {
        productos.clear()
        productos.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}