package com.andreborud.pokemonwiki.presentation.pokemonlist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andreborud.pokemonwiki.R
import com.andreborud.pokemonwiki.domain.model.Pokemon
import com.andreborud.pokemonwiki.domain.model.States
import com.andreborud.pokemonwiki.presentation.pokemondetails.PokemonDetailsActivity
import kotlinx.android.synthetic.main.activity_pokemon_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class PokemonListActivity : AppCompatActivity() {

    // Lazy Inject ViewModel
    private val viewModel: PokemonListViewModel by viewModel()
    private val clickListener: ClickListener = this::onPokemonClicked
    private var adapter = Adapter(clickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_list)
        setupRecyclerView()
        setupSwipeRefreshLayout()

        toolbar.title = getString(R.string.app_name)

        viewModel.observableState.observe(this, Observer { state ->
            state?.let { renderState(state) }
        })

        loadData(0)
    }

    private fun loadData(currentLength: Int) {
        viewModel.dispatch(Action.GetPokemonList(currentLength))
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            adapter = this@PokemonListActivity.adapter
            layoutManager = LinearLayoutManager(this@PokemonListActivity)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    val visibleItemCount: Int = layoutManager?.childCount!!
                    val totalItemCount: Int = layoutManager?.itemCount!!
                    val pastVisibleItems: Int = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                        loadData(this@PokemonListActivity.adapter.itemCount)
                    }
                }
            })
        }
    }

    private fun setupSwipeRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener {
            loadData(0)
        }
    }

    private fun renderState(state: State) {
        when (state.state) {
            States.LOADING -> renderLoading()
            States.ERROR -> renderError(state.errorMessage)
            else -> renderData(state.pokemonList)
        }
    }

    private fun renderLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    private fun renderError(errorMessage: String) {
//        adapter.loadMoreModule?.loadMoreComplete()
        swipeRefreshLayout.isRefreshing = false
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun renderData(pokemonList: List<Pokemon>) {
        swipeRefreshLayout.isRefreshing = false
        adapter.appendList(pokemonList.toMutableList())
    }

    private fun onPokemonClicked(pokemon: Pokemon) {
        val intent = Intent(this, PokemonDetailsActivity::class.java)
        intent.putExtra("pokemon", pokemon)
        startActivity(intent)
    }
}
