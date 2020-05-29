package com.andreborud.pokemonwiki.domain.model

data class PokemonListResponse(val count: Int, val next: String, val results: List<Pokemon>)