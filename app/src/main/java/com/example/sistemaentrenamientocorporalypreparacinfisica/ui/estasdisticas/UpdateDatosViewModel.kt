package com.example.sistemaentrenamientocorporalypreparacinfisica.ui.estasdisticas

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sistemaentrenamientocorporalypreparacinfisica.SupabaseInstance
import com.example.sistemaentrenamientocorporalypreparacinfisica.UsuarioAvances
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

// DTOs serializables para evitar el error con 'Any'
@Serializable
data class InsertAvancesDto(
    val idUser: String,
    val peso: Double,
    val altura: Double,
    val pesoGrasa: Double?,
    val pesoMusculo: Double?
)

@Serializable
data class UpdateAvancesDto(
    val peso: Double,
    val altura: Double,
    val pesoGrasa: Double?,
    val pesoMusculo: Double?
)

class UpdateDatosViewModel(
    private val idUser: String,
    private val context: Context
) : ViewModel() {

    private val _usuarioAvances = MutableLiveData<UsuarioAvances?>()
    val usuarioAvances: LiveData<UsuarioAvances?> = _usuarioAvances

    private val _datosActualizados = MutableLiveData<Boolean>()
    val datosActualizados: LiveData<Boolean> = _datosActualizados

    private val TAG = "UpdateDatosViewModel"

    init {
        getDataUsuarioAvances()
    }

    private fun getDataUsuarioAvances() {
        viewModelScope.launch {
            try {
                val response = SupabaseInstance.client.postgrest
                    .from("usuarioavances")
                    .select(Columns.ALL) {
                        filter { eq("idUser", idUser) }
                        order("fechaDatos", Order.DESCENDING)
                    }
                    .decodeList<UsuarioAvances>()

                _usuarioAvances.value = response.firstOrNull()
            } catch (e: Exception) {
                Log.e(TAG, "Error al obtener último registro: ${e.message}", e)
                _usuarioAvances.value = null
            }
        }
    }

    fun insertDataUsuariosAvances(
        idUsuarioAvances: Int?,
        peso: Double,
        altura: Double,
        pesoGrasa: Double?,
        pesoMusculo: Double?,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (idUsuarioAvances != null && idUsuarioAvances > 0) {
                    val updateBody = UpdateAvancesDto(
                        peso = peso,
                        altura = altura,
                        pesoGrasa = pesoGrasa,
                        pesoMusculo = pesoMusculo
                    )

                    SupabaseInstance.client.postgrest
                        .from("usuarioavances")
                        .update(updateBody) {
                            filter { eq("idUserAvances", idUsuarioAvances) }
                        }
                } else {
                    val insertBody = InsertAvancesDto(
                        idUser = idUser,
                        peso = peso,
                        altura = altura,
                        pesoGrasa = pesoGrasa,
                        pesoMusculo = pesoMusculo
                    )

                    SupabaseInstance.client.postgrest
                        .from("usuarioavances")
                        .insert(insertBody)
                }

                _datosActualizados.value = true
                onResult(true)

            } catch (e: Exception) {
                Log.e(TAG, "Error al guardar/actualizar datos: ${e.message}", e)
                onResult(false)
            }
        }
    }
}