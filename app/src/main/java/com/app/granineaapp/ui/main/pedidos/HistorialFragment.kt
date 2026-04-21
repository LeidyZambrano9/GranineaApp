package com.app.granineaapp.ui.main.pedidos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.app.granineaapp.R

class HistorialFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnMostrarMas = view.findViewById<Button>(R.id.btn_mostrar_mas)
        btnMostrarMas.setOnClickListener {
            // Se usa requireActivity().supportFragmentManager para asegurar la transacción en el contenedor principal
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentDetallePedido())
                .addToBackStack(null)
                .commit()
        }

        // Configurar el botón de cerrar (X) si existe en el layout
        view.findViewById<View>(R.id.tv_cerrar)?.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}