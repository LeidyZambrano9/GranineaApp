package com.app.granineaapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.app.granineaapp.R
import com.app.granineaapp.ui.main.MainActivity // Verifica que esta ruta sea la correcta

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. Enlazamos los componentes del XML con el código
        val btnIniciarSesion = findViewById<Button>(R.id.botonIniciarSesionLogin)
        val txtCrearCuenta = findViewById<TextView>(R.id.txtCrearCuenta)
        val txtRestablecer = findViewById<TextView>(R.id.txtRestablecer)

        // 2. Acción para el botón verde (Ir al Home)
        btnIniciarSesion?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Cerramos el Login para que no pueda volver atrás
        }

        // 3. Acción para "Crear Cuenta" (Ir al Registro)
        txtCrearCuenta?.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        // 4. Acción para "Restablecer Contraseña"
        //txtRestablecer?.setOnClickListener {
          //  val intent = Intent(this, RecuperarPasswordActivity::class.java)
            //startActivity(intent)
        }
    }
