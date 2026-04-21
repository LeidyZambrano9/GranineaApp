package com.app.granineaapp.model

import com.app.granineaapp.model.Producto

data class DetallePedido(
    val producto: Producto,
    val sabor: String,
    val cantidad: Int,
    val precioUnitario: Double
) {
    val precioTotal: Double get() = precioUnitario * cantidad
}