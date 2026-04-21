package com.app.granineaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.granineaapp.R
import com.app.granineaapp.model.Producto

class ProductoAdapter(
    private var productos: MutableList<Producto>,
    private val onEditar: ((Producto) -> Unit)? = null,
    private val onEliminar: ((Producto) -> Unit)? = null,
    private val onAgregar: ((Producto) -> Unit)? = null   // para catálogo cliente
) : RecyclerView.Adapter<ProductoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivImagen: ImageView = view.findViewById(R.id.ivProductoImagen)
        val tvNombre: TextView = view.findViewById(R.id.tvProductoNombre)
        val tvPrecio: TextView = view.findViewById(R.id.tvProductoPrecio)
        val tvTamanio: TextView = view.findViewById(R.id.tvProductoTamanio)
        val btnEditar: View? = view.findViewById(R.id.btnEditarProducto)
        val btnEliminar: View? = view.findViewById(R.id.btnEliminarProducto)
        val btnAgregar: View? = view.findViewById(R.id.btnAgregarCarrito)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = if (onEditar != null)
            R.layout.item_producto_admin
        else
            R.layout.item_producto_catalogo
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = productos[position]
        holder.tvNombre.text = producto.nombre
        holder.tvPrecio.text = "$ ${String.format("%,.0f", producto.precio)}"
        holder.tvTamanio.text = producto.tamanio

        holder.btnEditar?.setOnClickListener { onEditar?.invoke(producto) }
        holder.btnEliminar?.setOnClickListener { onEliminar?.invoke(producto) }
        holder.btnAgregar?.setOnClickListener { onAgregar?.invoke(producto) }
    }

    override fun getItemCount() = productos.size

    fun actualizarLista(nuevaLista: List<Producto>) {
        productos.clear()
        productos.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    fun eliminarProducto(producto: Producto) {
        val index = productos.indexOfFirst { it.id == producto.id }
        if (index != -1) {
            productos.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}