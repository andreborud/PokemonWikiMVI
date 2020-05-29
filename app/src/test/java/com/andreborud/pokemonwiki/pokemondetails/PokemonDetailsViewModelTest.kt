/*
* Copyright (C) 2019. WW International, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.andreborud.pokemonwiki.pokemondetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.andreborud.pokemonwiki.RxTestSchedulerRule
import com.andreborud.pokemonwiki.domain.GetPokemonDetailsUseCase
import com.andreborud.pokemonwiki.domain.model.*
import com.andreborud.pokemonwiki.presentation.pokemondetails.Action
import com.andreborud.pokemonwiki.presentation.pokemondetails.PokemonDetailsViewModel
import com.andreborud.pokemonwiki.presentation.pokemondetails.State
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PokemonDetailsViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testSchedulerRule = RxTestSchedulerRule()

    private lateinit var testSubject: PokemonDetailsViewModel
    private val pokemonDetailsUseCase = mock<GetPokemonDetailsUseCase>()
    private val loadingState = State(state = States.LOADING)
    private val observer = mock<Observer<State>>()

    @Before
    fun setUp() {
        testSubject = PokemonDetailsViewModel(pokemonDetailsUseCase)
        testSubject.observableState.observeForever(observer)
    }

    @Test
    fun `Given note successfully loaded, when action LoadNoteDetail is received, then State contains note`() {
        // GIVEN
        val pokemonDetails = PokemonDetails(
            abilities = listOf(PokemonAbility(ability = Pokemon("Strength", "url"), isHidden = true, slot = 0)),
            baseExperience = 100,
            height = 23,
            id = 1,
            name = "bulbasaur",
            species = Pokemon("Strength", "url"), // Reuse the Pokemon Data Class as its only expecting a name and a url
            sprites = Sprites("", "", "", "", "", "", "", ""),
            stats = listOf(PokemonStat(10,10, Pokemon("Speed", "url"))),
            types = listOf(PokemonType(1, Pokemon("Fire", "url"))),
            weight = 4
        )
        val successState = State(pokemonDetails = pokemonDetails, state = States.DATA)

        whenever(pokemonDetailsUseCase.getByName("bulbasaur")).thenReturn(Observable.just(pokemonDetails))

        // WHEN
        testSubject.dispatch(Action.GetPokemonDetails("bulbasaur"))
        testSchedulerRule.triggerActions()

        // THEN
        inOrder(observer) {
            verify(observer).onChanged(loadingState)
            verify(observer).onChanged(successState)
        }
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun `Given note failed to load, when action LoadNoteDetail is received, then State contains error`() {
        // GIVEN
        val errMsg = "Err!"
        val loadErrorState = State(errorMessage = errMsg, state = States.ERROR)

        whenever(pokemonDetailsUseCase.getByName("bulbasaur"))
            .thenReturn(Observable.error(Throwable(message = errMsg)))

        // WHEN
        testSubject.dispatch(Action.GetPokemonDetails("bulbasaur"))
        testSchedulerRule.triggerActions()

        // THEN
        inOrder(observer) {
            verify(observer).onChanged(loadingState)
            verify(observer).onChanged(loadErrorState)
        }
        verifyNoMoreInteractions(observer)
    }

}