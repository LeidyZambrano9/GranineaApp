package com.app.granineaapp.data

import com.app.granineaapp.SupabaseClient
import com.app.granineaapp.model.Usuario
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.Serializable
import io.github.jan.supabase.storage.storage
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

object UsuarioRepository {

    @Serializable
    data class UsuarioData(
        val id: String,
        val created_at: String? = null,  // Agregado - timestamptz
        val nombres: String,
        val apellidos: String,
        val celular: String? = null, // ✅ AGREGADO: Para mapear el celular desde Supabase
        val correo: String? = null,
        val rol: String = "cliente",
        val foto_url: String? = null
    )

    suspend fun existeUsuario(userId: String): Boolean {
        return try {
            val resultado = SupabaseClient.client
                .postgrest["Usuarios"]
                .select(Columns.raw("id")) {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeList<Map<String, String>>()
            resultado.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun obtenerUsuarioActual(): UsuarioData? {
        val userId = SupabaseClient.client.auth
            .currentUserOrNull()?.id ?: return null
        return try {
            val resultado = SupabaseClient.client
                .postgrest["Usuarios"]
                .select {
                    filter { eq("id", userId) }
                }
                .decodeList<UsuarioData>()

            android.util.Log.d("DEBUG_QUERY", "Resultado completo: $resultado")

            resultado.firstOrNull()
        } catch (e: Exception) {
            android.util.Log.e("DEBUG_QUERY", "Error: ${e.message}")
            null
        }
    }

    // Nota: Si usas esta función en el Registro antiguo, recuerda añadirle el parámetro celular si lo requieres allí.
    suspend fun insertarUsuario(id: String, nombres: String, apellidos: String, correo: String) {
        SupabaseClient.client.postgrest["Usuarios"].insert(
            UsuarioData(id = id, nombres = nombres, apellidos = apellidos, correo = correo)
        )
    }

    suspend fun obtenerRolActual(): String {
        return try {
            val userId = SupabaseClient.client.auth
                .currentUserOrNull()?.id ?: return "cliente"

            android.util.Log.d("DEBUG_ROL", "userId: $userId") // ← agrega esto

            val resultado = SupabaseClient.client
                .postgrest["Usuarios"]
                .select {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeList<UsuarioData>()

            android.util.Log.d("DEBUG_ROL", "resultado: $resultado") // ← y esto
            android.util.Log.d("DEBUG_ROL", "rol encontrado: ${resultado.firstOrNull()?.rol}") // ← y esto

            resultado.firstOrNull()?.rol ?: "cliente"

        } catch (e: Exception) {
            android.util.Log.e("DEBUG_ROL", "Exception: ${e.message}") // ← y esto
            "cliente"
        }
    }

    // Actualiza los datos del perfil en la tabla usuarios
    suspend fun actualizarPerfil(
        nombres: String,
        apellidos: String,
        celular: String? = null,  // Nuevo parámetro
        correo: String,
        fotoUrl: String? = null
    ) {
        val userId = SupabaseClient.client.auth
            .currentUserOrNull()?.id ?: return

        val datos = buildJsonObject {
            put("nombres", nombres)
            put("apellidos", apellidos)
            if (celular != null) put("celular", celular)  // Agregado
            put("correo", correo)
            if (fotoUrl != null) put("foto_url", fotoUrl)
        }

        SupabaseClient.client.postgrest["Usuarios"]
            .update(datos) {
                filter { eq("id", userId) }
            }
    }

    // Sube la foto al bucket avatars y devuelve la URL pública
    suspend fun subirFotoPerfil(
        contexto: android.content.Context,
        uri: android.net.Uri
    ): String {
        val userId = SupabaseClient.client.auth
            .currentUserOrNull()?.id ?: return ""

        android.util.Log.d("DEBUG_FOTO", "Uri scheme: ${uri.scheme}")
        android.util.Log.d("DEBUG_FOTO", "Uri path: ${uri.path}")

        val bytes = if (uri.scheme == "content") {
            contexto.contentResolver
                .openInputStream(uri)?.readBytes()
        } else {
            java.io.File(uri.path!!).readBytes()
        } ?: return ""

        android.util.Log.d("DEBUG_FOTO", "Bytes leídos: ${bytes.size}")

        val rutaArchivo = "perfil_$userId.jpg"

        SupabaseClient.client.storage["avatars"]
            .upload(
                path = rutaArchivo,
                data = bytes,
                options = { upsert = true }
            )

        val url = SupabaseClient.client.storage["avatars"]
            .publicUrl(rutaArchivo)

        android.util.Log.d("DEBUG_FOTO", "URL generada: $url")

        return url
    }
    suspend fun obtenerTodosLosUsuarios(): List<Usuario> {
        return try {
            SupabaseClient.client
                .postgrest["Usuarios"]
                .select()
                .decodeList<Usuario>()
        } catch (e: Exception) {
            android.util.Log.e("DEBUG_USUARIOS", "Error: ${e.message}")
            emptyList()
        }
    }
    suspend fun obtenerUsuarioPorId(id: String): UsuarioData? {
        return try {
            val resultado = SupabaseClient.client
                .postgrest["Usuarios"]
                .select {
                    filter { eq("id", id) }
                }
                .decodeList<UsuarioData>()
            resultado.firstOrNull()
        } catch (e: Exception) {
            android.util.Log.e("DEBUG_USUARIOS", "Error: ${e.message}")
            null
        }
    }

    suspend fun actualizarUsuarioPorAdmin(
        id: String,
        nombres: String,
        apellidos: String,
        correo: String,
        celular: String,
        rol: String
    ) {
        try {
            val datos = buildJsonObject {
                put("nombres", nombres)
                put("apellidos", apellidos)
                put("correo", correo)
                put("celular", celular)
                put("rol", rol)
            }
            SupabaseClient.client.postgrest["Usuarios"]
                .update(datos) {
                    filter { eq("id", id) }
                }
        } catch (e: Exception) {
            android.util.Log.e("DEBUG_USUARIOS", "Error al actualizar: ${e.message}")
        }
    }
}