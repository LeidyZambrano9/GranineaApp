package com.app.granineaapp.data

import com.app.granineaapp.model.*
import com.app.granineaapp.ui.main.productos.Producto

import java.util.Calendar


object FakeData {

    // ─────────────────────────────────────────
    //  USUARIOS
    // ─────────────────────────────────────────

    val usuarios: List<Usuario> = listOf(
        Usuario(
            id = 1,
            nombreApellido = "Daniel Rodriguez",
            correo = "dfrodriguezgarcia@ucompensar.edu.co",
            celular = "3133062544",
            contrasena = "Pass@1234",
            rol = Rol.ADMIN
        ),
        Usuario(
            id = 2,
            nombreApellido = "Natalia Lopez",
            correo = "natalialopez@gmail.com",
            celular = "3504404302",
            contrasena = "Cliente@1",
            rol = Rol.CLIENTE
        ),
        Usuario(
            id = 3,
            nombreApellido = "Adriana Gomez",
            correo = "adrianagomez@outlook.com",
            celular = "3208822559",
            contrasena = "Cliente@2",
            rol = Rol.CLIENTE
        ),
        Usuario(
            id = 4,
            nombreApellido = "Carlos Perez",
            correo = "carlosperez@gmail.com",
            celular = "3001112233",
            contrasena = "Cliente@3",
            rol = Rol.CLIENTE
        ),
        Usuario(
            id = 5,
            nombreApellido = "Laura Martinez",
            correo = "lauramartinez@hotmail.com",
            celular = "3114445566",
            contrasena = "Cliente@4",
            rol = Rol.TRABAJADOR
        ),
        Usuario(
            id = 6,
            nombreApellido = "Miguel Torres",
            correo = "migueltorres@gmail.com",
            celular = "3007778899",
            contrasena = "Cliente@5",
            rol = Rol.CLIENTE
        )
    )

    // Usuario logueado de prueba (cliente)
    val usuarioActual: Usuario get() = usuarios[1]

    // Usuario admin de prueba
    val usuarioAdmin: Usuario get() = usuarios[0]

    // ─────────────────────────────────────────
    //  PRODUCTOS
    // ─────────────────────────────────────────

}