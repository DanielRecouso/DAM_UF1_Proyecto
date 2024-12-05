package com.example.amiibo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            v.updatePadding(bottom = 0)
            insets
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_view) as NavHostFragment

        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()

//        TOOLBAR
        val toolBar = findViewById<MaterialToolbar>(R.id.toolBar)
        setSupportActionBar(toolBar)
        toolBar.setupWithNavController(navController, appBarConfiguration)
//        BOTTOMBAR
        val bottomBar = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomBar.setupWithNavController(navController)
    }


}
