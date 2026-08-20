package com.example.cadastros.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.example.cadastros.data.local.entity.Produto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {
    @Insert
    suspend fun inserirProduto(produto: Produto)

    @Query("SELECT * FROM produtos")
    fun listarTodos(): Flow<List<Produto>>
}