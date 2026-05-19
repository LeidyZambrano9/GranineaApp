package com.app.granineaapp.data

import android.content.ContentResolver
import android.net.Uri
import com.app.granineaapp.SupabaseClient
import com.app.granineaapp.ui.main.productos.Producto
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.storage

object ProductoRepository {

    private const val BUCKET = "productos"

    // ── SUBIR IMAGEN (por URI desde galería) ──────────────────────────────────
    suspend fun subirImagen(uri: Uri, contentResolver: ContentResolver): String? {
        return try {
            val bytes = contentResolver.openInputStream(uri)?.readBytes() ?: return null
            val fileName = "producto_${System.currentTimeMillis()}.jpg"

            SupabaseClient.client.storage
                .from(BUCKET)
                .upload(fileName, bytes) {
                    contentType = io.ktor.http.ContentType.parse("image/jpeg")
                    upsert = true
                }

            val supabaseUrl = "https://cugmrfnyaoglbdfstrjg.supabase.co"
            "$supabaseUrl/storage/v1/object/public/$BUCKET/$fileName"
        } catch (e: Exception) {
            null
        }
    }

    // ── SUBIR IMAGEN (por bytes directos) ────────────────────────────────────
    suspend fun subirImagen(nombreArchivo: String, bytes: ByteArray, mimeType: String): String {
        SupabaseClient.client.storage
            .from(BUCKET)
            .upload(nombreArchivo, bytes) {
                contentType = io.ktor.http.ContentType.parse(mimeType)
                upsert = true
            }

        val supabaseUrl = "https://cugmrfnyaoglbdfstrjg.supabase.co"
        return "$supabaseUrl/storage/v1/object/public/$BUCKET/$nombreArchivo"
    }

    // ── ELIMINAR IMAGEN ───────────────────────────────────────────────────────
    suspend fun eliminarImagen(nombreArchivo: String) {
        runCatching {
            SupabaseClient.client.storage.from(BUCKET).delete(listOf(nombreArchivo))
        }
    }

    // ── LEER (clientes) — solo productos activos ──────────────────────────────
    suspend fun obtenerProductos(): List<Producto> {
        return SupabaseClient.client
            .from("productos")
            .select {
                filter { eq("activo", true) }
                order("id", Order.ASCENDING)
            }
            .decodeList<Producto>()
    }

    // ── LEER (admin) — todos los productos, activos e inactivos ───────────────
    suspend fun obtenerTodosLosProductos(): List<Producto> {
        return SupabaseClient.client
            .from("productos")
            .select {
                order("id", Order.ASCENDING)
            }
            .decodeList<Producto>()
    }

    // ── CREAR ─────────────────────────────────────────────────────────────────
    suspend fun crearProducto(producto: Producto): Producto {
        return SupabaseClient.client
            .from("productos")
            .insert(producto) { select() }
            .decodeSingle<Producto>()
    }

    // ── ACTUALIZAR ────────────────────────────────────────────────────────────
    suspend fun actualizarProducto(producto: Producto) {
        SupabaseClient.client
            .from("productos")
            .update(producto) {
                filter { eq("id", producto.id!!) }
            }
    }

    // ── ELIMINAR (hard delete) ────────────────────────────────────────────────
    suspend fun eliminarProducto(id: Long) {
        SupabaseClient.client
            .from("productos")
            .delete {
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