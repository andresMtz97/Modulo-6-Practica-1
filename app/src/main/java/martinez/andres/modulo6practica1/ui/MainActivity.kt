package martinez.andres.modulo6practica1.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import martinez.andres.modulo6practica1.R
import martinez.andres.modulo6practica1.application.PracticaApp
import martinez.andres.modulo6practica1.data.PracticaRepository
import martinez.andres.modulo6practica1.data.db.model.TransactionEntity
import martinez.andres.modulo6practica1.databinding.ActivityMainBinding
import martinez.andres.modulo6practica1.ui.adapters.TransactionAdapter
import martinez.andres.modulo6practica1.util.sbMessage
import java.io.IOException

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
        updateRV()
    }

    private fun initRV() {
        transactionAdapter = TransactionAdapter(
            transactions,
            { transaction -> showEditDialog(transaction) },
            { transaction -> showDeleteDialog(transaction) }
        )
        binding.rvTransactions.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
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

    private fun showDeleteDialog(transaction: TransactionEntity) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_transaction)
            .setMessage(
                getString(
                    R.string.delete_message,
                    transaction.date,
                    transaction.account,
                    transaction.amount.toString()
                )
            )
            .setPositiveButton(R.string.accept) { _, _ ->
                try {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val result = async { repository.deleteTransaction(transaction) }
                        result.await()

                        withContext(Dispatchers.Main) {
                            sbMessage(
                                binding.root,
                                getString(R.string.delete_success, getString(R.string.transaction))
                            )
                            updateRV()
                        }
                    }
                } catch (e: IOException) {
                    sbMessage(
                        binding.root,
                        getString(R.string.delete_error, getString(R.string.transaction))
                    )
                }
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .create()
            .show()
    }
}