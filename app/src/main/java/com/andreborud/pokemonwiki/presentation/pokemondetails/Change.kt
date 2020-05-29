package com.andreborud.pokemonwiki.presentation.pokemondetails

import com.andreborud.pokemonwiki.domain.model.PokemonDetails

sealed class Change {
    object Loading : Change()
    data class Data(val pokemonDetails: PokemonDetails?) : Change()
    data class Error(val throwable: Throwable?) : Change()
}