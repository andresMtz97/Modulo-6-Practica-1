package martinez.andres.modulo6practica1.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import martinez.andres.modulo6practica1.R
import martinez.andres.modulo6practica1.data.db.model.Account
import martinez.andres.modulo6practica1.databinding.AccountSpinnerItemBinding

class AccountArrayAdapter(context: Context, accountList: List<Account>) :
    ArrayAdapter<Account>(context, 0, accountList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val account = getItem(position)

        val binding = AccountSpinnerItemBinding.inflate(LayoutInflater.from(context), parent, false)

        binding.ivAccount.setImageResource(account!!.image)
        binding.tvAccount.text = account.name

        return binding.root
    }
}