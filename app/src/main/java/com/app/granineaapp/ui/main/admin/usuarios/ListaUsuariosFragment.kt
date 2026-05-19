package com.app.granineaapp.ui.main.admin.usuarios

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.granineaapp.R
import com.app.granineaapp.adapter.UsuarioAdapter
import com.app.granineaapp.data.UsuarioRepository
import com.app.granineaapp.model.Usuario
import kotlinx.coroutines.launch

class ListaUsuariosFragment : Fragment() {

    private lateinit var rvUsuarios: RecyclerView
    private lateinit var adapter: UsuarioAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_lista_usuarios, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvUsuarios = view.findViewById(R.id.rvUsuarios)
        rvUsuarios.layoutManager = LinearLayoutManager(requireContext())

        view.findViewById<View>(R.id.btnVolverUsuarios).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        cargarUsuarios()
    }

    // ✅ Se recarga cada vez que vuelves de EditarUsuarioActivity
    override fun onResume() {
        super.onResume()
        cargarUsuarios()
    }

    private fun cargarUsuarios() {
        lifecycleScope.launch {
            val usuarios = UsuarioRepository.obtenerTodosLosUsuarios()

            adapter = UsuarioAdapter(
                usuarios = usuarios,
                onVerInfo = { usuario -> abrirInfoUsuario(usuario) },
                onEditar = { usuario -> editarUsuario(usuario) }
            )
            rvUsuarios.adapter = adapter
        }
    }

    private fun abrirInfoUsuario(usuario: Usuario) {
        val intent = Intent(requireContext(), EditarUsuarioActivity::class.java)
        intent.putExtra("usuario_id", usuario.id)
        intent.putExtra("solo_lectura", true)
        startActivity(intent)
    }

    private fun editarUsuario(usuario: Usuario) {
        val intent = Intent(requireContext(), EditarUsuarioActivity::class.java)
        intent.putExtra("usuario_id", usuario.id)
        intent.putExtra("solo_lectura", false)
        startActivity(intent)
    }
}