package com.andreborud.pokemonwiki.pokemonlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.andreborud.pokemonwiki.RxTestSchedulerRule
import com.andreborud.pokemonwiki.domain.GetPokemonListUseCase
import com.andreborud.pokemonwiki.domain.model.Pokemon
import com.andreborud.pokemonwiki.domain.model.States
import com.andreborud.pokemonwiki.presentation.pokemonlist.Action
import com.andreborud.pokemonwiki.presentation.pokemonlist.PokemonListViewModel
import com.andreborud.pokemonwiki.presentation.pokemonlist.State
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PokemonListViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testSchedulerRule = RxTestSchedulerRule()

    private lateinit var testSubject: PokemonListViewModel
    private val loadingState = State(state = States.LOADING)
    private val pokemonListUseCase = mock<GetPokemonListUseCase>()
    private val observer = mock<Observer<State>>()

    @Before
    fun setUp() {
        testSubject = PokemonListViewModel(pokemonListUseCase)
        testSubject.observableState.observeForever(observer)
    }

    @Test
    fun `Given pokemons successfully loaded, when action GetPokemonList is received, then State contains pokemons`() {
        // GIVEN
        val pokemonList = listOf(Pokemon("Bulbasaur", "https://pokeapi.co/api/v2/pokemon/1"))
        val successState = State(pokemonList = pokemonList, state = States.DATA)

        whenever(pokemonListUseCase.withOffset(0)).thenReturn(Observable.just(pokemonList))

        // WHEN
        testSubject.dispatch(Action.GetPokemonList(0))
        testSchedulerRule.triggerActions()

        // THEN
        inOrder(observer) {
            verify(observer).onChanged(loadingState)
            verify(observer).onChanged(successState)
        }
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun `Given pokemons failed to load, when action GetPokemonList is received, then State contains error`() {
        // GIVEN
        val errMsg = "Err!"
        val errorState = State(errorMessage = errMsg, state = States.ERROR)

        whenever(pokemonListUseCase.withOffset(0))
            .thenReturn(Observable.error(Throwable(message = errMsg)))

        // WHEN
        testSubject.dispatch(Action.GetPokemonList(0))
        testSchedulerRule.triggerActions()


        // THEN
        inOrder(observer) {
            verify(observer).onChanged(loadingState)
            verify(observer).onChanged(errorState)
        }
        verifyNoMoreInteractions(observer)
    }
}