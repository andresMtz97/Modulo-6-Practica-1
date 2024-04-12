package martinez.andres.modulo6practica1.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import martinez.andres.modulo6practica1.data.db.model.Accounts
import martinez.andres.modulo6practica1.data.db.model.TransactionEntity
import martinez.andres.modulo6practica1.databinding.TransactionElementBinding

class TransactionViewHolder(view: View) : ViewHolder(view) {

    private val binding = TransactionElementBinding.bind(view)

    fun render(
        transaction: TransactionEntity,
        onItemClicked: (TransactionEntity) -> Unit,
        onItemDelete: (TransactionEntity) -> Unit
    ) {
        binding.tvAmount.text = transaction.amount.toString()
        binding.tvDescription.text = transaction.description
        binding.tvDate.text = transaction.date

        val accountImageIndex = Accounts.accounts.indexOf(transaction.account)
        binding.ivAccount.setImageResource(if (accountImageIndex == -1) Accounts.images[0] else Accounts.images[accountImageIndex])

        itemView.setOnClickListener {
            onItemClicked(transaction)
        }

        binding.ivDelete.setOnClickListener {
            onItemDelete(transaction)
        }
    }
}