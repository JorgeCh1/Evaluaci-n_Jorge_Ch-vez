package com.example.listadoprod

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listadoprod.dataadapter.ProductoAdapter
import com.example.listadoprod.databinding.ActivityMainBinding
import com.example.listadoprod.dataclass.Producto

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var btnBorrar: Button
    var listaProd = ArrayList<Producto>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        iniciar()

    }

    private fun iniciar() {
        binding.btnAgregar.setOnClickListener {
            agregarProd()
        }
        binding.btnLimpiar.setOnClickListener {
            limpiar()
        }

    }

    private fun limpiar() {
        with(binding) {
            etID.setText("")
            etNombreProd.setText("")
            etPrecio.setText("")
            etID.requestFocus()
        }
    }

    private fun agregarProd() {
        with(binding) {
            try {
                val id: Int = etID.text.toString().toInt()
                val nombre: String = etNombreProd.text.toString()
                val precio: Double = etPrecio.text.toString().toDouble()
                val prod = Producto(id, nombre, precio)
                listaProd.add(prod)

            } catch (ex: Exception) {
                Toast.makeText(
                    this@MainActivity, "Error: los campos están vacíos",
                    Toast.LENGTH_LONG
                ).show()
            }
            rcvLista.layoutManager = LinearLayoutManager(this@MainActivity)
            rcvLista.adapter = ProductoAdapter(listaProd,
                { producto -> selecitem(producto) },
                {position -> eliminarProd(position)},
                {position -> editarProd(position)})
            limpiar()
        }


    }

    fun selecitem(producto: Producto) {
        with(binding) {
            etID.text = producto.id.toString().toEditable()
            etNombreProd.text = producto.nombre.toEditable()
            etPrecio.text = producto.precio.toString().toEditable()
        }
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    fun eliminarProd(position: Int){
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setMessage("¿Desea eliminar el producto?")
            .setCancelable(false)
            .setPositiveButton("Si"){ dialog, id ->
        with(binding){
            listaProd.removeAt(position)
            rcvLista.adapter?.notifyItemRemoved(position)
            limpiar()
        }
            }.setNegativeButton("No"){ dialog, id ->
                dialog.dismiss()
            }
        var alert = builder.create()
        alert.show()

    }
    fun editarProd(position: Int){
        with(binding){
        try {
            val id: Int = etID.text.toString().toInt()
            val nombre: String = etNombreProd.text.toString()
            val precio: Double = etPrecio.text.toString().toDouble()
            val prod = Producto(id, nombre, precio)
            listaProd.set(position, prod)
            rcvLista.adapter?.notifyItemChanged(position)
            limpiar()
        } catch (ex: Exception) {
                Toast.makeText(
                    this@MainActivity, "Error: Seleccione un Item",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}