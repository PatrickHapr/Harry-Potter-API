package com.example.harrypotterapi

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchCharacterActivity : AppCompatActivity() {

    private lateinit var spinnerCharacters: Spinner
    private lateinit var btnSearch: Button
    private lateinit var tvResult: TextView
    private lateinit var ivCharacter: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var apiService: HpApiService

    private var characters: List<Character> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_character)

        initViews()
        setupRetrofit()
        loadCharacters()
    }

    private fun initViews() {
        spinnerCharacters = findViewById(R.id.spinnerCharacters)
        btnSearch = findViewById(R.id.btnSearch)
        tvResult = findViewById(R.id.tvResult)
        ivCharacter = findViewById(R.id.ivCharacter)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://hp-api.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(HpApiService::class.java)
    }

    private fun loadCharacters() {
        progressBar.visibility = View.VISIBLE
        btnSearch.isEnabled = false

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getAllCharacters()
                }

                progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    characters = response.body()!!.sortedBy { it.name }
                    setupSpinner()
                    btnSearch.isEnabled = true
                } else {
                    Toast.makeText(this@SearchCharacterActivity,
                        "Erro ao carregar personagens", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@SearchCharacterActivity,
                    "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSpinner() {
        // Cria lista com "Selecione..." + nomes dos personagens
        val spinnerItems = mutableListOf("Selecione um personagem...")
        spinnerItems.addAll(characters.map { character ->
            if (character.name.isNotEmpty()) {
                character.name
            } else {
                "Personagem sem nome"
            }
        })

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            spinnerItems
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCharacters.adapter = adapter
    }

    fun searchCharacter(view: View) {
        val selectedPosition = spinnerCharacters.selectedItemPosition

        if (selectedPosition == 0) {
            Toast.makeText(this, "Selecione um personagem da lista", Toast.LENGTH_SHORT).show()
            return
        }

        // Pega o personagem selecionado (posição - 1 por causa do "Selecione...")
        val selectedCharacter = characters[selectedPosition - 1]
        val characterId = selectedCharacter.id

        // Faz a busca por ID
        searchCharacterById(characterId)
    }

    private fun searchCharacterById(characterId: String) {
        progressBar.visibility = View.VISIBLE
        tvResult.visibility = View.GONE
        ivCharacter.visibility = View.GONE

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getCharacterById(characterId)
                }

                progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    val character = response.body()!![0]
                    displayCharacter(character)
                } else {
                    tvResult.text = "Personagem não encontrado com o ID: $characterId"
                    tvResult.visibility = View.VISIBLE
                }

            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                tvResult.text = "Erro ao buscar personagem: ${e.message}"
                tvResult.visibility = View.VISIBLE
            }
        }
    }

    private fun displayCharacter(character: Character) {
        val result = """
            Nome: ${character.name}
            Espécie: ${character.species}
            Casa: ${character.house}
            
            ID utilizado: ${character.id}
        """.trimIndent()

        tvResult.text = result
        tvResult.visibility = View.VISIBLE

        // Exibe a foto se disponível
        if (character.image.isNotEmpty()) {
            Picasso.get()
                .load(character.image)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .into(ivCharacter)
            ivCharacter.visibility = View.VISIBLE
        } else {
            ivCharacter.visibility = View.GONE
        }
    }

    fun goBack(view: View) {
        finish()
    }
}