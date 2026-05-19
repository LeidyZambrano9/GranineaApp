package com.app.granineaapp.ui.main.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.app.granineaapp.R
import com.app.granineaapp.data.ProductoRepository
import com.app.granineaapp.data.UsuarioRepository
import com.app.granineaapp.ui.main.admin.pedidos.ListaPedidosFragment
import com.app.granineaapp.ui.main.admin.productos.CatalogoAdminFragment
import com.app.granineaapp.ui.main.admin.usuarios.ListaUsuariosFragment
import kotlinx.coroutines.launch

class AdminHomeFragment : Fragment() {

    private lateinit var tvUsuariosCount: TextView
    private lateinit var tvPedidosCount: TextView
    private lateinit var tvProductosCount: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_admin_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvUsuariosCount  = view.findViewById(R.id.tvAdminUsuariosCount)
        tvPedidosCount   = view.findViewById(R.id.tvAdminPedidosCount)
        tvProductosCount = view.findViewById(R.id.tvAdminProductosCount)

        cargarConteos()

        view.findViewById<Button>(R.id.btnAdminGestionarProductos).setOnClickListener {
            cargarFragment(CatalogoAdminFragment())
        }
        view.findViewById<Button>(R.id.btnAdminGestionarPedidos).setOnClickListener {
            cargarFragment(ListaPedidosFragment())
        }
        view.findViewById<Button>(R.id.btnAdminGestionarUsuarios).setOnClickListener {
            cargarFragment(ListaUsuariosFragment())
        }
        view.findViewById<Button>(R.id.btnAdminMostrarTodos).setOnClickListener {
            cargarFragment(ListaPedidosFragment())
        }
    }

    // Se refresca cada vez que el admin vuelve a este fragment (ej. después de crear un producto)
    override fun onResume() {
        super.onResume()
        cargarConteos()
    }

    // ── CONTEOS ───────────────────────────────────────────────────────────────
    private fun cargarConteos() {
        tvUsuariosCount.text  = "..."
        tvProductosCount.text = "..."
        tvPedidosCount.text   = "..."

        lifecycleScope.launch {
            // Usuarios
            try {
                val total = UsuarioRepository.obtenerTodosLosUsuarios().size
                tvUsuariosCount.text = total.toString()
            } catch (e: Exception) {
                tvUsuariosCount.text = "-"
            }
        }

        lifecycleScope.launch {
            // Productos (todos, activos e inactivos)
            try {
                val total = ProductoRepository.obtenerTodosLosProductos().size
                tvProductosCount.text = total.toString()
            } catch (e: Exception) {
                tvProductosCount.text = "-"
            }
        }

        // Si tienes PedidoRepository descomenta esto y ajusta el nombre de función:
        /*
        lifecycleScope.launch {
            try {
                val total = PedidoRepository.obtenerTodosPedidos().size
                tvPedidosCount.text = total.toString()
            } catch (e: Exception) {
                tvPedidosCount.text = "-"
            }
        }
        */
    }

    // ── NAVEGACIÓN ────────────────────────────────────────────────────────────
    private fun cargarFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}