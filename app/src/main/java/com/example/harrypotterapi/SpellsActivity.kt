package com.example.harrypotterapi

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SpellsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: android.widget.ProgressBar
    private lateinit var apiService: HpApiService
    private lateinit var spellsAdapter: SpellsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spells)

        initViews()
        setupRetrofit()
        setupRecyclerView()
        loadSpells()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://hp-api.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(HpApiService::class.java)
    }

    private fun setupRecyclerView() {
        spellsAdapter = SpellsAdapter { spell ->
            val intent = Intent(this, SpellDetailActivity::class.java)
            intent.putExtra("spell_name", spell.name)
            intent.putExtra("spell_description", spell.description)
            startActivity(intent)
        }

        recyclerView.adapter = spellsAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadSpells() {
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getAllSpells()
                }

                progressBar.visibility = View.GONE

                if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                    spellsAdapter.updateSpells(response.body()!!)
                }

            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                // Handle error
            }
        }
    }
    fun goBack(view: View) {
        finish()
    }
}