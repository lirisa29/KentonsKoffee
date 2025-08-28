package com.iie.st10349354.kentonskoffee

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.navigation.NavigationView
import com.iie.st10349354.kentonskoffee.databinding.ActivityMainWithNavDrawerBinding

class MainActivity : AppCompatActivity(), View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    var order = Order()
    private lateinit var binding: ActivityMainWithNavDrawerBinding
    private lateinit var toggleOnOff: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainWithNavDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }

    override fun onClick(v: View?){
        when(v?.id){
            R.id.img_sb1 -> order.productName = "Soy Latte"
            R.id.img_sb2 -> order.productName = "Chocco Frappe"
            R.id.img_sb3 -> order.productName = "Caramel Frappe"
        }
        Toast.makeText(this@MainActivity, "MMM " + order.productName, Toast.LENGTH_SHORT).show()
        openIntent(applicationContext, order.productName, OrderDetailsActivity::class.java)
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