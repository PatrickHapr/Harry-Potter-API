package com.example.harrypotterapi

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SpellDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spell_detail)

        // Configurar ActionBar para mostrar botão de voltar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detalhes do Feitiço"

        val spellName = intent.getStringExtra("spell_name") ?: "Feitiço Desconhecido"
        val spellDescription = intent.getStringExtra("spell_description") ?: "Descrição não disponível"

        findViewById<TextView>(R.id.tvSpellName).text = spellName
        findViewById<TextView>(R.id.tvSpellDescription).text = spellDescription
    }

    fun goBack(view: View) {
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}