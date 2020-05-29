package com.andreborud.pokemonwiki.di

import com.andreborud.pokemonwiki.data.PokemonApi
import com.andreborud.pokemonwiki.domain.GetPokemonDetailsUseCase
import com.andreborud.pokemonwiki.domain.GetPokemonListUseCase
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val apiModule = module {
    // single instance of Pokemon Api
    single<PokemonApi> {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()

        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        retrofit.create(PokemonApi::class.java)
    }

    // single instance of PokemonListRepository
    single {
        GetPokemonListUseCase(
            get()
        )
    }
    // single instance of PokemonDetailsRepository
    single {
        GetPokemonDetailsUseCase(
            get()
        )
    }
}