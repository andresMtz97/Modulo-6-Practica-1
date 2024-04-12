package martinez.andres.modulo6practica1.ui

import android.app.Dialog
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
import martinez.andres.modulo6practica1.util.toast

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
        transactionAdapter =
            TransactionAdapter(transactions) { transaction -> showEditDialog(transaction) }
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
            val dialog = buildEditTransactionDialog()
            dialog.show(supportFragmentManager, "dialogAdd")
        }
    }

    private fun showEditDialog(transaction: TransactionEntity) {
        val dialog = buildEditTransactionDialog(true, transaction)
        dialog.show(supportFragmentManager, "dialogUpdate")
    }

    private fun buildEditTransactionDialog(
        edit: Boolean = false,
        transaction: TransactionEntity = TransactionEntity(
            amount = 0.0,
            date = "",
            description = "",
            account = ""
        )
    ): EditTransactionDialog = EditTransactionDialog(
        edit,
        transaction,
        { updateRV() },
        { text, bgColor -> sbMessage(binding.root, text, bgColor) }
    )
}