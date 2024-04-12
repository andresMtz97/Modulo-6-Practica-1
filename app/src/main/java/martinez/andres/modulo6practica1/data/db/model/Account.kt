package martinez.andres.modulo6practica1.data.db.model

import martinez.andres.modulo6practica1.R

data class Account(val image: Int, val name: String)

object Accounts {

    val images = arrayOf(
        R.drawable.ic_bank,
        R.drawable.bbva,
        R.drawable.santander,
        R.drawable.hsbc,
        R.drawable.banorte,
        R.drawable.scotiabank,
        R.drawable.inbursa,
        R.drawable.banregio
    )

    val accounts = arrayOf(
        "Selecciona una cuenta de banco",
        "BBVA",
        "Santander",
        "HSBC",
        "Banorte",
        "Scotiabank",
        "Inbursa",
        "Banregio"
    )

    var list: ArrayList<Account>? = null
        get() {
            if (field != null) return field

            field = ArrayList()
            for (i in images.indices) {
                val imageId = images[i]
                val accountName = accounts[i]

                val account = Account(imageId, accountName)
                field!!.add(account)
            }
            return field
        }
}
