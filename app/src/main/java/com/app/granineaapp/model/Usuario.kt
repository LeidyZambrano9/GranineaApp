package com.app.granineaapp.model

data class Usuario(
    val id: Int,
    val nombreApellido: String,
    val correo: String,
    val celular: String,
    val contrasena: String,
    val rol: Rol = Rol.CLIENTE
)

enum class Rol {
    CLIENTE,
    TRABAJADOR,
    ADMIN
}
