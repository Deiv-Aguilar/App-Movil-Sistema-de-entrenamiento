package com.example.sistemaentrenamientocorporalypreparacinfisica

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sistemaentrenamientocorporalypreparacinfisica.databinding.ActivityMainBinding
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.DataSesion
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.PreferencesData
import com.example.sistemaentrenamientocorporalypreparacinfisica.model.UserData
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "USER_PREFERENCES_NAME")
lateinit var User: UserData
val urlServidor = "http://192.168.166.33/phpdocs/"
val urlServidorImagenes = "https://supabase.com/dashboard/project/agguxkrncdvkchsetlwq/storage/buckets/img"
var idUser: String? = null

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity", "Antes de installSplashScreen")
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate ejecutado, URL Servidor: $urlServidor")

        // Pantalla de carga splash screen
        Thread.sleep(500)
        screenSplash.setKeepOnScreenCondition { false }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("Supabase", "Supabase Iniciado desde Singleton")

        actionBar?.setDisplayShowTitleEnabled(true)
        actionBar?.setDisplayShowHomeEnabled(true)

        navView = binding.navView

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        delegate.applyDayNight()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return true
    }

    // Muestra los botones de la barra de navegación
    fun showBottomNav() {
        if (::navView.isInitialized) navView.visibility = View.VISIBLE
    }

    // Oculta los botones de la barra de navegación
    fun hideBottomNav() {
        if (::navView.isInitialized) navView.visibility = View.GONE
    }

    // Guarda datos en DataStore
    suspend fun saveValues(correo: String, loginSucces: Boolean, metodoLogueo: String, idUser: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("correo")] = correo
            preferences[stringPreferencesKey("metodoLogue")] = metodoLogueo
            preferences[booleanPreferencesKey("loginSucces")] = loginSucces
            preferences[stringPreferencesKey("idUser")] = idUser
        }
    }

    suspend fun deleteValues() {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey("correo"))
            preferences.remove(stringPreferencesKey("metodoLogue"))
            preferences.remove(booleanPreferencesKey("loginSucces"))
            preferences.remove(stringPreferencesKey("idUser"))
        }
    }

    suspend fun saveValuesPreferences(voz: String, estado: Boolean) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("voz")] = voz
            preferences[booleanPreferencesKey("habilitarvoz")] = estado
        }
    }

    suspend fun saveValuesPreferencesVoz(voz: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("voz")] = voz
        }
    }

    suspend fun saveValuesPreferencesEstado(estado: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey("habilitarvoz")] = estado
        }
    }

    suspend fun saveValueIdUser(idUser: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey("idUser")] = idUser
        }
    }

    // Permite acceder a los diferentes datos almacenados en el dataStore
    fun getUserProfile() = dataStore.data.map { preferences ->
        DataSesion(
            correo = preferences[stringPreferencesKey("correo")].orEmpty(),
            metodoLogue = preferences[stringPreferencesKey("metodoLogue")].orEmpty(),
            loginSucces = preferences[booleanPreferencesKey("loginSucces")] ?: false,
            idUser = preferences[stringPreferencesKey("idUser")].orEmpty()
        )
    }

    fun getPreferences() = dataStore.data.map {
        PreferencesData(
            voz = it[stringPreferencesKey("voz")].orEmpty(),
            habilitarVoz = it[booleanPreferencesKey("habilitarvoz")] ?: false
        )
    }

    companion object {
        fun getURL(): String {
            return urlServidor
        }

        fun geURLimage(): String {
            return urlServidorImagenes
        }

        // Método para obtener el valor almacenado de loginSucces
        suspend fun getLoginSucces(context: Context): Boolean {
            val dataStore = context.dataStore
            val loginSuccesKey = booleanPreferencesKey("loginSucces")
            val preferences = dataStore.data.first()

            return preferences[loginSuccesKey] ?: false
        }

        // Método para obtener el valor almacenado de idUser
        suspend fun getIdUser(context: Context): String? {
            val dataStore = context.dataStore
            val idUserKey = stringPreferencesKey("idUser")
            val preferences = dataStore.data.first()

            return preferences[idUserKey]
        }
    }
}