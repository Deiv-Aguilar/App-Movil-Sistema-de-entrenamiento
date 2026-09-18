package com.example.sistemaentrenamientocorporalypreparacinfisica

import kotlinx.serialization.Serializable

@Serializable
data class User1(
    val idUser: Int? = null,        // puede ser null al insertar
    val correo: String,
    val metodoLogue: String,
    val loginSucces: String,
    val nombre: String,
    val apellido: String,
    val fechaNacimiento: String,
    val telefono: Long,
    val sexo: String,
    val experenciaPrevia: String? = null
)

@Serializable
data class UsuarioAvances(
    val idUserAvances: Int? = null,
    val idUser: Int,
    val fechaDatos: String? = null,  // yyyy-MM-dd / timestamptz (permite null de forma segura)
    val peso: Double,
    val altura: Double,
    val pesoGrasa: Double? = null,
    val pesoMusculo: Double? = null
)

@Serializable
data class UsuarioAvancesInsert(
    val idUser: Int,
    val fechaDatos: String? = null,  // si es null, Supabase asigna now() por defecto
    val peso: Double,
    val altura: Double,
    val pesoGrasa: Double? = null,
    val pesoMusculo: Double? = null
)

@Serializable
data class UsuarioObjetivosInsert(
    val idUser: Int,
    val idParteTrabajo: Int
)

@Serializable
data class Calorias(
    val idCalorias: Int? = null,
    val idUser: Int,
    val fechaDatos: String? = null,
    val calorias: Int
)

@Serializable
data class Alimentos(
    val idAlimento: Int,
    val nombre: String,
    val descripcion: String,
    val imagen: String,
    val calorias: String,
    val tipoComida: String
)

@Serializable
data class Colacion(
    val idColacion: Int,
    val nombre: String,
    val descripcion: String,
    val imagen: String,
    val calorias: String? = null,
    val tipoColacion: String
)

@Serializable
data class ParteTrabajoCuerpo(
    val idParteTrabajo: Int,
    val parteCuerpo: String,
    var isChecked: Boolean = false
)

