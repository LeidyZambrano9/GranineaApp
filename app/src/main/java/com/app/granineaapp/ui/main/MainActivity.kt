package com.app.granineaapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.app.granineaapp.R
import com.app.granineaapp.ui.main.carrito.CarritoFragment
import com.app.granineaapp.ui.main.perfil.EditarPerfilFragment
import com.app.granineaapp.ui.main.productos.CatalogoFragment
import com.app.granineaapp.ui.inicio.HomeFragment
import com.app.granineaapp.data.UsuarioRepository
// Importa tus fragments de admin
import com.app.granineaapp.ui.main.admin.pedidos.ListaPedidosFragment
import com.app.granineaapp.ui.main.admin.AdminHomeFragment
import com.app.granineaapp.ui.main.admin.usuarios.ListaUsuariosFragment
import com.app.granineaapp.ui.main.admin.productos.CatalogoAdminFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        bottomNav = findViewById(R.id.bottom_nav)
        navView = findViewById(R.id.nav_view)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Listeners de navegación
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.inicio -> cargarFragment(HomeFragment())
                R.id.catalogoProductos -> cargarFragment(CatalogoFragment())
                R.id.carritoCompras -> cargarFragment(CarritoFragment())
                R.id.miPerfil -> cargarFragment(EditarPerfilFragment())
            }
            true
        }

        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.inicio -> cargarFragment(HomeFragment())
                R.id.catalogoProductos -> cargarFragment(CatalogoFragment())
                R.id.carritoCompras -> cargarFragment(CarritoFragment())
                R.id.miPerfil -> cargarFragment(EditarPerfilFragment())
                R.id.nav_admin_home -> cargarFragment(AdminHomeFragment())
                R.id.nav_pedidos -> cargarFragment(ListaPedidosFragment())
                R.id.nav_productos -> cargarFragment(CatalogoAdminFragment())
                R.id.nav_usuarios -> cargarFragment(ListaUsuariosFragment())
            }
            drawerLayout.closeDrawers()
            true
        }

        // ✅ El estado inicial va DENTRO del coroutine, después de conocer el rol
        configurarPorRol(savedInstanceState)
    }

    private fun configurarPorRol(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            val rol = UsuarioRepository.obtenerRolActual()
            android.util.Log.d("DEBUG_ROL", "Rol obtenido: $rol")

            runOnUiThread {
                val esAdmin = rol == "admin"
                val menu = navView.menu

                // Bottom Nav
                bottomNav.visibility = if (esAdmin) View.GONE else View.VISIBLE

                // Drawer: opciones cliente
                menu.findItem(R.id.inicio).isVisible = !esAdmin
                menu.findItem(R.id.catalogoProductos).isVisible = !esAdmin
                menu.findItem(R.id.carritoCompras).isVisible = !esAdmin
                menu.findItem(R.id.miPerfil).isVisible = !esAdmin

                // Drawer: opciones admin
                menu.findItem(R.id.nav_admin_home).isVisible = esAdmin
                menu.findItem(R.id.nav_pedidos).isVisible = esAdmin
                menu.findItem(R.id.nav_productos).isVisible = esAdmin
                menu.findItem(R.id.nav_usuarios).isVisible = esAdmin

                // ✅ Estado inicial según rol, solo la primera vez
                if (savedInstanceState == null) {
                    if (esAdmin) {
                        cargarFragment(AdminHomeFragment())
                    } else {
                        cargarFragment(HomeFragment())
                        bottomNav.selectedItemId = R.id.inicio
                    }
                }
            }
        }
    }
    private fun configurarPorRol() {
        lifecycleScope.launch {
            val rol = UsuarioRepository.obtenerRolActual()
            android.util.Log.d("DEBUG_ROL", "Rol obtenido: $rol")

            runOnUiThread {
                val esAdmin = rol == "admin"
                val menu = navView.menu

                // --- Bottom Nav ---
                // Admin no necesita el bottom nav
                bottomNav.visibility = if (esAdmin) View.GONE else View.VISIBLE

                // --- Drawer: opciones cliente ---
                menu.findItem(R.id.inicio).isVisible = !esAdmin
                menu.findItem(R.id.catalogoProductos).isVisible = !esAdmin
                menu.findItem(R.id.carritoCompras).isVisible = !esAdmin
                menu.findItem(R.id.miPerfil).isVisible = !esAdmin

                // --- Drawer: opciones admin ---
                menu.findItem(R.id.nav_admin_home).isVisible = esAdmin
                menu.findItem(R.id.nav_pedidos).isVisible = esAdmin
                menu.findItem(R.id.nav_productos).isVisible = esAdmin
                menu.findItem(R.id.nav_usuarios).isVisible = esAdmin

                // Cargar fragment inicial según rol
                if (esAdmin) {
                    cargarFragment(HomeFragment())
                } else {
                    cargarFragment(HomeFragment())
                    bottomNav.selectedItemId = R.id.inicio
                }
            }
        }
    }

    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}