package com.andreborud.pokemonwiki.domain

import com.andreborud.pokemonwiki.data.PokemonApi
import com.andreborud.pokemonwiki.domain.model.PokemonDetails
import io.reactivex.Observable

class GetPokemonDetailsUseCase(private val pokemonApi: PokemonApi) {
    fun getByName(name: String): Observable<PokemonDetails> {
        return pokemonApi.getPokemonDetails(name = name)
            .map { it }
    }
}