package com.example.harrypotterapi

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun searchCharacterById(view: View) {
        startActivity(Intent(this, SearchCharacterActivity::class.java))
    }

    fun searchStaff(view: View) {
        startActivity(Intent(this, SearchStaffActivity::class.java))
    }

    fun searchStudentsByHouse(view: View) {
        startActivity(Intent(this, SearchStudentsActivity::class.java))
    }

    fun viewSpells(view: View) {
        startActivity(Intent(this, SpellsActivity::class.java))
    }

    fun exitApp(view: View) {
        finishAffinity()
    }
}