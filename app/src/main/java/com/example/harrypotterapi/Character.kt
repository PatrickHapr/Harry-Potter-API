package com.example.harrypotterapi

data class Character(
    val id: String,
    val name: String,
    val alternate_names: List<String>,
    val species: String,
    val gender: String,
    val house: String,
    val dateOfBirth: String?,
    val yearOfBirth: Int?,
    val wizard: Boolean,
    val ancestry: String,
    val eyeColour: String,
    val hairColour: String,
    val wand: Wand?,
    val patronus: String,
    val hogwartsStudent: Boolean,
    val hogwartsStaff: Boolean,
    val actor: String,
    val alternate_actors: List<String>,
    val alive: Boolean,
    val image: String
)