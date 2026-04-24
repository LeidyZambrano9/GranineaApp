package com.app.granineaapp.ui.main.admin

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.granineaapp.R
import com.app.granineaapp.adapter.PedidoAdapter
import com.app.granineaapp.data.FakeData
import com.app.granineaapp.model.Pedido
import com.app.granineaapp.ui.main.admin.pedidos.DetallePedidoActivity
import com.app.granineaapp.ui.main.admin.pedidos.ListaPedidosFragment
import com.app.granineaapp.ui.main.admin.usuarios.ListaUsuariosFragment
import com.google.android.material.navigation.NavigationView
import com.app.granineaapp.ui.main.admin.productos.CatalogoAdminFragment
import com.app.granineaapp.ui.main.perfil.EditarPerfilFragment

class AdminActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // ==================== VIEWS PRINCIPALES ====================
    private lateinit var tvUsuariosCount: TextView
    private lateinit var tvPedidosCount: TextView
    private lateinit var tvProductosCount: TextView
    private lateinit var rvPedidosPendientes: RecyclerView
    private lateinit var contenidoHome: View
    private lateinit var contenedorFragments: FrameLayout

    // ==================== NAVIGATION DRAWER ====================
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    // ==================== ADAPTER ====================
    private lateinit var pedidoAdapter: PedidoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // Configurar Toolbar como ActionBar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarAdmin)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Inicializar Views principales
        tvUsuariosCount = findViewById(R.id.tvAdminUsuariosCount)
        tvPedidosCount = findViewById(R.id.tvAdminPedidosCount)
        tvProductosCount = findViewById(R.id.tvAdminProductosCount)
        rvPedidosPendientes = findViewById(R.id.rvAdminPedidosPendientes)
        contenidoHome = findViewById(R.id.contenidoHome)
        contenedorFragments = findViewById(R.id.contenedorFragments)

        // Configurar Navigation Drawer
        configurarDrawer()


        configurarBotones()
        cargarInfoAdminEnHeader()
    }



    // ==================== CONFIGURACIÓN DE BOTONES PRINCIPALES ====================
    private fun configurarBotones() {
        findViewById<View>(R.id.btnAdminGestionarProductos).setOnClickListener {
            cargarFragment(CatalogoAdminFragment(), "Catálogo - Admin")  // ✅ Nuevo fragment admin
        }
        findViewById<View>(R.id.btnAdminGestionarPedidos).setOnClickListener {
            cargarFragment(ListaPedidosFragment(), "Pedidos")
        }
        findViewById<View>(R.id.btnAdminGestionarUsuarios).setOnClickListener {
            cargarFragment(ListaUsuariosFragment(), "Usuarios")
        }
        findViewById<View>(R.id.btnAdminMostrarTodos).setOnClickListener {
            cargarFragment(ListaPedidosFragment(), "Todos los pedidos")
        }
    }

    // ==================== CONFIGURACIÓN DEL NAVIGATION DRAWER ====================
    private fun configurarDrawer() {
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        // Toggle hamburguesa
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.nav_open,
            R.string.nav_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Listener del menú lateral
        navigationView.setNavigationItemSelectedListener(this)
    }

    // ==================== MÉTODO PARA CARGAR FRAGMENTS ====================
    private fun cargarFragment(fragment: Fragment, titulo: String) {
        // Ocultar contenido del home y mostrar contenedor de fragments
        contenidoHome.visibility = View.GONE
        contenedorFragments.visibility = View.VISIBLE

        // Cargar el fragment con transacción
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedorFragments, fragment)
            .addToBackStack(null)
            .commit()

        // Actualizar título del toolbar
        supportActionBar?.title = titulo
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    // ==================== MÉTODO PARA VOLVER AL HOME ====================
    private fun volverAlHome() {
        // Remover todos los fragments del backstack
        supportFragmentManager.popBackStack(null, androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)

        // Mostrar contenido del home y ocultar contenedor de fragments
        contenedorFragments.visibility = View.GONE
        contenidoHome.visibility = View.VISIBLE

        // Restaurar título y recargar datos
        supportActionBar?.title = "Gestionar"

    }

    // ==================== NAVEGACIÓN DEL MENÚ LATERAL ====================
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                volverAlHome()
                drawerLayout.closeDrawer(GravityCompat.START)  //
            }
            R.id.nav_pedidos -> {
                cargarFragment(ListaPedidosFragment(), "Pedidos")
            }
            R.id.nav_productos -> {
                cargarFragment(CatalogoAdminFragment(), "Catálogo - Admin")  // ✅ Nuevo fragment admin
            }
            R.id.nav_usuarios -> {
                cargarFragment(ListaUsuariosFragment(), "Usuarios")
            }
            R.id.nav_perfil -> {
                cargarFragment(EditarPerfilFragment(), "Mi Perfil")
            }
        }
        return true
    }

    // ==================== DETALLE DE PEDIDO (Activity separada) ====================
    private fun abrirDetallePedido(pedido: Pedido) {
        try {
            val intent = android.content.Intent(this, DetallePedidoActivity::class.java)
            intent.putExtra("pedido_id", pedido.id)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al abrir detalle: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // ==================== MARCAR PEDIDO COMO ENTREGADO ====================


    // ==================== CARGAR INFO DEL ADMIN EN EL HEADER ====================
    private fun cargarInfoAdminEnHeader() {
        val headerView = navigationView.getHeaderView(0)
        val tvAdminName = headerView.findViewById<TextView>(R.id.tvAdminName)
        val tvAdminEmail = headerView.findViewById<TextView>(R.id.tvAdminEmail)

        tvAdminName.text = "Administrador"
        tvAdminEmail.text = "admin@graninea.com"
    }

    // ==================== MANEJO DEL BOTÓN DE ATRÁS Y HAMBURGUESA ====================
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}