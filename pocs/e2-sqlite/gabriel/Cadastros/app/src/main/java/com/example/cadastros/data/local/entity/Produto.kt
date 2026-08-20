package com.example.cadastros.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "produtos")
data class Produto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val descricao: String,
    val preco: Double
)