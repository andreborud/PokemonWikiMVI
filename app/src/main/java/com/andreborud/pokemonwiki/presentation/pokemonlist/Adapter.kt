package com.andreborud.pokemonwiki.presentation.pokemonlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andreborud.pokemonwiki.domain.model.Pokemon
import com.andreborud.pokemonwiki.BR
import com.andreborud.pokemonwiki.databinding.PokemonListItemBinding

typealias ClickListener = (Pokemon) -> Unit

class Adapter(private val clickListener: ClickListener): ListAdapter<Pokemon, DataBindingViewHolder>(object : DiffUtil.ItemCallback<Pokemon>() {
    override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        return oldItem.name == newItem.name
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
        return oldItem == newItem
    }
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder =
        DataBindingViewHolder(PokemonListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), clickListener)

    override fun onBindViewHolder(holder: DataBindingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<Pokemon>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    fun appendList(list: List<Pokemon>) {
        val items = mutableListOf<Pokemon>()
        if (this.currentList.isNotEmpty())
            items.addAll(this.currentList)
        items.addAll(list)
        submitList(items)
    }
}

class DataBindingViewHolder(private val binding: ViewDataBinding, private val clickListener: ClickListener): RecyclerView.ViewHolder(binding.root){

    @SuppressLint("DefaultLocale")
    fun bind(pokemon: Pokemon) {
        binding.setVariable(BR.name, pokemon.name.capitalize())
        binding.executePendingBindings()

        binding.root.setOnClickListener {
            clickListener(pokemon)
        }
    }
}
