package martinez.andres.modulo6practica1.ui.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import martinez.andres.modulo6practica1.data.db.model.TransactionEntity
import martinez.andres.modulo6practica1.databinding.TransactionElementBinding

class TransactionViewHolder(view: View): ViewHolder(view) {

    private val binding = TransactionElementBinding.bind(view)

    fun render(transaction: TransactionEntity) {

    }
}