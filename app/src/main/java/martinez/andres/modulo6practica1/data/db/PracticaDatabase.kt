package martinez.andres.modulo6practica1.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import martinez.andres.modulo6practica1.data.db.model.TransactionEntity
import martinez.andres.modulo6practica1.util.Constants

@Database(
    entities = [TransactionEntity::class],
    version = 1,
    exportSchema = true
)
abstract class PracticaDatabase : RoomDatabase() {
    abstract fun practicaDao(): PracticaDao

    companion object {
        @Volatile
        private var INSTANCE: PracticaDatabase? = null

        fun getDatabase(context: Context): PracticaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PracticaDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}