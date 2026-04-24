package com.app.granineaapp.model


data class DetallePedido(
    val id: Int = 0,
    val sabor: String,
    val cantidad: Int,
    val precioUnitario: Double
) {
    val precioTotal: Double get() = precioUnitario * cantidad
}

