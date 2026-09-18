package com.example.sistemaentrenamientocorporalypreparacinfisica

import android.content.Context
import com.russhwolf.settings.SharedPreferencesSettings
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SettingsCodeVerifierCache
import io.github.jan.supabase.gotrue.SettingsSessionManager
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseInstance {

    private var instance: SupabaseClient? = null

    fun getInstance(context: Context): SupabaseClient {
        return instance ?: synchronized(this) {
            instance ?: run {
                val sharedPreferences = context.getSharedPreferences("supabase_session", Context.MODE_PRIVATE)
                val settings = SharedPreferencesSettings(sharedPreferences)

                createSupabaseClient(
                    supabaseUrl = "https://agguxkrncdvkchsetlwq.supabase.co",
                    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFnZ3V4a3JuY2R2a2Noc2V0bHdxIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTczNDE2MjIsImV4cCI6MjA3MjkxNzYyMn0.7iLbx1dcHHwm7IA5uEuYEy5-7H9JVw4sPrSWPmuCSuk"
                ) {
                    install(Auth) {
                        sessionManager = SettingsSessionManager(settings)
                        codeVerifierCache = SettingsCodeVerifierCache(settings)
                    }
                    install(Postgrest)
                }.also { instance = it }
            }
        }
    }

    val client: SupabaseClient
        get() = instance ?: throw IllegalStateException("SupabaseInstance debe ser inicializado primero en MainApplication.")
}