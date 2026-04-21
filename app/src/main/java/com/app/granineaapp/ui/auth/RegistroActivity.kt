package com.app.granineaapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.granineaapp.R

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        // Ajuste de padding para que el contenido no se solape con la barra de estado
        val mainView = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Enlazamos los componentes de la interfaz con tus NUEVOS IDs
        val etNombre = findViewById<EditText>(R.id.RegistroNombre)
        val etCorreo = findViewById<EditText>(R.id.RegistroCorreo)
        val etCelular = findViewById<EditText>(R.id.RegistroCelular)
        val etPassword = findViewById<EditText>(R.id.RegistroContraseña)
        val btnAccion = findViewById<Button>(R.id.BotonoCrearcuentaRegistro)

        val cardError = findViewById<CardView>(R.id.cardError)
        val cardExito = findViewById<CardView>(R.id.TarjetaCuentacreadaRegistro)
        val layoutFormulario = findViewById<LinearLayout>(R.id.layoutFormulario)

        // 2. Lógica del botón principal
        btnAccion.setOnClickListener {

            // SIEMPRE que el botón diga "Volver", redirigimos al Login
            if (btnAccion.text.toString().equals("Volver", ignoreCase = true)) {
                val intent = Intent(this, LoginActivity::class.java)
                // Limpia el historial para que no se pueda volver al registro vacío
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
                finish()
            }
            else {
                // Obtenemos los textos para validar
                val nombre = etNombre.text.toString().trim()
                val correo = etCorreo.text.toString().trim()
                val celular = etCelular.text.toString().trim()
                val pass = etPassword.text.toString().trim()

                // VALIDACIÓN: Si falta cualquier campo, mostramos la tarjeta roja
                if (nombre.isEmpty() || correo.isEmpty() || celular.isEmpty() || pass.isEmpty()) {
                    cardExito.visibility = View.GONE
                    cardError.visibility = View.VISIBLE
                }
                else {
                    // TODO CORRECTO: Mostramos la tarjeta blanca de éxito
                    cardError.visibility = View.GONE
                    cardExito.visibility = View.VISIBLE

                    // Ocultamos el bloque de campos (layoutFormulario)
                    layoutFormulario.visibility = View.GONE

                    // Cambiamos el texto del botón a "Volver"
                    btnAccion.text = "Volver"
                }
            }
        }
    }
}