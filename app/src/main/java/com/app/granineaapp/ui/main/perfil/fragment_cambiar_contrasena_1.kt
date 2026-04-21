package com.app.granineaapp.ui.main.perfil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.app.granineaapp.R
import com.app.granineaapp.ui.main.perfil.fragment_cambiar_contrasena_2

class fragment_cambiar_contrasena_1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cambiar_contrasena_1, container, false)

        val btnEnviar = view.findViewById<Button>(R.id.btn_enviar)
        btnEnviar.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment_cambiar_contrasena_2())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = fragment_cambiar_contrasena_1()
    }
}