package com.andreborud.pokemonwiki.di

import com.andreborud.pokemonwiki.presentation.pokemondetails.PokemonDetailsViewModel
import com.andreborud.pokemonwiki.presentation.pokemonlist.PokemonListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val pokemonListVMModule = module {
    // PokemonListViewModel
    viewModel { PokemonListViewModel(get()) }
}

val pokemonDetailsVMModule = module {
    // PokemonListViewModel
    viewModel { PokemonDetailsViewModel(get()) }
}
