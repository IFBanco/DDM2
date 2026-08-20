package com.example.cadastros

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.cadastros.data.local.AppDatabase
import com.example.cadastros.data.local.entity.Produto
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CadastroProdutoScreen()
                }
            }
        }
    }
}

@Composable
fun CadastroProdutoScreen() {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val produtoDao = database.produtoDao()
    val coroutineScope = rememberCoroutineScope()

    val listaProdutos by produtoDao.listarTodos().collectAsState(initial = emptyList())

    var nome by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Cadastro de Produto (PoC)", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome do Produto") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = preco,
            onValueChange = { preco = it },
            label = { Text("Preço (ex: 99.90)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val precoDouble = preco.toDoubleOrNull() ?: 0.0

                if (nome.isNotBlank()) {
                    coroutineScope.launch {
                        produtoDao.inserirProduto(
                            Produto(
                                nome = nome,
                                preco = precoDouble,
                                descricao = descricao
                            )
                        )
                        Toast.makeText(context, "Salvo no SQLite!", Toast.LENGTH_SHORT).show()

                        nome = ""
                        preco = ""
                        descricao = ""
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Dados em Cache:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(listaProdutos) { produto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "ID: ${produto.id}", style = MaterialTheme.typography.labelSmall)
                        Text(text = "Nome: ${produto.nome}", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Preço: R$ ${produto.preco}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Desc: ${produto.descricao}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}