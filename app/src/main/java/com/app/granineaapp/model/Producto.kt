package com.app.granineaapp.model

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val tamanio: String,
    val saboresDisponibles: List<String>,
    val imagenUrl: String = "",
    val disponible: Boolean = true
)
