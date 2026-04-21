package com.app.granineaapp.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.granineaapp.R
import com.app.granineaapp.ui.main.carrito.CarritoFragment
import com.app.granineaapp.ui.main.perfil.EditarPerfilFragment
import com.app.granineaapp.ui.main.perfil.PerfilFragment
import com.app.granineaapp.ui.main.productos.CatalogoFragment
import com.app.granineaapp.ui.inicio.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout : androidx.drawerlayout.widget.DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Estado inicial
        if (savedInstanceState == null) {
            cargarFragment(HomeFragment())
            bottomNav.selectedItemId = R.id.inicio
        }

        // Navegación BottomNavigationView
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.inicio -> cargarFragment(HomeFragment())
                R.id.catalogoProductos -> cargarFragment(CatalogoFragment())
                R.id.carritoCompras -> cargarFragment(CarritoFragment())
                R.id.miPerfil -> cargarFragment(EditarPerfilFragment())
            }
            true
        }

        // Navegación NavigationView (Drawer)
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.inicio -> cargarFragment(HomeFragment())
                R.id.catalogoProductos -> cargarFragment(CatalogoFragment())
                R.id.carritoCompras -> cargarFragment(CarritoFragment())
                R.id.miPerfil -> cargarFragment(EditarPerfilFragment())

            }
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun cargarFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}