package martinez.andres.modulo6practica1.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import martinez.andres.modulo6practica1.application.PracticaApp
import martinez.andres.modulo6practica1.data.PracticaRepository
import martinez.andres.modulo6practica1.data.db.model.TransactionEntity
import martinez.andres.modulo6practica1.databinding.ActivityMainBinding
import martinez.andres.modulo6practica1.ui.adapters.TransactionAdapter
import martinez.andres.modulo6practica1.util.Constants
import martinez.andres.modulo6practica1.util.sbMessage

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var transactions: List<TransactionEntity> = emptyList()
    private lateinit var repository: PracticaRepository

    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = (application as PracticaApp).repository

        initUI()
    }

    private fun initUI() {
        initRV()
        initListeners()
    }

    private fun initRV() {
        transactionAdapter = TransactionAdapter(transactions)
        binding.rvTransactions.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        updateRV()
    }

    private fun updateRV() {
        lifecycleScope.launch {
            transactions = repository.getTransactions()
            binding.tvNoData.visibility =
                if (transactions.isEmpty()) View.VISIBLE else View.INVISIBLE
            transactionAdapter.updateList(transactions)
        }
    }

    private fun initListeners() {
        binding.fabAddTransaction.setOnClickListener {
            Log.i(Constants.LOG_TAG, "Presiono boton add")
            val dialog = EditTransactionDialog(
                updateRV = { updateRV() },
                message = { text, bgColor -> sbMessage(binding.root, text, bgColor) }
            )

            dialog.show(supportFragmentManager, "dialogAdd")
        }
    }
}