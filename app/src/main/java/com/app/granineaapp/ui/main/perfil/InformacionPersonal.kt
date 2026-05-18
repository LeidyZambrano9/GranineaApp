package com.app.granineaapp.ui.main.perfil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.app.granineaapp.R

class InformacionPersonal : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_informacion_personal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCerrar = view.findViewById<ImageView>(R.id.btn_cerrar)
        val btnEditar = view.findViewById<Button>(R.id.btn_editarIP)
        val etNombre = view.findViewById<EditText>(R.id.et_nombreIP)
        val etEmail = view.findViewById<EditText>(R.id.et_emailIP)
        val etTelefono = view.findViewById<EditText>(R.id.et_telefonoIP)

        btnCerrar.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnEditar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val email = etEmail.text.toString()
            val telefono = etTelefono.text.toString()

            if (nombre.isNotEmpty() && email.isNotEmpty() && telefono.isNotEmpty()) {
                // Aquí iría la lógica para guardar los datos
                Toast.makeText(requireContext(), "Información actualizada", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = InformacionPersonal()
    }
}