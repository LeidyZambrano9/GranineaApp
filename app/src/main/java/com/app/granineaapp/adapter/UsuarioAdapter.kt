package com.app.granineaapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.granineaapp.R
import com.app.granineaapp.model.Usuario

class UsuarioAdapter(
    private var usuarios: List<Usuario>,
    private val onVerInfo: (Usuario) -> Unit,
    private val onEditar: ((Usuario) -> Unit)? = null
) : RecyclerView.Adapter<UsuarioAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvUsuarioTitulo)
        val tvInfo: TextView = view.findViewById(R.id.tvUsuarioInfo)
        val btnInfo: View = view.findViewById(R.id.btnVerInfoUsuario)
        val btnEditar: View? = view.findViewById(R.id.btnEditarUsuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuario = usuarios[position]
        holder.tvTitulo.text = "Usuario #${usuario.id}"
        holder.tvInfo.text = "Información del usuario"
        holder.btnInfo.setOnClickListener { onVerInfo(usuario) }
        holder.btnEditar?.setOnClickListener { onEditar?.invoke(usuario) }
    }

    override fun getItemCount() = usuarios.size

    fun actualizarLista(nuevaLista: List<Usuario>) {
        usuarios = nuevaLista
        notifyDataSetChanged()
    }
}