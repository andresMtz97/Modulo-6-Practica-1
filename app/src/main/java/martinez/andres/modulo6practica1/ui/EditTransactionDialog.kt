package martinez.andres.modulo6practica1.ui

import android.app.AlertDialog.Builder
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import martinez.andres.modulo6practica1.R
import martinez.andres.modulo6practica1.application.PracticaApp
import martinez.andres.modulo6practica1.data.PracticaRepository
import martinez.andres.modulo6practica1.data.db.model.TransactionEntity
import martinez.andres.modulo6practica1.databinding.DialogEditTransactionBinding
import martinez.andres.modulo6practica1.util.Constants
import java.io.IOException

class EditTransactionDialog(
    private var edit: Boolean = false,
    private var transaction: TransactionEntity = TransactionEntity(
        amount = 0.0,
        date = "",
        description = "",
        account = ""
    ),
    private val updateRV: () -> Unit,
    private val message: (String, Int) -> Unit
) : DialogFragment() {

    private var _binding: DialogEditTransactionBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: Builder
    private lateinit var dialog: Dialog

    private lateinit var repository: PracticaRepository

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogEditTransactionBinding.inflate(requireActivity().layoutInflater)

        builder = Builder(requireContext())

        repository = (requireContext().applicationContext as PracticaApp).repository

        val btnText = if (edit) getString(R.string.update) else getString(R.string.save)
        dialog = buildDialog(btnText, getString(R.string.cancel)) {
            transaction.apply {
                amount = binding.tietAmount.text.toString().toDouble()
                description = binding.tietDescription.text.toString()
            }

            var errorMessage = ""
            var successMessage: String
            try {
                lifecycleScope.launch(Dispatchers.IO) {

                    val result =
                        if (edit) {
                            errorMessage =
                                getString(R.string.update_error, getString(R.string.transaction))
                            successMessage =
                                getString(R.string.update_success, getString(R.string.transaction))
                            async { repository.updateTransaction(transaction) }
                        } else {
                            errorMessage =
                                getString(R.string.add_error, getString(R.string.transaction))
                            successMessage =
                                getString(R.string.add_success, getString(R.string.transaction))
                            async { repository.insertTransaction(transaction) }
                        }
                    result.await()

                    withContext(Dispatchers.Main) {
                        message(
                            successMessage,
                            ContextCompat.getColor(requireContext(), R.color.success)
                        )
                        updateRV()
                    }
                }
            } catch (e: IOException) {
                Log.e(Constants.LOG_TAG, e.message.toString())
                message(errorMessage, ContextCompat.getColor(requireContext(), R.color.error))
            }
        }

        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun buildDialog(
        positiveBtnText: String,
        negativeBtnText: String,
        positiveButton: () -> Unit,
    ): Dialog = builder.setView(binding.root)
        .setTitle(getString(R.string.transaction))
        .setPositiveButton(positiveBtnText) { _, _ ->
            positiveButton()
        }
        .setNegativeButton(negativeBtnText) { _, _ -> }
        .create()

}