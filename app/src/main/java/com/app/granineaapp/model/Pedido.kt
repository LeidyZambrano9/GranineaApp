package com.app.granineaapp.model


import com.app.granineaapp.model.Usuario

import com.app.granineaapp.model.DetallePedido
import java.util.Date


// ✅ Versión simplificada para modo visual
data class Pedido(
    val id: Int = 0,
    val cliente: String = "",
    val total: Double = 0.0,
    val estado: String = "Pendiente"  // ✅ Cambiado de EstadoPedido a String
)