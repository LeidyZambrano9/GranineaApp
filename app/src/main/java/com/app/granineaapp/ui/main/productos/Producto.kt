package com.app.granineaapp.ui.main.productos

enum class TipoProducto { SIN_LICOR, CON_LICOR, XL }

data class Producto(
    val nombre: String,
    val precio: Double,
    val imagenRes: Int,
    val tipo: TipoProducto
)