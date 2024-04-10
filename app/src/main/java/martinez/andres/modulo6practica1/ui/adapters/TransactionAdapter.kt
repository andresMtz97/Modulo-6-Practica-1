package martinez.andres.modulo6practica1.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import martinez.andres.modulo6practica1.data.db.model.TransactionEntity

class TransactionAdapter(
    private val transactions: List<TransactionEntity>
): Adapter<TransactionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}