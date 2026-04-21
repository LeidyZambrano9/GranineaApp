package com.app.granineaapp.ui.main.perfil

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.app.granineaapp.R
import com.app.granineaapp.ui.auth.LoginActivity
import com.app.granineaapp.ui.inicio.HomeFragment
import com.app.granineaapp.ui.main.pedidos.HistorialFragment

class EditarPerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_editar_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnInformacionPersonal = view.findViewById<Button>(R.id.informacion_personal)
        btnInformacionPersonal.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, InformacionPersonal())
                .addToBackStack(null)
                .commit()
        }

        val btnCambiarContrasena = view.findViewById<Button>(R.id.cambiar_contrasena)
        btnCambiarContrasena.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment_cambiar_contrasena_1())
                .addToBackStack(null)
                .commit()
        }

        val btnHistorialCompras = view.findViewById<Button>(R.id.historial_compras)
        btnHistorialCompras.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HistorialFragment())
                .addToBackStack(null)
                .commit()
        }

        val btnVolver = view.findViewById<Button>(R.id.btn_volver)
        btnVolver.setOnClickListener {
            // Navega al HomeFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        val btnCerrarSesion = view.findViewById<Button>(R.id.cerrar_sesion)
        btnCerrarSesion.setOnClickListener {
            // Redirige al LoginActivity y limpia el stack de actividades
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditarPerfilFragment()
    }
}