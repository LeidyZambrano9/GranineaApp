package com.app.granineaapp.model


import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: String = "",
    val nombres: String = "",
    val apellidos: String = "",
    val correo: String? = null,
    val celular: String? = null,
    val rol: String = "cliente",
    val foto_url: String? = null
)
