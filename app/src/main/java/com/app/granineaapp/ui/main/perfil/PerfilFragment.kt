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

class PerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //  CORREGIDO: Ahora inflama el XML correcto del menú (el de Goku y los botones)
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón: Información Personal
        val btnInformacionPersonal = view.findViewById<Button>(R.id.informacion_personal)
        btnInformacionPersonal?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, InformacionPersonalFragment())
                .addToBackStack(null)
                .commit()
        }

        // Botón: Cambiar Contraseña
        val btnCambiarContrasena = view.findViewById<Button>(R.id.cambiar_contrasena)
        btnCambiarContrasena?.setOnClickListener {
            // Nota: Descoméntalo si la clase 'fragment_cambiar_contrasena_1' ya existe en tu proyecto
            /*
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment_cambiar_contrasena_1())
                .addToBackStack(null)
                .commit()
            */
        }

        // Botón: Historial de Compras
        val btnHistorialCompras = view.findViewById<Button>(R.id.historial_compras)
        btnHistorialCompras?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HistorialFragment())
                .addToBackStack(null)
                .commit()
        }

        // Botón: Volver
        val btnVolver = view.findViewById<Button>(R.id.btn_volver)
        btnVolver?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        // Botón: Cerrar Sesión
        val btnCerrarSesion = view.findViewById<Button>(R.id.cerrar_sesion)
        btnCerrarSesion?.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    companion object {
        // ✅ CORREGIDO: Retorna correctamente una instancia de PerfilFragment
        @JvmStatic
        fun newInstance() = PerfilFragment()
    }
}