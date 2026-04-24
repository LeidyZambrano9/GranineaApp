package com.app.granineaapp.ui.main.admin.usuarios

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.granineaapp.R
import com.app.granineaapp.adapter.UsuarioAdapter
import com.app.granineaapp.data.FakeData
import com.app.granineaapp.model.Usuario

class ListaUsuariosFragment : Fragment() {

    private lateinit var rvUsuarios: RecyclerView
    private lateinit var adapter: UsuarioAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_lista_usuarios, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rvUsuarios = view.findViewById(R.id.rvUsuarios)

        adapter = UsuarioAdapter(
            usuarios = FakeData.usuarios,
            onVerInfo = { usuario -> abrirInfoUsuario(usuario) },
            onEditar = { usuario -> editarUsuario(usuario) }
        )
        rvUsuarios.layoutManager = LinearLayoutManager(requireContext())
        rvUsuarios.adapter = adapter

        view.findViewById<View>(R.id.btnVolverUsuarios).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
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