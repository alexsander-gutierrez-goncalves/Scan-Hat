package br.com.alexsander.leitor.repository

import br.com.alexsander.leitor.data.Code
import kotlinx.coroutines.flow.Flow

interface CodeRepository {
    val codes: Flow<List<Code>>
    suspend fun insert(code: Code)
    suspend fun delete(code: Code)
}