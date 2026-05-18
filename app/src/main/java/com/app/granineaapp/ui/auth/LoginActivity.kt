package com.app.granineaapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.app.granineaapp.R
import com.app.granineaapp.SupabaseClient
import com.app.granineaapp.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    // Se declaran los EditText para poder capturar el texto
    private lateinit var etCorreo: EditText
    private lateinit var etContrasena: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Referencias a los componentes de tu XML
        etCorreo = findViewById(R.id.inputUser) // ⚠️ Verifica que este ID coincida con tu XML
        etContrasena = findViewById(R.id.inputPassword) // ⚠️ Verifica que este ID coincida con tu XML

        val btnIniciarSesion = findViewById<Button>(R.id.botonIniciarSesionLogin)
        val txtCrearCuenta = findViewById<TextView>(R.id.txtCrearCuenta)
        val btnGoogle = findViewById<Button>(R.id.btnGoogleLogin)
        val btnHuella = findViewById<ImageButton>(R.id.btnHuellaLogin)

        // 1. Login Tradicional COMPLETAMENTE REAL CON SUPABASE
        btnIniciarSesion?.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val clave = etContrasena.text.toString().trim()

            // Validación inicial de campos vacíos
            if (correo.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Ejecución de la petición en la corrutina
            lifecycleScope.launch {
                try {
                    // Intentamos autenticar con Supabase Auth
                    SupabaseClient.client.auth.signInWith(Email) {
                        email = correo
                        password = clave
                    }

                    // Si pasa aquí, las credenciales existen y son correctas
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "¡Bienvenido a Graninea!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    // Si el usuario no existe o la contraseña no coincide, cae al catch
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // 2. Login con Google (Intacto)
        btnGoogle?.setOnClickListener {
            iniciarProcesoGoogle()
        }

        // 3. Login con Huella (Intacto)
        btnHuella?.setOnClickListener {
            iniciarProcesoBiometrico()
        }

        // 4. Registro (Intacto)
        txtCrearCuenta?.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }

    private fun iniciarProcesoGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 100)
    }

    private fun iniciarProcesoBiometrico() {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(this@LoginActivity, "Error: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "¡Huella confirmada!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(this@LoginActivity, "Huella no reconocida", Toast.LENGTH_SHORT).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Acceso Biométrico")
            .setSubtitle("Usa tu huella para entrar a Graninea")
            .setNegativeButtonText("Cancelar")
            .setAllowedAuthenticators(BIOMETRIC_STRONG or BIOMETRIC_WEAK)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                val cuenta = task.result
                Toast.makeText(this, "Bienvenido: ${cuenta?.displayName}", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "No se pudo conectar con Google", Toast.LENGTH_SHORT).show()
            }
        }
    }
}