package br.com.fiap.projetofinal.models

import com.google.firebase.firestore.Exclude

data class Product(

    @get:Exclude var id: String? = null,
    val name: String? = null,
)