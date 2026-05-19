package com.app.granineaapp

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {

    val client = createSupabaseClient(
        // URL corregida: solo la raíz del proyecto de Supabase
        supabaseUrl = "https://cugmrfnyaoglbdfstrjg.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN1Z21yZm55YW9nbGJkZnN0cmpnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Nzg4NjcyOTIsImV4cCI6MjA5NDQ0MzI5Mn0.AS-OTHoaPX59HH6Z9o8lzD_B16SVgptCVd_GY2pbCAQ"
    ) {
        install(Auth)
        install(Postgrest)
        install(Storage)          // ← necesario para subir imágenes al bucket
    }
}