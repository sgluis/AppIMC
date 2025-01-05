package com.luis.rimc.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.luis.rimc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActions()
    }

    private fun setupActions() {
        binding.btnCalcular.setOnClickListener {
            if (!areFieldsValid()) {
                showSnackbar("Completa todos los campos correctamente")
            } else {
                val bundle = prepareBundle()
                val intent = Intent(this, SecondActivity::class.java).apply {
                    putExtra("bundle", bundle)
                }
                startActivity(intent)
                finish()
            }
        }
    }

    private fun areFieldsValid(): Boolean {
        // Validar campos vacíos
        return binding.editNombre.text.isNotEmpty() &&
                binding.editPeso.text.isNotEmpty() &&
                binding.editAltura.text.isNotEmpty() &&
                (binding.radioMasculino.isChecked || binding.radioFemenino.isChecked) &&
                areNumbersValid()
    }

    private fun areNumbersValid(): Boolean {
        // Validar que los valores de peso y altura sean números válidos
        return try {
            binding.editPeso.text.toString().toDouble()
            binding.editAltura.text.toString().toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun prepareBundle(): Bundle {
        val nombre = binding.editNombre.text.toString()
        val peso = binding.editPeso.text.toString().toDouble()
        val altura = binding.editAltura.text.toString().toDouble()
        val genero = if (binding.radioMasculino.isChecked) "masculino" else "femenino"

        return Bundle().apply {
            putString("nombre", nombre)
            putDouble("peso", peso)
            putDouble("altura", altura)
            putString("genero", genero)
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}
