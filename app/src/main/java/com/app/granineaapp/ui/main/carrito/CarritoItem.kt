package com.app.granineaapp.ui.main.carrito

data class CarritoItem(
    val nombre: String,
    val precio: Double,
    val imagenRes: Int = 0,
    val imagenUrl: String = "",   // ← nuevo
    var cantidad: Int,
    val sabor: String
)