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
import com.google.android.gms.common.api.ApiException
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var etCorreo: EditText
    private lateinit var etContrasena: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Vinculación exacta con los IDs actuales de tu XML
        etCorreo = findViewById(R.id.inputUser)
        etContrasena = findViewById(R.id.inputPassword)

        val btnIniciarSesion = findViewById<Button>(R.id.botonIniciarSesionLogin)
        val txtCrearCuenta = findViewById<TextView>(R.id.txtCrearCuenta)
        val btnGoogle = findViewById<Button>(R.id.btnGoogleLogin)
        val btnHuella = findViewById<ImageButton>(R.id.btnHuellaLogin)

        // 1. LOGIN TRADICIONAL CON SUPABASE
        btnIniciarSesion?.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val clave = etContrasena.text.toString().trim()

            if (correo.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    SupabaseClient.client.auth.signInWith(Email) {
                        email = correo
                        password = clave
                    }
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "¡Bienvenido a Graninea!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // 2. LOGIN CON GOOGLE
        btnGoogle?.setOnClickListener {
            iniciarProcesoGoogle()
        }

        // 3. LOGIN CON HUELLA
        btnHuella?.setOnClickListener {
            iniciarProcesoBiometrico()
        }

        // 4. IR AL REGISTRO
        txtCrearCuenta?.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }

    private fun iniciarProcesoGoogle() {
        // Configuramos las opciones de Google Sign-In pidiendo el ID Token
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1014864077184-crlgc88na95r1sj0fj0r6k49grsercih.apps.googleusercontent.com") // ⚠️ Recuerda cambiar esto por tu Web Client ID real
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Forzamos el cierre de sesión previo local para que siempre permita elegir cuenta en caso de error
        googleSignInClient.signOut().addOnCompleteListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 100)
        }
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
            try {
                // Captura estricta resolviendo excepciones
                val cuenta = task.getResult(ApiException::class.java)
                val tokenDeGoogle = cuenta?.idToken

                if (tokenDeGoogle != null) {
                    // CONEXIÓN INTEGRADA CON SUPABASE AUTH //
                    lifecycleScope.launch {
                        try {
                            // Intercambiamos el token de Google con el sistema de Supabase sin conflictos de variables
                            SupabaseClient.client.auth.signInWith(IDToken) {
                                idToken = tokenDeGoogle
                                provider = Google
                            }

                            runOnUiThread {
                                Toast.makeText(this@LoginActivity, "Bienvenido: ${cuenta.displayName}", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            runOnUiThread {
                                Toast.makeText(this@LoginActivity, "Supabase rechazó el token de Google: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Error: El token de Google llegó vacío", Toast.LENGTH_SHORT).show()
                }

            } catch (e: ApiException) {
                e.printStackTrace()
                Toast.makeText(this, "Fallo en Google Sign-In (Código: ${e.statusCode})", Toast.LENGTH_LONG).show()
            }
        }
    }
}