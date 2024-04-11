package martinez.andres.modulo6practica1.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import martinez.andres.modulo6practica1.R
import martinez.andres.modulo6practica1.data.db.model.TransactionEntity

class TransactionAdapter(
    private var transactions: List<TransactionEntity>
): Adapter<TransactionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TransactionViewHolder(layoutInflater.inflate(R.layout.transaction_element, parent, false))
    }

    override fun getItemCount(): Int = transactions.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.render(transactions[position])
    }

    fun updateList(list: List<TransactionEntity>) {
        transactions = list
        notifyDataSetChanged()
    }
}