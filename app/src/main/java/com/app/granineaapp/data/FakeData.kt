package com.app.granineaapp.data

import com.app.granineaapp.model.*
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

    val productos: MutableList<Producto> = mutableListOf(
        Producto(
            id = 1,
            nombre = "El Chiki",
            descripcion = "Granizado pequeño de 8 onzas. Incluye: 1 Oka Loka, 1 banderita, 1 gusanito.",
            precio = 5500.0,
            tamanio = "8 onzas",
            saboresDisponibles = listOf("Uva", "Maracumango", "Mango biche", "Fresa", "Limón"),
            imagenUrl = "ic_chiki",
            disponible = true
        ),
        Producto(
            id = 2,
            nombre = "El Neita",
            descripcion = "Granizado mediano con sabores intensos y toppings premium.",
            precio = 8000.0,
            tamanio = "12 onzas",
            saboresDisponibles = listOf("Uva", "Fresa", "Mango biche", "Maracuyá", "Limón"),
            imagenUrl = "ic_neita",
            disponible = true
        ),
        Producto(
            id = 3,
            nombre = "Chocopa",
            descripcion = "Granizado sabor chocolate con papas. La combinación perfecta.",
            precio = 10000.0,
            tamanio = "12 onzas",
            saboresDisponibles = listOf("Chocolate", "Chocolate con leche", "Chocolate amargo"),
            imagenUrl = "ic_chocopa",
            disponible = true
        ),
        Producto(
            id = 4,
            nombre = "Litroski",
            descripcion = "El granizado gigante de un litro para los más antojados.",
            precio = 15000.0,
            tamanio = "1 litro",
            saboresDisponibles = listOf("Uva", "Fresa", "Mango biche", "Maracumango", "Limón", "Mora"),
            imagenUrl = "ic_litroski",
            disponible = true
        ),
        Producto(
            id = 5,
            nombre = "Chocorramo con licor",
            descripcion = "Granizado con chocorramo y un toque de licor. Solo para mayores.",
            precio = 12000.0,
            tamanio = "10 onzas",
            saboresDisponibles = listOf("Uva con licor", "Fresa con licor", "Maracumango con licor"),
            imagenUrl = "ic_chocorramo_licor",
            disponible = true
        ),
        Producto(
            id = 6,
            nombre = "Chocorramo sin licor",
            descripcion = "Granizado con chocorramo sin licor. Para toda la familia.",
            precio = 9000.0,
            tamanio = "10 onzas",
            saboresDisponibles = listOf("Uva", "Fresa", "Mango biche"),
            imagenUrl = "ic_chocorramo",
            disponible = true
        )
    )

    // ─────────────────────────────────────────
    //  PEDIDOS
    // ─────────────────────────────────────────

    private fun fecha(diasAtras: Int): java.util.Date {
        return Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -diasAtras) }.time
    }

    val pedidos: MutableList<Pedido> = mutableListOf(

        // Pedido 32 — Pendiente (aparece en mockup Admin)
        Pedido(
            id = 32,
            cliente = usuarios[1],   // Natalia Lopez
            detalles = listOf(
                DetallePedido(productos[0], "Uva", 2, 5500.0),
                DetallePedido(productos[2], "Chocolate", 3, 10000.0)
            ),
            direccionEntrega = "Calle 128 c# 105 9 25",
            referenciaDireccion = "encontrarse en la puerta",
            tipoConstruccion = "Casa",
            piso = "",
            telefono = "3504404302",
            observaciones = "Timbrar en el tercer piso",
            metodoPago = MetodoPago.EFECTIVO,
            estado = EstadoPedido.PENDIENTE,
            fecha = fecha(0),
            tarifaEnvio = 0.0
        ),

        // Pedido 33 — Pendiente
        Pedido(
            id = 33,
            cliente = usuarios[3],   // Carlos Perez
            detalles = listOf(
                DetallePedido(productos[1], "Fresa", 1, 8000.0),
                DetallePedido(productos[3], "Mango biche", 1, 15000.0)
            ),
            direccionEntrega = "Calle 130 C # 10A - 82",
            referenciaDireccion = "Portería principal",
            tipoConstruccion = "Apartamento",
            piso = "Piso 5",
            telefono = "3001112233",
            observaciones = "",
            metodoPago = MetodoPago.NEQUI,
            estado = EstadoPedido.PENDIENTE,
            fecha = fecha(0),
            tarifaEnvio = 0.0
        ),

        // Pedido 34 — Detalle exacto del mockup Admin (Adriana)
        Pedido(
            id = 34,
            cliente = usuarios[2],   // Adriana Gomez
            detalles = listOf(
                DetallePedido(productos[0], "Fresa", 2, 6000.0),   // Granizado fresa x2 = 12.000
                DetallePedido(productos[1], "Mango", 3, 2000.0)    // Granizado mango x3 = 6.000
            ),
            direccionEntrega = "Carrera 108A # 139-32",
            referenciaDireccion = "Graninea Suba",
            tipoConstruccion = "Local",
            telefono = "3208822559",
            metodoPago = MetodoPago.EFECTIVO,
            estado = EstadoPedido.PENDIENTE,
            fecha = fecha(0),
            tarifaEnvio = 0.0
        ),

        // Pedido 35 — En preparación
        Pedido(
            id = 35,
            cliente = usuarios[4],   // Laura Martinez
            detalles = listOf(
                DetallePedido(productos[4], "Uva con licor", 2, 12000.0)
            ),
            direccionEntrega = "Cra 91 # 128B - 50",
            tipoConstruccion = "Casa",
            telefono = "3114445566",
            metodoPago = MetodoPago.DEBITO,
            estado = EstadoPedido.EN_PREPARACION,
            fecha = fecha(0),
            tarifaEnvio = 0.0
        ),

        // Historial — Pedido entregado hace 1 día
        Pedido(
            id = 30,
            cliente = usuarios[1],   // Natalia Lopez
            detalles = listOf(
                DetallePedido(productos[2], "Chocolate", 1, 10000.0)
            ),
            direccionEntrega = "Calle 128 c# 105 9 25",
            telefono = "3504404302",
            metodoPago = MetodoPago.EFECTIVO,
            estado = EstadoPedido.ENTREGADO,
            fecha = fecha(1),
            tarifaEnvio = 0.0
        ),

        // Historial — Pedido entregado hace 7 días (aparece en historial mockup cliente)
        Pedido(
            id = 28,
            cliente = usuarios[1],
            detalles = listOf(
                DetallePedido(productos[0], "Uva", 2, 5500.0),        // Granizado fresa x2 = 24.000 (mockup)
                DetallePedido(productos[1], "Mango biche", 3, 15000.0) // Granizado mango x3 = 45.000
            ),
            direccionEntrega = "Calle 128 c# 105 9 25",
            telefono = "3504404302",
            metodoPago = MetodoPago.NEQUI,
            estado = EstadoPedido.ENTREGADO,
            fecha = fecha(7),    // 22 de Enero aprox
            tarifaEnvio = 0.0
        ),

        // Historial — más pedidos para llenar la lista del mockup cliente
        Pedido(
            id = 25,
            cliente = usuarios[1],
            detalles = listOf(DetallePedido(productos[3], "Fresa", 1, 15000.0)),
            direccionEntrega = "Calle 128 c# 105 9 25",
            telefono = "3504404302",
            metodoPago = MetodoPago.EFECTIVO,
            estado = EstadoPedido.ENTREGADO,
            fecha = fecha(15),   // 13 de Enero aprox
            tarifaEnvio = 0.0
        ),
        Pedido(
            id = 22,
            cliente = usuarios[1],
            detalles = listOf(DetallePedido(productos[5], "Uva", 2, 9000.0)),
            direccionEntrega = "Calle 128 c# 105 9 25",
            telefono = "3504404302",
            metodoPago = MetodoPago.EFECTIVO,
            estado = EstadoPedido.ENTREGADO,
            fecha = fecha(25),   // 03 de Enero aprox
            tarifaEnvio = 0.0
        ),
        Pedido(
            id = 19,
            cliente = usuarios[1],
            detalles = listOf(
                DetallePedido(productos[4], "Fresa con licor", 1, 12000.0),
                DetallePedido(productos[0], "Limón", 1, 5500.0)
            ),
            direccionEntrega = "Calle 128 c# 105 9 25",
            telefono = "3504404302",
            metodoPago = MetodoPago.NEQUI,
            estado = EstadoPedido.ENTREGADO,
            fecha = fecha(33),   // 24 de Diciembre aprox
            tarifaEnvio = 0.0
        ),
        Pedido(
            id = 15,
            cliente = usuarios[1],
            detalles = listOf(DetallePedido(productos[2], "Chocolate amargo", 3, 10000.0)),
            direccionEntrega = "Calle 128 c# 105 9 25",
            telefono = "3504404302",
            metodoPago = MetodoPago.EFECTIVO,
            estado = EstadoPedido.ENTREGADO,
            fecha = fecha(50),   // 07 de Diciembre aprox
            tarifaEnvio = 0.0
        )
    )

    // ─────────────────────────────────────────
    //  HELPERS
    // ─────────────────────────────────────────

    /** Pedidos pendientes para el panel Admin */
    fun pedidosPendientes(): List<Pedido> =
        pedidos.filter { it.estado == EstadoPedido.PENDIENTE }

    /** Todos los pedidos de un cliente específico (historial) */
    fun pedidosDeCliente(usuarioId: Int): List<Pedido> =
        pedidos.filter { it.cliente.id == usuarioId }
            .sortedByDescending { it.fecha }

    /** Buscar producto por nombre (para el buscador del Admin) */
    fun buscarProducto(query: String): List<Producto> =
        productos.filter { it.nombre.contains(query, ignoreCase = true) }

    /** Estadísticas del Home Admin */
    val totalUsuariosRegistrados: Int get() = usuarios.count { it.rol == Rol.CLIENTE }
    val pedidosHoy: Int get() = pedidos.count { it.estado != EstadoPedido.ENTREGADO && it.estado != EstadoPedido.CANCELADO }
    val productosDisponibles: Int get() = productos.count { it.disponible }
}