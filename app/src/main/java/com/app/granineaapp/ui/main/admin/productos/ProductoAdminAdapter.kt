package com.app.granineaapp.ui.main.admin.productos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.app.granineaapp.R
import com.app.granineaapp.ui.main.productos.Producto
import com.google.android.material.button.MaterialButton

class ProductoAdminAdapter(
    productos: List<Producto>,
    private val onEditar: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdminAdapter.AdminViewHolder>() {

    private val productos: MutableList<Producto> = productos.toMutableList()

    inner class AdminViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagen: ImageView      = itemView.findViewById(R.id.img_producto)
        val nombre: TextView       = itemView.findViewById(R.id.tv_nombre)
        val precio: TextView       = itemView.findViewById(R.id.tv_precio)
        val btnEditar: MaterialButton = itemView.findViewById(R.id.btn_agregar) // reutiliza el id existente
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto_admin, parent, false)
        return AdminViewHolder(view)
    }

    override fun getItemCount(): Int = productos.size

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        val producto = productos[position]

        // Imagen: recurso local > URL remota > placeholder
        when {
            producto.imagenRes != 0 -> holder.imagen.setImageResource(producto.imagenRes)
            producto.imagenUrl.isNotEmpty() -> holder.imagen.load(producto.imagenUrl) {
                crossfade(true)
                placeholder(R.drawable.logo_graninea)
                error(R.drawable.logo_graninea)
            }
            else -> holder.imagen.setImageResource(R.drawable.logo_graninea)
        }

        holder.nombre.text = producto.nombre
        holder.precio.text = "$${String.format("%,.0f", producto.precio)}"

        // Indicador visual de estado activo/inactivo
        holder.itemView.alpha = if (producto.activo) 1f else 0.45f

        holder.btnEditar.text = "Editar"
        holder.btnEditar.setOnClickListener { onEditar(producto) }
        holder.itemView.setOnClickListener  { onEditar(producto) }
    }

    fun actualizarLista(nuevaLista: List<Producto>) {
        productos.clear()
        productos.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}