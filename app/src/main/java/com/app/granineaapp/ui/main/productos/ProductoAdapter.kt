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
    productos: List<Producto>,                  // ← acepta cualquier List (mutable o no)
    private val onClick: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    // Copia interna mutable para poder actualizar
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

        holder.imagen.setImageResource(producto.imagenRes)
        holder.nombre.text = producto.nombre
        holder.precio.text = "$${producto.precio.toInt()}"

        holder.itemView.setOnClickListener { onClick(producto) }

        holder.btnAgregar.setOnClickListener {
            // Reutiliza el mismo onClick para ir al detalle
            onClick(producto)
        }
    }

    /**
     * Reemplaza la lista mostrada y notifica al RecyclerView.
     * Llamado desde CatalogoFragment cuando cambia el filtro o el buscador.
     */
    fun actualizarLista(nuevaLista: List<Producto>) {
        productos.clear()
        productos.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}