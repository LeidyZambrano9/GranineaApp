package com.app.granineaapp.ui.main.admin.pedidos

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.granineaapp.R

class DetallePedidoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_pedido)  // ✅ Tu layout con fondo negro

        // ✅ Referencias a las views del layout
        val tvTitulo = findViewById<TextView>(R.id.tvDetallePedidoTitulo)
        val tvCliente = findViewById<TextView>(R.id.tvDetallePedidoCliente)
        val tvProductos = findViewById<TextView>(R.id.tvDetallePedidoProductos)
        val tvTotal = findViewById<TextView>(R.id.tvDetallePedidoTotal)
        val btnMarcar = findViewById<Button>(R.id.btnMarcarEntregadoDetalle)
        val btnVolver = findViewById<Button>(R.id.btnVolverDetalle)

        // ✅ Recibir datos del intent (o usar dummy si no vienen)
        val pedidoId = intent.getIntExtra("pedido_id", 0)
        val cliente = intent.getStringExtra("pedido_cliente") ?: "Cliente Ejemplo"
        val total = intent.getDoubleExtra("pedido_total", 0.0)
        val estado = intent.getStringExtra("pedido_estado") ?: "Pendiente"

        // ✅ Llenar campos visualmente
        tvTitulo.text = "Pedido #$pedidoId"
        tvCliente.text = "Cliente: $cliente"
        tvProductos.text = """
            • Granizado fresa x2 ....... $4.000
            • Granizado mango x2 ...... $6.000
            • Chocopa x1 .............. $8.000
        """.trimIndent()
        tvTotal.text = "Total: $${String.format("%.0f", total)}"

        // ✅ Botón Marcar Entregado (visual)
        btnMarcar.setOnClickListener {
            Toast.makeText(this, "✅ Pedido marcado como entregado", Toast.LENGTH_SHORT).show()
            finish()
        }

        // ✅ Botón Volver
        btnVolver.setOnClickListener {
            finish()
        }
    }
}