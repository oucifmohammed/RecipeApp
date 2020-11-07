package com.example.forkify.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.forkify.R
import kotlinx.android.synthetic.main.ingredient_viewholder.view.*

class IngredientsRecyclerViewAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var ingredientList: List<String>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return IngredientViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.ingredient_viewholder,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is IngredientViewHolder -> {
                holder.bind("- ${ingredientList[position]}")
            }
        }
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    fun submitList(list: List<String>) {
        ingredientList = list
    }

    class IngredientViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: String) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.ingredient_name.text = item
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: String)
    }
}