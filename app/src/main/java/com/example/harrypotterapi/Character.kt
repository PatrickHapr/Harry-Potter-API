package com.example.harrypotterapi

data class Character(
    val id: String,
    val name: String,
    val alternate_names: List<String>,
    val species: String,
    val house: String,
    val hogwartsStudent: Boolean,
    val image: String
)