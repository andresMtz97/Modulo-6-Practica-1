package martinez.andres.modulo6practica1.ui

import android.app.AlertDialog.Builder
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
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
import martinez.andres.modulo6practica1.data.db.model.Account
import martinez.andres.modulo6practica1.data.db.model.Accounts
import martinez.andres.modulo6practica1.data.db.model.TransactionEntity
import martinez.andres.modulo6practica1.databinding.DialogEditTransactionBinding
import martinez.andres.modulo6practica1.ui.adapters.AccountArrayAdapter
import martinez.andres.modulo6practica1.util.Constants
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar

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
        repository = (requireContext().applicationContext as PracticaApp).repository
        builder = Builder(requireContext())

        setupAccountSpinner()

        binding.apply {
            tietAmount.setText(transaction.amount.toString())
            tietDescription.setText(transaction.description)
            tietDate.setText(transaction.date)
            tietDate.setOnClickListener { showDatePickerDialog() }
        }

        val btnText = if (edit) getString(R.string.update) else getString(R.string.save)
        dialog = buildDialog(btnText, getString(R.string.cancel)) {
            transaction.apply {
                amount = binding.tietAmount.text.toString().toDouble()
                description = binding.tietDescription.text.toString()
                date = binding.tietDate.text.toString()
                account = if (binding.accountSpinner.selectedItemPosition != 0)
                    (binding.accountSpinner.selectedItem as Account).name
                else
                    ""
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
        .setTitle(if (edit) getString(R.string.update_transaction) else getString(R.string.add_transaction))
        .setPositiveButton(positiveBtnText) { _, _ ->
            positiveButton()
        }
        .setNegativeButton(negativeBtnText) { _, _ -> }
        .create()

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment(binding.tietDate.text.toString()) { day, month, year ->
            onDateSelected(day, month, year)
        }
        datePicker.show(parentFragmentManager, "datePicker")
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, day)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        binding.tietDate.setText(dateFormat.format(calendar.time))
    }

    private fun setupAccountSpinner() {
        val spinnerAdapter = AccountArrayAdapter(requireContext(), Accounts.list!!)
        binding.accountSpinner.apply {
            adapter = spinnerAdapter
//            onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    val selectedItem = parent!!.getItemAtPosition(position) as Account
//                    transaction.account = selectedItem.name
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) { }
//            }
        }

    }
}