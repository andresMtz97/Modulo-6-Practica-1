package martinez.andres.modulo6practica1.data

import martinez.andres.modulo6practica1.data.db.PracticaDao
import martinez.andres.modulo6practica1.data.db.model.TransactionEntity

class PracticaRepository(private val practicaDao: PracticaDao) {
    suspend fun insertTransaction(transaction: TransactionEntity) {
        practicaDao.insertTransaction(transaction)
    }

    suspend fun getTransactions(): List<TransactionEntity> {
        return practicaDao.getTransactions()
    }

    suspend fun updateTransaction(transaction: TransactionEntity) {
        practicaDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        practicaDao.deleteTransaction(transaction)
    }
}