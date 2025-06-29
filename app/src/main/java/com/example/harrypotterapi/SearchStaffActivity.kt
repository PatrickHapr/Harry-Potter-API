package com.example.harrypotterapi

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchStaffActivity : AppCompatActivity() {

    private lateinit var etStaffName: EditText
    private lateinit var tvResult: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var apiService: HpApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_staff)

        initViews()
        setupRetrofit()
    }

    private fun initViews() {
        etStaffName = findViewById(R.id.etStaffName)
        tvResult = findViewById(R.id.tvResult)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://hp-api.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(HpApiService::class.java)
    }

    fun searchStaff(view: View) {
        val staffName = etStaffName.text.toString().trim().lowercase()

        progressBar.visibility = View.VISIBLE
        tvResult.visibility = View.GONE

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getStaff()
                }

                progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    val staff = response.body()!!

                    if (staffName.isEmpty()) {
                        displayAllStaff(staff)
                    } else {
                        val filteredStaff = staff.filter {
                            it.name.lowercase().contains(staffName)
                        }
                        if (filteredStaff.isNotEmpty()) {
                            displayFilteredStaff(filteredStaff)
                        } else {
                            tvResult.text = "Professor não encontrado"
                            tvResult.visibility = View.VISIBLE
                        }
                    }
                } else {
                    tvResult.text = "Erro ao buscar professores"
                    tvResult.visibility = View.VISIBLE
                }

            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                tvResult.text = "Erro: ${e.message}"
                tvResult.visibility = View.VISIBLE
            }
        }
    }

    private fun displayAllStaff(staff: List<Character>) {
        val result = StringBuilder("PROFESSORES DE HOGWARTS:\n\n")

        staff.forEach { teacher ->
            result.append("Nome: ${teacher.name}\n")
            result.append("Nomes Alternativos: ${teacher.alternate_names.joinToString(", ")}\n")
            result.append("Espécie: ${teacher.species}\n")
            result.append("Casa: ${teacher.house}\n")
            result.append("─────────────────\n")
        }

        tvResult.text = result.toString()
        tvResult.visibility = View.VISIBLE
    }

    private fun displayFilteredStaff(staff: List<Character>) {
        val result = StringBuilder("PROFESSOR ENCONTRADO:\n\n")

        staff.forEach { teacher ->
            result.append("Nome: ${teacher.name}\n")
            result.append("Nomes Alternativos: ${teacher.alternate_names.joinToString(", ")}\n")
            result.append("Espécie: ${teacher.species}\n")
            result.append("Casa: ${teacher.house}\n")
        }

        tvResult.text = result.toString()
        tvResult.visibility = View.VISIBLE
    }
    fun goBack(view: View) {
        finish()
    }
}