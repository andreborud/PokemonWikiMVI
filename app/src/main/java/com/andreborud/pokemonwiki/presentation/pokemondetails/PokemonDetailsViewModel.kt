package com.andreborud.pokemonwiki.presentation.pokemondetails

import com.andreborud.pokemonwiki.domain.model.States
import com.andreborud.pokemonwiki.domain.GetPokemonDetailsUseCase
import com.ww.roxie.BaseViewModel
import com.ww.roxie.Reducer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PokemonDetailsViewModel(private val getPokemonDetailsUseCase: GetPokemonDetailsUseCase) :  BaseViewModel<Action, State>() {

    override val initialState = State(state = States.IDLE)

    private val reducer: Reducer<State, Change> = { state, change ->
        when (change) {
            is Change.Loading -> state.copy(
                state = States.LOADING,
                pokemonDetails = null,
                errorMessage = ""
            )
            is Change.Data -> state.copy(
                state = States.DATA,
                pokemonDetails = change.pokemonDetails,
                errorMessage = ""
            )
            is Change.Error -> state.copy(
                state = States.ERROR,
                pokemonDetails = null,
                errorMessage = change.throwable?.message ?: "Something went wrong!"
            )
        }
    }

    init {
        bindActions()
    }

    private fun bindActions() {
        val getPokemonDetails = actions.ofType<Action.GetPokemonDetails>()
            .switchMap {
                getPokemonDetailsUseCase.getByName(name = it.name)
                    .subscribeOn(Schedulers.io())
                    .map<Change> { pokemonDetails -> Change.Data(pokemonDetails) }
                    .defaultIfEmpty(Change.Data(null))
                    .onErrorReturn { throwable -> Change.Error(throwable) }
                    .startWith(Change.Loading)
            }

        disposables += getPokemonDetails
            .scan(initialState, reducer)
            .filter { it.state != States.IDLE }
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(state::setValue, Timber::e)
    }
}