package br.com.alexsander.leitor.repository

import br.com.alexsander.leitor.data.Code
import br.com.alexsander.leitor.data.CodeDAO
import kotlinx.coroutines.flow.Flow

class CodeRepositoryImpl(private val codeDAO: CodeDAO) : CodeRepository {
    override val codes: Flow<List<Code>> = codeDAO.getAll()

    override suspend fun insert(code: Code) {
        codeDAO.insert(code)
    }

    override suspend fun delete(code: Code) {
        codeDAO.delete(code)
    }
}