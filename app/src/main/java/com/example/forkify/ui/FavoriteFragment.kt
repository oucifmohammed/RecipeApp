package com.example.forkify.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forkify.R
import com.example.forkify.adapters.LocalRecipesAdapter
import com.example.forkify.data.local.RecipeEntity
import com.example.forkify.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(), LocalRecipesAdapter.Interaction{

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var localRecipeAdapter: LocalRecipesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        val activity = activity as MainActivity
        activity.supportActionBar?.title = "Favourite"
        MainActivity.viewModel.recipeListLiveData.observe(viewLifecycleOwner,{
            if(it.isEmpty()){
                binding.recyclerView.visibility = View.INVISIBLE
                binding.textView.visibility = View.VISIBLE
            }else{
                binding.textView.visibility = View.INVISIBLE
                binding.recyclerView.visibility = View.VISIBLE
                localRecipeAdapter.submitList(it)
            }
        })
    }

    fun initAdapter(){
        binding.recyclerView.apply {
            localRecipeAdapter = LocalRecipesAdapter(this@FavoriteFragment)
            adapter = localRecipeAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDeleteItem(item: RecipeEntity) {
        MainActivity.viewModel.deleteRecipe(item)
        Toast.makeText(requireContext(),"Recipe has been deleted Successfully",Toast.LENGTH_SHORT).show()
    }

    override fun onItemSelected(item: RecipeEntity) {
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToRecipeFragment(item.imageUrl,item.recipeId)
        findNavController().navigate(action)
    }

}