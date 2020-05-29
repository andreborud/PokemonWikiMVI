package com.andreborud.pokemonwiki.presentation.pokemondetails

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andreborud.pokemonwiki.BR
import com.andreborud.pokemonwiki.databinding.PokemonInfoListItemBinding
import com.andreborud.pokemonwiki.domain.model.PokemonAbility
import com.andreborud.pokemonwiki.domain.model.PokemonInfo
import com.andreborud.pokemonwiki.domain.model.PokemonStat
import com.andreborud.pokemonwiki.domain.model.PokemonType

class Adapter : ListAdapter<PokemonInfo, DataBindingViewHolder>(object : DiffUtil.ItemCallback<PokemonInfo>() {
    override fun areItemsTheSame(oldItem: PokemonInfo, newItem: PokemonInfo): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: PokemonInfo, newItem: PokemonInfo): Boolean {
        return oldItem == newItem
    }
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder =
        DataBindingViewHolder(PokemonInfoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: DataBindingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<PokemonInfo>?) {
        super.submitList(list?.let { ArrayList(it) })
    }
}

class DataBindingViewHolder(private val binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root){

    @SuppressLint("DefaultLocale")
    fun bind(pokemonInfo: PokemonInfo) {
        if (pokemonInfo is PokemonStat) {
            binding.setVariable(BR.title, pokemonInfo.stat.name.capitalize() + ":")
            binding.setVariable(BR.value, pokemonInfo.baseStat.toString())
        } else if (pokemonInfo is PokemonAbility) {
            binding.setVariable(BR.title, pokemonInfo.ability.name.capitalize() + ":")
            binding.setVariable(BR.value, if (pokemonInfo.isHidden) "Hidden" else "Known")
        } else if (pokemonInfo is PokemonType) {
            binding.setVariable(BR.title, pokemonInfo.type.name.capitalize())
            binding.setVariable(BR.value, "Slot: " + pokemonInfo.slot.toString())
        }
        binding.executePendingBindings()


    }
}
