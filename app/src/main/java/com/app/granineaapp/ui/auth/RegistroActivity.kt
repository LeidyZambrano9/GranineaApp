package com.app.granineaapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.app.granineaapp.ui.auth.LoginActivity
import com.app.granineaapp.SupabaseClient
import com.app.granineaapp.R
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class RegistroActivity : AppCompatActivity() {

    private lateinit var etNombres: EditText
    private lateinit var etApellidos: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etCelular: EditText
    private lateinit var etContrasena: EditText
    private lateinit var etReContrasena: EditText
    private lateinit var checkTerminos: CheckBox
    private lateinit var btnRegistro: Button

    // Estructura de datos actualizada en Supabase con los campos celular y correo
    @Serializable
    data class UsuarioData (
        val id: String,
        val nombres: String,
        val apellidos: String,
        val celular: String,
        val correo: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        // Vinculación exacta con los nuevos IDs de tu XML
        val txtVolverLogin = findViewById<TextView>(R.id.txt_volver_login)

        etNombres = findViewById(R.id.RegistroNombres)
        etApellidos = findViewById(R.id.RegistroApellidos)
        etCorreo = findViewById(R.id.RegistroCorreo)
        etCelular = findViewById(R.id.RegistroCelular)
        etContrasena = findViewById(R.id.RegistroContraseña)
        etReContrasena = findViewById(R.id.RegistroRepetirContraseña)
        btnRegistro = findViewById(R.id.BotonoCrearcuentaRegistro)
        checkTerminos = findViewById(R.id.CheckTerminos_Registro)

        // ESCUCHAR EL BOTON DE REGISTRO //
        btnRegistro.setOnClickListener {
            val nombres = etNombres.text.toString().trim()
            val apellidos = etApellidos.text.toString().trim()
            val correo = etCorreo.text.toString().trim()
            val celular = etCelular.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()
            val recontrasena = etReContrasena.text.toString().trim()

            // VALIDACIONES DE CAMPOS VACÍOS //
            if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || celular.isEmpty() || contrasena.isEmpty() || recontrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, llenar todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // VALIDACIÓN DEL FORMATO DE CORREO: Debe contener un @ //
            if (!correo.contains("@")) {
                Toast.makeText(this, "Por favor ingrese un correo válido (Debe contener '@')", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // VALIDACIÓN ESTRICTA DE CELULAR: Exactamente 10 dígitos //
            if (celular.length != 10) {
                Toast.makeText(this, "El número de celular debe tener exactamente 10 dígitos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // VALIDACIÓN DE CONTRASEÑA //
            if (contrasena.length < 8) {
                Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (contrasena != recontrasena){
                Toast.makeText(this, "Las contraseñas no coinciden, verifique nuevamente.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!checkTerminos.isChecked){
                Toast.makeText(this, "Por favor acepta los términos y condiciones", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // REGISTRO EN SUPABASE BLINDADO //
            lifecycleScope.launch {
                try {
                    // PASO 1: Registrar el usuario en Supabase Auth
                    SupabaseClient.client.auth.signUpWith(Email) {
                        email = correo
                        password = contrasena
                    }

                    // Se captura el UUID de Auth de forma estricta para evitar nulos
                    val uuidDelUsuario = SupabaseClient.client.auth.currentUserOrNull()?.id

                    if (uuidDelUsuario != null) {
                        // PASO 2: Guardar los datos adicionales en la tabla Usuarios (incluye celular y correo)
                        SupabaseClient.client.postgrest["Usuarios"].insert(
                            UsuarioData(
                                id = uuidDelUsuario,
                                nombres = nombres,
                                apellidos = apellidos,
                                celular = celular,
                                correo = correo
                            )
                        )

                        // PASO 3: Redirigir al usuario al Login si todo sale melo
                        runOnUiThread {
                            Toast.makeText(this@RegistroActivity, "¡Registro exitoso en Graninea!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@RegistroActivity, LoginActivity::class.java))
                            finish()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@RegistroActivity, "Error: No se pudo generar el ID de usuario", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        // Alerta visual por si la tabla rechaza la estructura
                        android.app.AlertDialog.Builder(this@RegistroActivity)
                            .setTitle("Aviso de la Base de Datos")
                            .setMessage("El usuario se creó en Auth, pero la tabla Usuarios rechazó los datos:\n\n${e.localizedMessage ?: e.message}")
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }
            }
        }

        // Volver al Login //
        txtVolverLogin?.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}