package com.app.granineaapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.app.granineaapp.R
import com.app.granineaapp.ui.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnIniciarSesion = findViewById<Button>(R.id.botonIniciarSesionLogin)
        val txtCrearCuenta = findViewById<TextView>(R.id.txtCrearCuenta)
        val btnGoogle = findViewById<Button>(R.id.btnGoogleLogin)
        val btnHuella = findViewById<ImageButton>(R.id.btnHuellaLogin)

        // 1. Login Tradicional
        btnIniciarSesion?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // 2. Login con Google
        btnGoogle?.setOnClickListener {
            iniciarProcesoGoogle()
        }

        // 3. Login con Huella
        btnHuella?.setOnClickListener {
            iniciarProcesoBiometrico()
        }

        // 4. Registro
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
                    // Error al abrir el sensor
                    Toast.makeText(this@LoginActivity, "Error: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // ESTO ES LO QUE HACE QUE ENTRE A LA APP
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "¡Huella confirmada!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Huella puesta pero no es la correcta
                    Toast.makeText(this@LoginActivity, "Huella no reconocida", Toast.LENGTH_SHORT).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Acceso Biométrico")
            .setSubtitle("Usa tu huella para entrar a Graninea")
            .setNegativeButtonText("Cancelar")
            // CLAVE: Permite biometría fuerte (dedo real) o débil (emuladores)
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