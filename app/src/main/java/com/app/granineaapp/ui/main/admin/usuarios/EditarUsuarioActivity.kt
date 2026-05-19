package com.app.granineaapp.ui.main.admin.usuarios

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.app.granineaapp.R
import com.app.granineaapp.data.UsuarioRepository
import kotlinx.coroutines.launch

class EditarUsuarioActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etCelular: EditText
    private lateinit var spinnerRol: Spinner
    private var usuarioId: String = ""
    private var soloLectura: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_usuario)

        usuarioId = intent.getStringExtra("usuario_id") ?: ""
        soloLectura = intent.getBooleanExtra("solo_lectura", false)

        etNombre = findViewById(R.id.etUsuarioNombre)
        etApellido = findViewById(R.id.etUsuarioApellido)
        etCorreo = findViewById(R.id.etUsuarioCorreo)
        etCelular = findViewById(R.id.etUsuarioCelular)
        spinnerRol = findViewById(R.id.spinnerUsuarioRol)

        configurarSpinnerRol()
        cargarDatos()

        if (soloLectura) {
            etNombre.isEnabled = false
            etApellido.isEnabled = false
            etCorreo.isEnabled = false
            etCelular.isEnabled = false
            spinnerRol.isEnabled = false
            findViewById<View>(R.id.btnGuardarUsuario).visibility = View.GONE
        } else {
            findViewById<View>(R.id.btnGuardarUsuario).setOnClickListener { guardarCambios() }
        }

        findViewById<View>(R.id.btnVolverUsuario).setOnClickListener { finish() }
    }

    private fun configurarSpinnerRol() {
        val roles = listOf("cliente", "admin")
        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            roles
        ) {
            override fun getView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                view.findViewById<TextView>(android.R.id.text1)
                    .setTextColor(resources.getColor(android.R.color.white))
                return view
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRol.adapter = adapter
    }

    private fun cargarDatos() {
        lifecycleScope.launch {
            val usuario = UsuarioRepository.obtenerUsuarioPorId(usuarioId) ?: return@launch
            runOnUiThread {
                etNombre.setText(usuario.nombres)
                etApellido.setText(usuario.apellidos)
                etCorreo.setText(usuario.correo ?: "")
                etCelular.setText(usuario.celular ?: "")
                val roles = listOf("cliente", "admin")
                spinnerRol.setSelection(roles.indexOf(usuario.rol).coerceAtLeast(0))
            }
        }
    }

    private fun guardarCambios() {
        val nombres = etNombre.text.toString().trim()
        val apellidos = etApellido.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val celular = etCelular.text.toString().trim()

        if (nombres.isBlank() || apellidos.isBlank() || correo.isBlank()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevoRol = spinnerRol.selectedItem.toString()

        lifecycleScope.launch {
            UsuarioRepository.actualizarUsuarioPorAdmin(
                id = usuarioId,
                nombres = nombres,
                apellidos = apellidos,
                correo = correo,
                celular = celular,
                rol = nuevoRol
            )
            runOnUiThread {
                Toast.makeText(this@EditarUsuarioActivity, "Usuario actualizado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}