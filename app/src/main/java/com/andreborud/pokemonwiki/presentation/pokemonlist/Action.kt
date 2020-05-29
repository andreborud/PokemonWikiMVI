package com.andreborud.pokemonwiki.presentation.pokemonlist

import com.ww.roxie.BaseAction

sealed class Action : BaseAction {
    data class GetPokemonList(val offset: Int) : Action()
}