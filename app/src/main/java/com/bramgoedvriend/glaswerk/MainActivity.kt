package com.bramgoedvriend.glaswerk

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bramgoedvriend.glaswerk.network.GlaswerkAPIService
import com.bramgoedvriend.glaswerk.login.LoginDataSource
import com.bramgoedvriend.glaswerk.login.LoginRepository

class MainActivity : AppCompatActivity() {

    internal lateinit var jsonAPI: GlaswerkAPIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar?.setDisplayHomeAsUpEnabled(false)

        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.DamageFragment,
                R.id.OrdersFragment,
                R.id.stockFragment,
                R.id.studentsFragment,
                R.id.settingsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true;
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    fun logout() {
        LoginRepository(LoginDataSource(), this).logout()
    }

}
