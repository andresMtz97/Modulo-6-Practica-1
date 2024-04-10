package martinez.andres.modulo6practica1.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import martinez.andres.modulo6practica1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        initListeners()
    }

    private fun initListeners() {
        binding.fabAddTransaction.setOnClickListener {

        }
    }
}