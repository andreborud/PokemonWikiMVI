package com.andreborud.pokemonwiki.presentation.pokemonlist

import com.andreborud.pokemonwiki.domain.model.Pokemon
import com.andreborud.pokemonwiki.domain.model.States
import com.ww.roxie.BaseState

data class State(
    val pokemonList: List<Pokemon> = emptyList(),
    val state: States = States.IDLE,
    val errorMessage: String = ""
) : BaseState