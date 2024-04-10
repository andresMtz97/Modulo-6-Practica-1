package martinez.andres.modulo6practica1.application

import android.app.Application
import martinez.andres.modulo6practica1.data.PracticaRepository
import martinez.andres.modulo6practica1.data.db.PracticaDatabase

class PracticaApp : Application() {
    private val database by lazy { PracticaDatabase.getDatabase(this) }

    val repository by lazy { PracticaRepository(database.practicaDao()) }
}