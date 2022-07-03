package br.com.fiap.projetofinal.models

import com.google.firebase.firestore.Exclude

data class User(
    val username: String? = null,
    val email: String? = null,
    val phone: String? = null,
    @get:Exclude val password: String? = null
)