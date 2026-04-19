package com.app.granineaapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.granineaapp.R
import com.app.granineaapp.ui.inicio.HomeActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. Enlazamos los componentes del XML con el código
        val btnIniciarSesion = findViewById<Button>(R.id.botonIniciarSesionLogin)
        val txtCrearCuenta = findViewById<TextView>(R.id.txtCrearCuenta)

        // 2. Acción para el botón de Inicio (BLOQUEADO POR AHORA)
        btnIniciarSesion?.setOnClickListener {
            // Quitamos el Intent al Home. Solo mostramos un mensaje.
            Toast.makeText(this, "Ingreso no disponible aún", Toast.LENGTH_SHORT).show()
        }

        // 3. Acción para "Crear Cuenta" (Ir al Registro)
        txtCrearCuenta?.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}