package com.andreborud.pokemonwiki.domain.model

import com.google.gson.annotations.SerializedName

data class PokemonDetails(
    val abilities: List<PokemonAbility>,
    @SerializedName("base_experience") val baseExperience: Int,
    val height: Int,
    val id: Int,
    val name: String,
    val species: Pokemon, // Reuse the Pokemon Data Class as its only expecting a name and a url
    val sprites: Sprites,
    val stats: List<PokemonStat>,
    val types: List<PokemonType>,
    val weight: Int
)

interface PokemonInfo

data class PokemonAbility(
    val ability: Pokemon, // Reuse the Pokemon Data Class as its only expecting a name and a url
    @SerializedName("is_hidden") val isHidden: Boolean,
    val slot: Int
) : PokemonInfo

data class Sprites(
    @SerializedName("back_default") val backDefault: String?,
    @SerializedName("back_female") val backFemale: String?,
    @SerializedName("back_shiny") val backShiny: String?,
    @SerializedName("back_shiny_female") val backShiny_female: String?,
    @SerializedName("front_default") val frontDefault: String?,
    @SerializedName("front_female") val frontFemale: String?,
    @SerializedName("front_shiny") val frontShiny: String?,
    @SerializedName("front_shiny_female") val frontShinyFemale: String?
)

data class PokemonStat(
    @SerializedName("base_stat") val baseStat: Int,
    val effort: Int,
    val stat: Pokemon // Reuse the Pokemon Data Class as its only expecting a name and a url
) : PokemonInfo

data class PokemonType(
    val slot: Int,
    val type: Pokemon // Reuse the Pokemon Data Class as its only expecting a name and a url
) : PokemonInfo
