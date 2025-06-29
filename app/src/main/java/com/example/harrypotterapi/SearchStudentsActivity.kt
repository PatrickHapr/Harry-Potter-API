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

class SearchStudentsActivity : AppCompatActivity() {

    private lateinit var rgHouses: RadioGroup
    private lateinit var rbGryffindor: RadioButton
    private lateinit var rbSlytherin: RadioButton
    private lateinit var rbHufflepuff: RadioButton
    private lateinit var rbRavenclaw: RadioButton
    private lateinit var tvResult: TextView
    private lateinit var scrollView: ScrollView
    private lateinit var progressBar: ProgressBar
    private lateinit var apiService: HpApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_students)

        initViews()
        setupRetrofit()
    }

    private fun initViews() {
        rgHouses = findViewById(R.id.rgHouses)
        rbGryffindor = findViewById(R.id.rbGryffindor)
        rbSlytherin = findViewById(R.id.rbSlytherin)
        rbHufflepuff = findViewById(R.id.rbHufflepuff)
        rbRavenclaw = findViewById(R.id.rbRavenclaw)
        tvResult = findViewById(R.id.tvResult)
        scrollView = findViewById(R.id.scrollView)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://hp-api.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(HpApiService::class.java)
    }

    fun searchStudents(view: View) {
        val selectedHouse = when (rgHouses.checkedRadioButtonId) {
            R.id.rbGryffindor -> "gryffindor"
            R.id.rbSlytherin -> "slytherin"
            R.id.rbHufflepuff -> "hufflepuff"
            R.id.rbRavenclaw -> "ravenclaw"
            else -> {
                Toast.makeText(this, "Selecione uma casa", Toast.LENGTH_SHORT).show()
                return
            }
        }

        progressBar.visibility = View.VISIBLE
        tvResult.visibility = View.GONE

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getCharactersByHouse(selectedHouse)
                }

                progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    val characters = response.body()!!
                    val students = characters.filter { it.hogwartsStudent }
                    displayStudents(students, selectedHouse)
                } else {
                    tvResult.text = "Nenhum estudante encontrado"
                    tvResult.visibility = View.VISIBLE
                }

            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                tvResult.text = "Erro: ${e.message}"
                tvResult.visibility = View.VISIBLE
            }
        }
    }

    private fun displayStudents(students: List<Character>, house: String) {
        val result = StringBuilder("ESTUDANTES DA CASA ${house.uppercase()}:\n\n")

        students.forEach { student ->
            result.append("• ${student.name}\n")
        }

        tvResult.text = result.toString()
        tvResult.visibility = View.VISIBLE
        scrollView.visibility = View.VISIBLE
    }
    fun goBack(view: View) {
        finish()
    }
}