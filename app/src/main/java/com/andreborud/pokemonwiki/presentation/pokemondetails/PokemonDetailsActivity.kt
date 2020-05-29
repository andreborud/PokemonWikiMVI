package com.andreborud.pokemonwiki.presentation.pokemondetails

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreborud.pokemonwiki.R
import com.andreborud.pokemonwiki.domain.model.States
import com.andreborud.pokemonwiki.domain.model.Pokemon
import com.andreborud.pokemonwiki.domain.model.PokemonDetails
import com.andreborud.pokemonwiki.databinding.ActivityPokemonDetailsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.activity_pokemon_details.*
import kotlinx.android.synthetic.main.activity_pokemon_details.recyclerView
import kotlinx.android.synthetic.main.activity_pokemon_details.toolbar
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class PokemonDetailsActivity : AppCompatActivity() {

    // Lazy Inject ViewModel
    private val viewModel: PokemonDetailsViewModel by viewModel()
    private var adapter = Adapter()
    private var currentTab = 0

    @SuppressLint("DefaultLocale")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_details)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_pokemon_details)

        viewModel.observableState.observe(this, Observer { state ->
            state?.let { renderState(state) }
        })

        val pokemon: Pokemon? = intent.getParcelableExtra("pokemon")
        loadData(pokemon!!.name)

        toolbar.title = pokemon.name.capitalize()

        tabs.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) { }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabSelected(tab: TabLayout.Tab) {
                currentTab = tab.position
                loadListData()
            }
        })

        setupRecyclerView()

        swipeRefreshLayout.isEnabled = false
    }

    private fun loadData(name: String) {
        viewModel.dispatch(Action.GetPokemonDetails(name))
    }

    private fun setupRecyclerView() {
        recyclerView.apply {
            adapter = this@PokemonDetailsActivity.adapter
            layoutManager = LinearLayoutManager(this@PokemonDetailsActivity)
        }
    }

    private fun renderState(state: State) {
        when (state.state) {
            States.LOADING -> renderLoading()
            States.ERROR -> renderError(state.errorMessage)
            else -> renderData(state.pokemonDetails!!)
        }
    }

    private fun renderLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    private fun renderError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun renderData(pokemonDetails: PokemonDetails?) {
        if (pokemonDetails == null) return
        binding.pokemonDetails = pokemonDetails
        Timber.i(pokemonDetails.name)
        loadListData()
        swipeRefreshLayout.isRefreshing = false
    }

    private fun loadListData() {
        binding.pokemonDetails?.let{
            adapter.submitList(when (currentTab) {
                0 -> it.stats
                1 -> it.abilities
                else -> it.types
            })
        }
    }

    companion object {

        lateinit var binding: ActivityPokemonDetailsBinding

        /**
         * Custom binding to load image from url straight into an imageview
         */
        @BindingAdapter("loadImage")
        @JvmStatic
        fun loadImage(view: ImageView, imageUrl: String?) {
            imageUrl?.let {
                Glide.with(view.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.image_loader)
                    .apply(RequestOptions().circleCrop())
                    .into(view)
            }
        }

        @BindingAdapter("setHtmlText")
        @JvmStatic
        fun setHtmlText(view: TextView, text: Int?) {
            text?.let {
                val htmlString = when (view.id) {
                    R.id.height -> view.context.resources.getString(R.string.height, text.toString())
                    else -> view.context.resources.getString(R.string.weight, text.toString())
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.text = Html.fromHtml(htmlString, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    @Suppress("DEPRECATION")
                    view.text = Html.fromHtml(htmlString)
                }
            }
        }

    }
}
