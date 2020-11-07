package com.example.forkify.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.forkify.databinding.HeaderFooterLayoutBinding

class RecipeLoadStateAdapter (val retry: ()-> Unit): LoadStateAdapter<RecipeLoadStateAdapter.FooterHeaderViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): FooterHeaderViewHolder {
        val binding = HeaderFooterLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return FooterHeaderViewHolder(binding)
    }
    override fun onBindViewHolder(holder: FooterHeaderViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class FooterHeaderViewHolder(private val binding: HeaderFooterLayoutBinding): RecyclerView.ViewHolder(binding.root){

        init {
            binding.retryButton.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState){
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                errorMessage.isVisible = loadState !is LoadState.Loading
                retryButton.isVisible = loadState !is LoadState.Loading
            }
        }

    }

}