package com.app.granineaapp.ui.main.perfil

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.app.granineaapp.R
import com.app.granineaapp.SupabaseClient
import com.app.granineaapp.data.UsuarioRepository
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch
import java.io.File
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class EditarPerfilFragment : Fragment() {

    private var uriFotoSeleccionada: Uri? = null
    private lateinit var ivEditarFoto: ImageView
    private lateinit var archivoFotoTemp: File

    // Lanzador para solicitar el permiso de cámara
    private val lanzadorPermisoCamara =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { concedido ->
            if (concedido) {
                abrirCamara()
            } else {
                Toast.makeText(requireContext(),
                    "Se necesita permiso de cámara para tomar fotos",
                    Toast.LENGTH_SHORT).show()
            }
        }

    // Lanzador para la cámara
    private val lanzadorCamara =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { exito ->
            if (exito) {
                uriFotoSeleccionada = Uri.fromFile(archivoFotoTemp)
                ivEditarFoto.load(uriFotoSeleccionada) {
                    transformations(CircleCropTransformation())
                }
            }
        }

    // Lanzador para la galería
    private val lanzadorGaleria =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                uriFotoSeleccionada = uri
                ivEditarFoto.load(uri) {
                    transformations(CircleCropTransformation())
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.fragment_editar_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivEditarFoto       = view.findViewById(R.id.iv_editar_foto)
        val ivCamaraIcon   = view.findViewById<ImageView>(R.id.iv_camara_icon)
        val etNombres      = view.findViewById<EditText>(R.id.et_editar_nombres)
        val etApellidos    = view.findViewById<EditText>(R.id.et_editar_apellidos)
        val etCorreo       = view.findViewById<EditText>(R.id.et_editar_correo)
        val etCelular      = view.findViewById<EditText>(R.id.et_editar_celular)
        val etContrasena   = view.findViewById<EditText>(R.id.et_editar_contrasena)
        val etReContrasena = view.findViewById<EditText>(R.id.et_editar_recontrasena)
        val btnGuardar     = view.findViewById<Button>(R.id.btn_guardar_perfil)

        // Cargar datos actuales en los campos
        lifecycleScope.launch {
            val usuario = UsuarioRepository.obtenerUsuarioActual()
            if (usuario != null) {
                etNombres.setText(usuario.nombres)
                etApellidos.setText(usuario.apellidos)
                etCorreo.setText(usuario.correo ?: "")
                etCelular.setText(usuario.celular ?: "") // ✅ CONFIGURADO: Ahora sí pinta el celular guardado al entrar

                if (!usuario.foto_url.isNullOrEmpty()) {
                    ivEditarFoto.load(usuario.foto_url) {
                        transformations(CircleCropTransformation())
                        placeholder(R.mipmap.ic_launcher_round)
                        error(R.mipmap.ic_launcher_round)
                    }
                }
            }
        }

        // Click en el ícono de cámara
        ivCamaraIcon.setOnClickListener {
            mostrarOpcionesFoto()
        }

        // Guardar cambios con las nuevas validaciones
        btnGuardar.setOnClickListener {
            guardarCambios(
                etNombres, etApellidos, etCorreo, etCelular,
                etContrasena, etReContrasena
            )
        }
    }

    private fun mostrarOpcionesFoto() {
        val opciones = arrayOf("Tomar foto", "Elegir de galería")
        android.app.AlertDialog.Builder(requireContext())
            .setTitle("Foto de perfil")
            .setItems(opciones) { _, cual ->
                when (cual) {
                    0 -> verificarPermisoCamara()
                    1 -> lanzadorGaleria.launch("image/*")
                }
            }
            .show()
    }

    private fun verificarPermisoCamara() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                abrirCamara()
            }
            shouldShowRequestPermissionRationale(
                Manifest.permission.CAMERA
            ) -> {
                android.app.AlertDialog.Builder(requireContext())
                    .setTitle("Permiso de cámara")
                    .setMessage("Necesitamos acceso a la cámara para que puedas tomar tu foto de perfil.")
                    .setPositiveButton("Entendido") { _, _ ->
                        lanzadorPermisoCamara.launch(Manifest.permission.CAMERA)
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
            else -> {
                lanzadorPermisoCamara.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun abrirCamara() {
        val carpeta = File(requireContext().cacheDir, "images")
        carpeta.mkdirs()
        archivoFotoTemp = File(carpeta, "foto_perfil_temp.jpg")

        val uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            archivoFotoTemp
        )
        lanzadorCamara.launch(uri)
    }

    private fun guardarCambios(
        etNombres: EditText,
        etApellidos: EditText,
        etCorreo: EditText,
        etCelular: EditText,
        etContrasena: EditText,
        etReContrasena: EditText
    ) {
        val nombres      = etNombres.text.toString().trim()
        val apellidos    = etApellidos.text.toString().trim()
        val correo       = etCorreo.text.toString().trim()
        val celular      = etCelular.text.toString().trim()
        val contrasena   = etContrasena.text.toString().trim()
        val recontrasena = etReContrasena.text.toString().trim() // ✅ CORREGIDO: Todo en minúscula aquí

        // 🛑 VALIDACIÓN DE CAMPOS VACÍOS (Igual al registro)
        if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || celular.isEmpty() || contrasena.isEmpty() || recontrasena.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, llenar todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // 🛑 VALIDACIÓN DEL FORMATO DE CORREO: Debe contener un @
        if (!correo.contains("@")) {
            Toast.makeText(requireContext(), "Por favor ingrese un correo válido (Debe contener '@')", Toast.LENGTH_SHORT).show()
            return
        }

        // 🛑 VALIDACIÓN ESTRICTA DE CELULAR: Exactamente 10 dígitos
        if (celular.length != 10) {
            Toast.makeText(requireContext(), "El número de celular debe tener exactamente 10 dígitos", Toast.LENGTH_SHORT).show()
            return
        }

        // 🛑 VALIDACIÓN DE CONTRASEÑA: Mínimo 8 caracteres (Igual al registro)
        if (contrasena.length < 8) {
            Toast.makeText(requireContext(), "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
            return
        }

        // ✅ CORREGIDO: También usamos 'recontrasena' en minúscula aquí
        if (contrasena != recontrasena) {
            Toast.makeText(requireContext(), "Las contraseñas no coinciden, verifique nuevamente.", Toast.LENGTH_SHORT).show()
            return
        }

        // ... resto del código del lifecycleScope para guardar en Supabase

        if (contrasena != recontrasena) {
            Toast.makeText(requireContext(), "Las contraseñas no coinciden, verifique nuevamente.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                // Subir foto si se seleccionó una nueva
                var fotoUrl: String? = null
                if (uriFotoSeleccionada != null) {
                    fotoUrl = UsuarioRepository.subirFotoPerfil(
                        requireContext(),
                        uriFotoSeleccionada!!
                    )
                    android.util.Log.d("DEBUG_FOTO", "fotoUrl retornada: $fotoUrl")
                }

                // ✅ ACTUALIZADO: Ahora sí le enviamos el celular al repositorio modificado
                UsuarioRepository.actualizarPerfil(
                    nombres   = nombres,
                    apellidos = apellidos,
                    correo    = correo,
                    celular   = celular,
                    fotoUrl   = fotoUrl
                )

                // Actualizar contraseña en Supabase Auth
                SupabaseClient.client.auth.updateUser {
                    password = contrasena
                }

                runOnUiThread {
                    Toast.makeText(requireContext(),
                        "Perfil actualizado correctamente",
                        Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(requireContext(),
                        "Error al guardar: ${e.message}",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun runOnUiThread(action: () -> Unit) {
        activity?.runOnUiThread(action)
    }
}