package com.app.granineaapp.ui.main.carrito

data class CarritoItem(
    val nombre: String,
    val precio: Double,
    val imagenRes: Int,
    var cantidad: Int,
    val sabor: String
)