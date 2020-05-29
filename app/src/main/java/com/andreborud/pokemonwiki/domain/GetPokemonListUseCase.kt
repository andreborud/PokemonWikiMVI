package com.andreborud.pokemonwiki.domain

import com.andreborud.pokemonwiki.domain.model.Pokemon
import com.andreborud.pokemonwiki.data.PokemonApi
import io.reactivex.Observable

class GetPokemonListUseCase(private val pokemonApi: PokemonApi) {
    fun withOffset(offset: Int): Observable<List<Pokemon>> {
        return pokemonApi.getPokemonList(
                offset = offset,
                limit = 30
            )
            .map { it.results }
    }
}
