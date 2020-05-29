package com.andreborud.pokemonwiki.presentation.pokemonlist

import com.andreborud.pokemonwiki.domain.model.Pokemon

sealed class Change {
    object Loading : Change()
    data class Data(val pokemonList: List<Pokemon>) : Change()
    data class Error(val throwable: Throwable?) : Change()
}