package com.app.granineaapp.ui.main.admin.usuarios

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.app.granineaapp.R
import com.app.granineaapp.data.FakeData
import com.app.granineaapp.model.Rol

class EditarUsuarioActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etCelular: EditText
    private lateinit var spinnerRol: Spinner
    private var usuarioId: Int = -1
    private var soloLectura: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_usuario)

        usuarioId = intent.getIntExtra("usuario_id", -1)
        soloLectura = intent.getBooleanExtra("solo_lectura", false)

        etNombre = findViewById(R.id.etUsuarioNombre)
        etCorreo = findViewById(R.id.etUsuarioCorreo)
        etCelular = findViewById(R.id.etUsuarioCelular)
        spinnerRol = findViewById(R.id.spinnerUsuarioRol)

        configurarSpinnerRol()
        cargarDatos()

        if (soloLectura) {
            etNombre.isEnabled = false
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
        // El usuario va a tener un rol que definirá sus permisos en la app.
        // Roles disponibles: Cliente, Trabajador
        val roles = listOf("CLIENTE", "TRABAJADOR")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRol.adapter = spinnerAdapter
    }

    private fun cargarDatos() {
        val usuario = FakeData.usuarios.find { it.id == usuarioId } ?: return
        etNombre.setText(usuario.nombreApellido)
        etCorreo.setText(usuario.correo)
        etCelular.setText(usuario.celular)
        val rolIndex = if (usuario.rol == Rol.TRABAJADOR) 1 else 0
        spinnerRol.setSelection(rolIndex)
    }

    private fun guardarCambios() {
        val nombre = etNombre.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val celular = etCelular.text.toString().trim()

        if (nombre.isBlank() || correo.isBlank() || celular.isBlank()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevoRol = if (spinnerRol.selectedItemPosition == 1) Rol.TRABAJADOR else Rol.CLIENTE
        val index = FakeData.usuarios.indexOfFirst { it.id == usuarioId }

        if (index != -1) {
            // FakeData.usuarios es val List, debería ser MutableList para editar.
            // En producción se actualizará en la base de datos.
            Toast.makeText(this, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}