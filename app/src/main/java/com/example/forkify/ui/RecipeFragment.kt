package com.example.forkify.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.forkify.adapters.IngredientsRecyclerViewAdapter
import com.example.forkify.databinding.FragmentRecipeBinding
import com.example.forkify.other.Operations
import com.example.forkify.other.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private val args: RecipeFragmentArgs by navArgs()
    private lateinit var recyclerViewAdapter: IngredientsRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeBinding.inflate(inflater,container,false)
        val activity = activity as MainActivity
        activity.supportActionBar?.title = "Ingredients"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        val imageUrl = args.imageUrl
        val recipeId = args.recipeId

        val connected = Operations.checkForInternetConnection(requireContext())
        if(!connected){
            Toast.makeText(requireContext(),"There is no network",Toast.LENGTH_SHORT).show()
        }else {
            MainActivity.viewModel.getRecipeIngredient(recipeId)
        }

        MainActivity.viewModel.recipeIngredientsLiveData.observe(viewLifecycleOwner, {
            if(it.status == Status.LOADING){
                binding.ingredientsProgressBar.visibility = View.VISIBLE
            }else if(it.status == Status.SUCCESS){
                binding.ingredientsProgressBar.visibility = View.INVISIBLE
                binding.linearLayout.visibility = View.VISIBLE
                binding.imageView.visibility = View.VISIBLE
                Glide.with(requireContext()).load(imageUrl).into(binding.imageView)
                recyclerViewAdapter.submitList(it.data!!.ingredients.ingredients!!)
            }
        })

    }

    private fun initAdapter(){
        binding.ingredientsRecyclerView.apply {
            recyclerViewAdapter = IngredientsRecyclerViewAdapter()
            adapter = recyclerViewAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}