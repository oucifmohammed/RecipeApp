package com.example.forkify.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.forkify.R
import com.example.forkify.data.local.RecipeEntity
import kotlinx.android.synthetic.main.local_recipe_card.view.*

class LocalRecipesAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecipeEntity>() {

        override fun areItemsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean {
            return newItem == oldItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return LocalRecipeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.local_recipe_card,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LocalRecipeViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<RecipeEntity>) {
        differ.submitList(list)
    }

    class LocalRecipeViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: RecipeEntity) = with(itemView) {
            itemView.delete_action.setOnClickListener {
                interaction?.onDeleteItem(item)
            }

            itemView.setOnClickListener {
                interaction?.onItemSelected(item)
            }

            itemView.recipe_title.text = item.recipeTitle
            itemView.publisher_name.text = item.publisher
            Glide.with(itemView.context).load(item.imageUrl).into(itemView.recipeImage)
        }
    }

    interface Interaction {
        fun onDeleteItem(item: RecipeEntity)
        fun onItemSelected(item: RecipeEntity)
    }
}