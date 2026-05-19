package com.app.granineaapp.ui.main.productos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Producto(
    val id: Long? = null,
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val tipo: String = "sin licor",

    @SerialName("imagen_url")
    val imagenUrl: String = "",

    val sabores: String = "",   // ← nuevo campo: "fresa,mango,uva"

    val activo: Boolean = true,

    @SerialName("created_at")
    val createdAt: String? = null,

    @Transient
    val imagenRes: Int = 0
)