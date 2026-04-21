package com.app.granineaapp.ui.main.productos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.granineaapp.R

class ProductoAdapter(
    private val productos: List<Producto>,
    private val onClick: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagen: ImageView = itemView.findViewById(R.id.img_producto)
        val nombre: TextView = itemView.findViewById(R.id.tv_nombre)
        val precio: TextView = itemView.findViewById(R.id.tv_precio)
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

        holder.imagen.setImageResource(producto.imagenRes)
        holder.nombre.text = producto.nombre
        holder.precio.text = "$${producto.precio.toInt()}"

        // Configuramos el click en el itemView (el CardView principal)
        holder.itemView.setOnClickListener {
            onClick(producto)
        }

        // Aseguramos que los elementos internos no consuman el click si queremos que todo el card sea cliqueable
        // EXCEPTO el botón de agregar que tiene su propia acción
        holder.btnAgregar.setOnClickListener {
            // Acción para agregar al carrito (se puede implementar después)
        }
    }
}