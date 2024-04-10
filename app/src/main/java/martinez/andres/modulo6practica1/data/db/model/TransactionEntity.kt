package martinez.andres.modulo6practica1.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import martinez.andres.modulo6practica1.util.Constants

@Entity(tableName = Constants.TRANSACTION_TABLE)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transactionId")
    val id: Long = 0,

    @ColumnInfo(name = "amount")
    var amount: Double,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "account")
    var account: String
)