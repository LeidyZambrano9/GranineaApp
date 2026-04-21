package com.app.granineaapp.ui.main.admin.pedidos

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.granineaapp.R
import com.app.granineaapp.data.FakeData
import com.app.granineaapp.model.EstadoPedido
import com.app.granineaapp.model.Pedido

class DetallePedidoActivity : AppCompatActivity() {

    private lateinit var tvTitulo: TextView
    private lateinit var tvCliente: TextView
    private lateinit var tvProductos: TextView
    private lateinit var tvTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_pedido)

        tvTitulo = findViewById(R.id.tvDetallePedidoTitulo)
        tvCliente = findViewById(R.id.tvDetallePedidoCliente)
        tvProductos = findViewById(R.id.tvDetallePedidoProductos)
        tvTotal = findViewById(R.id.tvDetallePedidoTotal)

        val pedidoId = intent.getIntExtra("pedido_id", -1)
        val pedido = FakeData.pedidos.find { it.id == pedidoId }

        if (pedido != null) mostrarDetalle(pedido)

        findViewById<View>(R.id.btnVolverDetalle).setOnClickListener { finish() }

        findViewById<View>(R.id.btnMarcarEntregadoDetalle)?.setOnClickListener {
            if (pedido != null) marcarEntregado(pedido)
        }
    }

    private fun mostrarDetalle(pedido: Pedido) {
        tvTitulo.text = "Pedido #${pedido.id}"
        tvCliente.text = "Cliente: ${pedido.cliente.nombreApellido}"

        val sb = StringBuilder("Productos:\n")
        pedido.detalles.forEach { d ->
            sb.append("• ${d.producto.nombre} ${d.sabor}\n")
            sb.append("  Cantidad: ${d.cantidad}   Precio: $${String.format("%,.0f", d.precioTotal)}\n")
        }
        tvProductos.text = sb.toString()
        tvTotal.text = "Total: $${String.format("%,.0f", pedido.totalConEnvio)}"
    }

    private fun marcarEntregado(pedido: Pedido) {
        val index = FakeData.pedidos.indexOfFirst { it.id == pedido.id }
        if (index != -1) {
            FakeData.pedidos[index] = pedido.copy(estado = EstadoPedido.ENTREGADO)
            Toast.makeText(this, "Marcado como entregado", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}