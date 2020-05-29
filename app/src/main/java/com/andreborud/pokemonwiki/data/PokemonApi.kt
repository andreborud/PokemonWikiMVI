package com.andreborud.pokemonwiki.data

import com.andreborud.pokemonwiki.domain.model.PokemonDetails
import com.andreborud.pokemonwiki.domain.model.PokemonListResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon")
    fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Observable<PokemonListResponse>

    @GET("pokemon/{name}/")
    fun getPokemonDetails(@Path("name") name: String): Observable<PokemonDetails>

}