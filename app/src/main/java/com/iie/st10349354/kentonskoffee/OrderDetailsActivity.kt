package com.iie.st10349354.kentonskoffee

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.iie.st10349354.kentonskoffee.databinding.ActivityOrderDetailsBinding

class OrderDetailsActivity : AppCompatActivity() {
    var order = Order()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_order_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the order from SharedPreferences
        val orderFromPrefs = JsonUtils.getOrderFromPreferences(this)
        //orderFromPrefs?.let { order = it }

        // Set the product name on the text view
        binding.tvPlacedOrder.text = order.productName

        when(order.productName) {
            "Soy Latte" -> binding.imgOrderedBeverage.setImageResource(R.drawable.sb1)
            "Chocco Frappe" -> binding.imgOrderedBeverage.setImageResource(R.drawable.sb2)
            "Caramel Frappe" -> binding.imgOrderedBeverage.setImageResource(R.drawable.sb3)
        }

        binding.fabOrder.setOnClickListener(){
            shareIntent(applicationContext, order.productName)
        }
    }
}