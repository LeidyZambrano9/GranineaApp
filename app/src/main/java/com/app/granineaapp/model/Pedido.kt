package com.app.granineaapp.model


import com.app.granineaapp.model.Usuario
import com.app.granineaapp.model.DetallePedido
import java.util.Date

data class Pedido(
    val id: Int = 0,
    val cliente: Usuario,
    val detalles: List<DetallePedido>,
    val direccionEntrega: String,
    val referenciaDireccion: String = "",
    val tipoConstruccion: String = "",        // "casa", "apartamento", etc.
    val piso: String = "",
    val telefono: String = "",
    val observaciones: String = "",
    val puntoEncuentroLat: Double? = null,
    val puntoEncuentroLng: Double? = null,
    val metodoPago: MetodoPago = MetodoPago.EFECTIVO,
    val estado: EstadoPedido = EstadoPedido.PENDIENTE,
    val fecha: Date = Date(),
    val tarifaEnvio: Double = 0.0
) {
    val totalProductos: Double get() = detalles.sumOf { it.precioTotal }
    val totalConEnvio: Double get() = totalProductos + tarifaEnvio
}

enum class EstadoPedido {
    PENDIENTE,
    EN_PREPARACION,
    EN_CAMINO,
    ENTREGADO,
    CANCELADO
}

enum class MetodoPago {
    EFECTIVO,
    NEQUI,
    DEBITO
}
