package com.app.granineaapp.ui.main.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.granineaapp.R
import com.app.granineaapp.ui.inicio.HomeFragment

class fragment_cambiar_contrasena_2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cambiar_contrasena_2, container, false)

        val btnConfirmar = view.findViewById<Button>(R.id.btn_confirmar)
        btnConfirmar.setOnClickListener {
            Toast.makeText(requireContext(), "Su contraseña ha sido cambiada", Toast.LENGTH_SHORT).show()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = fragment_cambiar_contrasena_2()
    }
}