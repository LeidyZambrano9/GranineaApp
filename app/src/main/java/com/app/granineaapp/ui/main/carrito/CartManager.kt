package com.app.granineaapp.ui.main.carrito

object CartManager {
    private val items = mutableListOf<CarritoItem>()

    fun agregarProducto(item: CarritoItem) {
        // Si el producto con el mismo nombre y sabor ya está, aumentamos cantidad
        val existente = items.find { it.nombre == item.nombre && it.sabor == item.sabor }
        if (existente != null) {
            existente.cantidad += item.cantidad
        } else {
            items.add(item)
        }
    }

    fun obtenerItems(): List<CarritoItem> = items

    fun limpiarCarrito() {
        items.clear()
    }

    fun eliminarItem(item: CarritoItem) {
        items.remove(item)
    }

    fun obtenerTotal(): Double {
        return items.sumOf { it.precio * it.cantidad }
    }
}