package com.andreborud.pokemonwiki.presentation.pokemondetails

import com.ww.roxie.BaseAction

sealed class Action : BaseAction {
    data class GetPokemonDetails(val name: String) : Action()
}