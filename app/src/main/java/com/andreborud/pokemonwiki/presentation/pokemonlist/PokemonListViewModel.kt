package com.andreborud.pokemonwiki.presentation.pokemonlist

import com.andreborud.pokemonwiki.domain.model.States
import com.andreborud.pokemonwiki.domain.GetPokemonListUseCase
import com.ww.roxie.BaseViewModel
import com.ww.roxie.Reducer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PokemonListViewModel(private val getPokemonListUseCase: GetPokemonListUseCase) :  BaseViewModel<Action, State>() {

    override val initialState = State(state = States.IDLE)

    private val reducer: Reducer<State, Change> = { state, change ->
        when (change) {
            is Change.Loading -> state.copy(
                state = States.LOADING,
                pokemonList = emptyList(),
                errorMessage = ""
            )
            is Change.Data -> state.copy(
                state = States.DATA,
                pokemonList = change.pokemonList,
                errorMessage = ""
            )
            is Change.Error -> state.copy(
                state = States.ERROR,
                pokemonList = emptyList(),
                errorMessage = change.throwable?.message ?: "Something went wrong!"
            )
        }
    }

    init {
        bindActions()
    }

    private fun bindActions() {
        val getPokemonList = actions.ofType<Action.GetPokemonList>()
            .switchMap {
                getPokemonListUseCase.withOffset(it.offset)
                    .subscribeOn(Schedulers.io())
                    .map<Change> { pokemonList -> Change.Data(pokemonList) }
                    .defaultIfEmpty(Change.Data(emptyList()))
                    .onErrorReturn { throwable -> Change.Error(throwable) }
                    .startWith(Change.Loading)
            }

        disposables += getPokemonList
            .scan(initialState, reducer)
            .filter { it.state != States.IDLE }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(state::setValue, Timber::e)
    }
}