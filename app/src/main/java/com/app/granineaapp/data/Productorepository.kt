package com.app.granineaapp.data

import com.app.granineaapp.SupabaseClient
import com.app.granineaapp.ui.main.productos.Producto
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.storage

object ProductoRepository {

    // Nombre exacto del bucket en Supabase Storage
    private const val BUCKET = "productos"

    // ── SUBIR IMAGEN ──────────────────────────────────────────────────────────
    /**
     * Sube los bytes de una imagen al bucket y devuelve la URL pública.
     * @param nombreArchivo  ej. "producto_1234567890.jpg"
     * @param bytes          contenido del archivo
     * @param mimeType       ej. "image/jpeg" o "image/png"
     */
    suspend fun subirImagen(nombreArchivo: String, bytes: ByteArray, mimeType: String): String {
        val storage = SupabaseClient.client.storage
        val bucket = storage.from(BUCKET)

        bucket.upload(nombreArchivo, bytes) {
            contentType = io.ktor.http.ContentType.parse(mimeType)
            upsert = true
        }

        // En SDK 3.x la URL pública se construye manualmente así:
        val supabaseUrl = "https://cugmrfnyaoglbdfstrjg.supabase.co"
        return "$supabaseUrl/storage/v1/object/public/$BUCKET/$nombreArchivo"
    }

    // ── ELIMINAR IMAGEN (opcional, para limpiar al desactivar producto) ────────
    suspend fun eliminarImagen(nombreArchivo: String) {
        runCatching {
            SupabaseClient.client.storage.from(BUCKET).delete(listOf(nombreArchivo))
        }
    }


    // ── LEER ─────────────────────────────────────────────────────────────────

    suspend fun obtenerProductos(): List<Producto> {
        return SupabaseClient.client
            .from("productos")
            .select {
                filter { eq("activo", true) }
                order("id", Order.ASCENDING)
            }
            .decodeList<Producto>()
    }

    // ── CREAR ─────────────────────────────────────────────────────────────────

    suspend fun crearProducto(producto: Producto): Producto {
        return SupabaseClient.client
            .from("productos")
            .insert(producto) {
                select()
            }
            .decodeSingle<Producto>()
    }

    // ── ACTUALIZAR ────────────────────────────────────────────────────────────

    suspend fun actualizarProducto(id: Long, producto: Producto) {
        SupabaseClient.client
            .from("productos")
            .update(producto) {
                filter { eq("id", id) }
            }
    }

    // ── DESACTIVAR (soft delete) ──────────────────────────────────────────────

    suspend fun desactivarProducto(id: Long) {
        SupabaseClient.client
            .from("productos")
            .update({ set("activo", false) }) {
                filter { eq("id", id) }
            }
    }
}