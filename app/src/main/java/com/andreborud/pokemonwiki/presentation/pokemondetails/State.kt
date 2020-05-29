package com.andreborud.pokemonwiki.presentation.pokemondetails

import com.andreborud.pokemonwiki.domain.model.States
import com.andreborud.pokemonwiki.domain.model.PokemonDetails
import com.ww.roxie.BaseState

data class State(
    val pokemonDetails: PokemonDetails? = null,
    val state: States = States.IDLE,
    val errorMessage: String = ""
) : BaseState