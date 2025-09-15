package com.iie.st10349354.kentonskoffee

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.navigation.NavigationView
import com.iie.st10349354.kentonskoffee.databinding.ActivityMainWithNavDrawerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    var order = Order()

    private lateinit var binding: ActivityMainWithNavDrawerBinding
    private lateinit var toggleOnOff: ActionBarDrawerToggle
    private lateinit var orderDAO : OrderDAO // Declare DAO
    private lateinit var db: AppDatabase // Declare Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainWithNavDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialise Database and DAO
        db = AppDatabase.getDatabase(this) as AppDatabase
        orderDAO = db.orderDAO()

        // Window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up the Toolbar
        setSupportActionBar(binding.navToolbar)

        // Setup Navigation Drawer toggle
        toggleOnOff = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.navToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggleOnOff)
        toggleOnOff.syncState()

        // Ensure navView is clickable
        binding.navView.bringToFront()
        binding.navView.setNavigationItemSelectedListener(this)

        // Product selection listeners
        binding.imgSb1.setOnClickListener(this)
        binding.imgSb2.setOnClickListener(this)
        binding.imgSb3.setOnClickListener(this)

        insertProducts()
    }

    private fun insertProducts() {
        val products = listOf(
            OrderEntity(productName = "Soy Latte", customerName = "", customerCell = "", orderDate = ""),
            OrderEntity(productName = "Chocco Frappe", customerName = "", customerCell = "", orderDate = ""),
            OrderEntity(productName = "Caramel Frappe", customerName = "", customerCell = "", orderDate = "")
        )

        CoroutineScope(Dispatchers.IO).launch {
            products.forEach { orderDAO.insert(it)}
        }
    }

    override fun onClick(v: View?){
        when(v?.id){
            R.id.img_sb1 -> getProductFromDB("Soy Latte")
            R.id.img_sb2 -> getProductFromDB("Chocco Frappe")
            R.id.img_sb3 -> getProductFromDB("Caramel Frappe")
        }
        Toast.makeText(this@MainActivity, "MMM " + order.productName, Toast.LENGTH_SHORT).show()
        openIntent(applicationContext, order.productName, OrderDetailsActivity::class.java)
    }

    private fun getProductFromDB(productName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val product = orderDAO.getAllOrders().firstOrNull { it.productName == productName}
            product?.let {
                // Save to SharedPreferences as JSON
                JsonUtils.saveOrderToPreferences(this@MainActivity, it)

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity,
                        "MMM ${it.productName}", Toast.LENGTH_SHORT).show()
                    openIntent(applicationContext, it.productName, OrderDetailsActivity::class.java)
                }
            }
        }
    }

    override fun onBackPressed(){
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
        else{
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId){
            R.id.nav_photo -> openIntent(this, "", CoffeeSnapsActivity::class.java)
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggleOnOff.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}