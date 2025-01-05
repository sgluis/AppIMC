package com.luis.rimc.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.luis.rimc.R
import com.luis.rimc.adapters.AdaptadorResultado
import com.luis.rimc.dao.UserDAO
import com.luis.rimc.databinding.ActivitySecondBinding
import com.luis.rimc.model.Resultado
import com.luis.rimc.model.Usuario
import java.util.Locale
import kotlin.math.pow

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySecondBinding
    private lateinit var nombre: String
    private var peso: Double = 0.0
    private var altura: Double = 0.0
    private lateinit var genero: String
    private lateinit var listaResultados: ArrayList<Resultado>
    private lateinit var adaptadorResultado: AdaptadorResultado
    private var formula: Double = 0.0
    private lateinit var userDAO: UserDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeComponents()
        setupActions()
    }

    private fun initializeComponents() {
        val bundle = intent.extras?.getBundle("bundle")
        nombre = bundle?.getString("nombre") ?: ""
        peso = bundle?.getDouble("peso") ?: 0.0
        altura = bundle?.getDouble("altura") ?: 0.0
        genero = bundle?.getString("genero") ?: ""
        listaResultados = getResults()
        adaptadorResultado = AdaptadorResultado(listaResultados, this)
        binding.recycleResultado.adapter = adaptadorResultado
        binding.recycleResultado.layoutManager = LinearLayoutManager(this)
        formula = calculateBMI(peso, altura)
        userDAO = UserDAO(applicationContext)
    }

    private fun getResults(): ArrayList<Resultado> {
        return arrayListOf(
            Resultado("Bajo peso", R.drawable.estado1),
            Resultado("Peso Normal", R.drawable.estado2),
            Resultado("Sobrepeso", R.drawable.estado3),
            Resultado("Obesidad I", R.drawable.estado4),
            Resultado("Obesidad II", R.drawable.estado5),
            Resultado("Obesidad III", R.drawable.estado6)
        )
    }

    private fun calculateBMI(peso: Double, altura: Double): Double {
        return if (altura != 0.0) peso / altura.pow(2) else 0.0
    }

    private fun setupActions() {
        setupWelcomeMessage()
        setupUserDetails()
        setupBMIResult()
        handleUserData()
        setupBackButton()
    }

    private fun setupWelcomeMessage() {
        binding.textBienvenida.text = "Hola, $nombre"
    }

    private fun setupUserDetails() {
        binding.textPeso.text = "PESO: $peso"
        binding.textAltura.text = "ALTURA: $altura"
        binding.textGenero.text = "GENERO: ${genero.uppercase()}"
    }

    private fun setupBMIResult() {
        binding.textResultado.text = "IMC: ${String.format(Locale("es", "ES"), "%.2f", formula)}"
    }

    private fun handleUserData() {
        if (userDAO.existeUsuario(nombre)) {
            binding.textUltimoPeso.text = "ULTIMO PESO: ${(userDAO.obtenerUsuarioPorNombre(nombre)?.peso ?: 0.0)}"
        }

        val filteredResults = filterResultsByBMI(formula)
        adaptadorResultado.actualizarLista(filteredResults)

        if (!userDAO.existeUsuario(nombre)) {
            userDAO.insertarUsuario(Usuario(nombre, peso, altura, genero))
        } else {
            val existingUser = userDAO.obtenerUsuarioPorNombre(nombre)
            if (existingUser?.peso != peso) {
                userDAO.actualizarDatos(Usuario(nombre, peso, altura, genero), peso)
            }
        }
    }

    private fun filterResultsByBMI(imc: Double): ArrayList<Resultado> {
        return when (imc) {
            in 0.0..18.5 -> arrayListOf(listaResultados[0])
            in 18.6..24.9 -> arrayListOf(listaResultados[1])
            in 25.0..29.9 -> arrayListOf(listaResultados[2])
            in 30.0..34.9 -> arrayListOf(listaResultados[3])
            in 35.0..39.9 -> arrayListOf(listaResultados[4])
            in 40.0..100.0 -> arrayListOf(listaResultados[5])
            else -> arrayListOf()
        }
    }

    private fun setupBackButton() {
        binding.buttonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
