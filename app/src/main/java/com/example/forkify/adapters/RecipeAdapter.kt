package com.example.forkify.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.forkify.R
import com.example.forkify.data.remote.Recipe
import com.example.forkify.databinding.RecipeCardBinding

class RecipeAdapter(private val interaction: Interaction? = null) :
    PagingDataAdapter<Recipe, RecipeAdapter.RecipeViewHolder>(recipeComparator) {

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {

        val binding = RecipeCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecipeViewHolder(binding, interaction)
    }

    companion object {
        val recipeComparator = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem == newItem
            }
        }
    }

     class RecipeViewHolder (private val binding: RecipeCardBinding, private val interaction: Interaction?) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe?) {
            if (recipe == null) {
                binding.apply {
                    recipeTitle.text = "Loading"
                    publisherName.text = "Loading"
                }
            } else {
                showRecipeData(recipe)
            }
        }

        private fun showRecipeData(recipe: Recipe) {
            binding.apply {
                Glide.with(binding.root)
                    .load(recipe.imageUrl)
                    .centerCrop()
                    .error(R.drawable.cancel_icon)
                    .into(recipeImage)
                recipeTitle.text = recipe.title
                publisherName.text = recipe.publisher

                itemView.setOnClickListener {
                    interaction!!.onItemSelected(recipe)
                }

                addAction.setOnClickListener {
                    interaction!!.onAddItem(recipe)
                }
            }

        }
    }

    interface Interaction {
        fun onItemSelected(item: Recipe)

        fun onAddItem(item: Recipe)
    }
}