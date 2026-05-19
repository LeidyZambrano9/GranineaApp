package com.app.granineaapp.ui.main.perfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.app.granineaapp.R
import com.app.granineaapp.data.UsuarioRepository
import kotlinx.coroutines.launch

class InformacionPersonalFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Asegúrate de que el nombre del archivo XML sea fragment_perfil.xml
        return inflater.inflate(R.layout.fragment_informacion_personal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vinculación con los IDs corregidos de tu XML
        val ivFoto     = view.findViewById<ImageView>(R.id.iv_perfil_foto)
        val etNombres  = view.findViewById<EditText>(R.id.et_ver_nombres)
        val etApellidos = view.findViewById<EditText>(R.id.et_ver_apellidos)
        val etCorreo   = view.findViewById<EditText>(R.id.et_ver_correo)
        val btnEditar  = view.findViewById<Button>(R.id.btn_ir_editar_perfil)

        // Cargar datos del usuario desde Supabase
        lifecycleScope.launch {
            try {
                val usuario = UsuarioRepository.obtenerUsuarioActual()
                android.util.Log.d("DEBUG_PERFIL", "foto_url: ${usuario?.foto_url}")

                if (usuario != null) {
                    // Usamos .setText() porque en el XML son EditText
                    etNombres.setText(usuario.nombres)
                    etApellidos.setText(usuario.apellidos)
                    etCorreo.setText(usuario.correo ?: "")

                    // Cargar foto con Coil si existe URL
                    if (!usuario.foto_url.isNullOrEmpty()) {
                        val urlConTimestamp = "${usuario.foto_url}?t=${System.currentTimeMillis()}"

                        ivFoto?.load(urlConTimestamp) {
                            transformations(CircleCropTransformation())
                            placeholder(R.mipmap.ic_launcher_round)
                            error(R.mipmap.ic_launcher_round)
                            memoryCachePolicy(CachePolicy.DISABLED)
                            diskCachePolicy(CachePolicy.DISABLED)
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Error al cargar perfil", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                android.util.Log.e("ERROR_PERFIL", "Error: ${e.message}")
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }

        // Navegación a la pantalla de edición
        btnEditar.setOnClickListener {
            // Usamos 'fragment_container' que es el contenedor real de tu activity_main.xml
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EditarPerfilFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}